//package utils;
//
//import com.slack.api.Slack;
//import com.slack.api.methods.SlackApiException;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//
//public class PublishingMessage {
//
//    /**
//     * Post a message to a channel your app is in using ID and message text
//     */
//    private static String urlSlackWebHook = "https://hooks.slack.com/services/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";
//    private static String channelName = "qa-report";
//    private static String botUserOAuthAccessToken = "xoxb-3353652950228-3351315187826-BAxdBmAEHsCijQfd4LY4JR9u";
//
//    static void publishMessage(String id, String text) {
//        // you can get this instance via ctx.client() in a Bolt app
//        var client = Slack.getInstance().methods();
//        var logger = LoggerFactory.getLogger("QA Automation");
//        try {
//            // Call the chat.postMessage method using the built-in WebClient
//            var result = client.chatPostMessage(r -> r
//                            // The token you used to initialize your app
//                            .token(botUserOAuthAccessToken)
//                            .channel(id)
//                            .text(text)
//                    // You could also use a blocks[] array to send richer content
//            );
//            // Print result, which includes information about the message (like TS)
//            logger.info("result {}", result);
//        } catch (IOException | SlackApiException e) {
//            logger.error("error: {}", e.getMessage(), e);
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        publishMessage("C03B11TGELQ", "Hello world :tada:");
//    }
//
//}