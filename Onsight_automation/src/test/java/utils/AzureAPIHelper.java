package utils;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.*;

public class AzureAPIHelper {
    String ORG, PROJECT, PAT, BASE_URL;

    public AzureAPIHelper(String PAT, String ORG, String PROJECT) {
        this.PAT = PAT;
        this.ORG = ORG;
        this.PROJECT = PROJECT;
        BASE_URL = String.format("https://dev.azure.com/%s/%s/_apis/build/builds", ORG, PROJECT);
    }

    public int getLatestBuildId(String branch, String pipeline_id) throws Exception {
        String url = String.format(BASE_URL+"?definitions=%s&branchName=%s&queryOrder=startTimeDescending&$top=1&api-version=7.1",
                pipeline_id, branch);

        JSONObject json = new JSONObject(sendGet(url));

        JSONArray value_jsonArray = json.getJSONArray("value");
        int build_id = 0;
        boolean found = false;
        for (Object value_obj : value_jsonArray) {
            JSONObject jsonObj = (JSONObject) value_obj;
            build_id = jsonObj.getInt("id");

            String get_Stage_url = String.format(BASE_URL+ "/%d/timeline?api-version=7.1",build_id);
            JSONObject get_Stage_json = new JSONObject(sendGet(get_Stage_url));
            JSONArray records_jsonArray = get_Stage_json.getJSONArray("records");

            for (Object record_obj : records_jsonArray) {
                JSONObject recordObj = (JSONObject) record_obj;
                if (recordObj.getString("type").equalsIgnoreCase("Stage") && recordObj.getString("name").startsWith("Build") && recordObj.getString("state").equalsIgnoreCase("completed") && recordObj.getString("result").equalsIgnoreCase("succeeded")) {
                    found = true;
                    break;
                }
            }
            if (found)
                break;
        }

        return build_id;
    }

    public String getArtifactDownloadUrl(int buildId, String artifactName) throws Exception {
        String url = String.format(BASE_URL+"/%d/artifacts?api-version=7.1",buildId);

        JSONObject json = new JSONObject(sendGet(url));
        JSONArray values = json.getJSONArray("value");
        for (int i = 0; i < values.length(); i++) {
            JSONObject artifact = values.getJSONObject(i);
            if (artifact.getString("name").equalsIgnoreCase(artifactName)) {
                return artifact.getJSONObject("resource").getString("downloadUrl");
            }
        }
        throw new RuntimeException("Artifact not found: " + artifactName);
    }

    public void downloadArtifact(String artifactUrl, String outputFile) throws Exception {
        System.out.println("⬇️  Downloading artifact from: " + artifactUrl);

        HttpURLConnection conn = (HttpURLConnection) new URL(artifactUrl + "&$format=zip").openConnection();
        conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((":" + PAT).getBytes()));

        try (InputStream in = conn.getInputStream();
             FileOutputStream out = new FileOutputStream(outputFile)) {
            in.transferTo(out);
        }
        System.out.println("✅ Downloaded to: " + Paths.get(outputFile).toAbsolutePath());
    }

    private String sendGet(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((":" + PAT).getBytes()));

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    public void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                String filePath = destDir + File.separator + entry.getName();

                if (entry.isDirectory()) {
                    File newDir = new File(filePath);
                    if (!newDir.exists()) newDir.mkdirs();
                } else {
                    File newFile = new File(filePath);
                    new File(newFile.getParent()).mkdirs();

                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile))) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zipIn.read(buffer)) > 0) {
                            bos.write(buffer, 0, len);
                        }
                    }
                }
                zipIn.closeEntry();
            }
        }
        new File(zipFilePath).deleteOnExit();
    }
}