package utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;

public class AppiumSessionManager {

    static AppiumDriverLocalService service;

    public static void startAppiumSession(String appium_url) {
        String ip = appium_url.split(":")[1].replace("//", "");
        int port = Integer.parseInt(appium_url.split(":")[2]);
        deleteActiveAppiumSession(appium_url);
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress(ip);
        builder.withTimeout(Duration.ofMinutes(5));
        builder.usingPort(port);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
    }

    public static void stopAppiumSession()
    {
        if (service != null)
            service.stop();
    }

    private static void deleteActiveAppiumSession(String appium_url) {
        try {
            URL url = new URL(appium_url + "/sessions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            System.out.println("Active Sessions: " + response);

            if (response.contains("sessionId")) {
                String sessionId = response.split("\"sessionId\":\"")[1].split("\"")[0]; // Extract first session ID

                URL deleteUrl = new URL(appium_url + "/session/" + sessionId);
                HttpURLConnection deleteConn = (HttpURLConnection) deleteUrl.openConnection();
                deleteConn.setRequestMethod("DELETE");
                deleteConn.setDoOutput(true);

                OutputStream os = deleteConn.getOutputStream();
                os.flush();
                os.close();

                System.out.println("Killed session: " + sessionId);
            } else {
                System.out.println("No active Appium sessions found.");
            }

        } catch (Exception ignored) {

        }
    }

}
