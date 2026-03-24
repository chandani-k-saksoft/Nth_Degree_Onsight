package utils;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;

public class Slackintegration {
    //    private static String urlSlackWebHook = "https://hooks.slack.com/services/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";
//    private static String channelName = "qareport";
//    private static String botUserOAuthAccessToken = "xoxb-1784431520051-1784470950419-X93OadnJgJny90wenihLeeO6";
    private static final String urlSlackWebHook = "https://hooks.slack.com/services/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";
    private static final String channelName = "qa-report";
    private static final String botUserOAuthAccessToken = "xoxb-3353652950228-3351315187826-BAxdBmAEHsCijQfd4LY4JR9u";
    public void sendTestExecutionStatusToSlack(String message) throws Exception {
        try {
            Payload payload = Payload.builder().channel(channelName).text(message).build();

            WebhookResponse webhookResponse = Slack.getInstance().send(urlSlackWebHook, payload);
            webhookResponse.getMessage();
        } catch (IOException e) {
            System.out.println("Unexpected Error! WebHook:" + urlSlackWebHook);
        }
    }
    public void sendTestExecutionReportToSlack(String testReportPath) throws Exception {
       // String url = "https://slack.com/api/files.upload?token=" + botUserOAuthAccessToken + "&channels=" + channelName
        //        + "";
        String url = "https://slack.com/api/files.upload";
        try {
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpPost httppost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(new File(testReportPath));
            //builder.addPart("content", fileBody);
            builder.addTextBody("token",botUserOAuthAccessToken);
            builder.addTextBody("channels",channelName);
            builder.addBinaryBody("file",new File(testReportPath));
            builder.addBinaryBody("content",new File(testReportPath));
            httppost.setEntity(builder.build());
            HttpResponse response = null;
            response = httpclient.execute(httppost);
            HttpEntity result = response.getEntity();
            System.out.println("Test"+result.getContent().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Slackintegration sl=new Slackintegration();
        //sl.sendTestExecutionStatusToSlack("Test Message");
        sl.sendTestExecutionReportToSlack("D:\\ImportantData\\Skype_Files\\RestAssuredTest\\poa-da\\ExtentReports\\ExtentReportResults.html");
    }
}
