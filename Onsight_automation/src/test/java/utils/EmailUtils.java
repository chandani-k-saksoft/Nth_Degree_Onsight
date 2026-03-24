package utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.client.json.JsonFactory;
import io.restassured.path.json.JsonPath;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.lang.Thread;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.*;

public class EmailUtils {
    public static final String USER_ID = "ckgupta438@gmail.com";

    private static final String PASS = "mbvj utky kzlo cltg";
    public static Gmail service = null;
    int retryIndex = 5;

    public static void main(String... abc) throws Exception {
        System.out.println("demo");
        System.out.println(EmailUtils.getEmail("Nth Degree Project Update for Saksoft at Saksoft Test Show 2026"));
    }

    public static String getEmail(String subject) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        Session session = Session.getDefaultInstance(properties);

        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", USER_ID, PASS);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);


        Message[] messages = //inbox.getMessages();
        inbox.search(
                new FlagTerm(new Flags(Flags.Flag.SEEN), false)
        );
//        System.out.println("Total Messages: " + messages.length);
        System.out.println("Reading mail details...");
        Optional<Message> message = Arrays.stream(messages).filter(X -> {
            try {
                return X.getSubject().equalsIgnoreCase("Nth Degree Project Update for Saksoft at Saksoft Test Show 2026");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }).findFirst();
        if (message.isPresent())
            return extractText(message.get());
        return  "NO_MAIL_FOUND";
    }

    public static String extractText(Message message) throws Exception {

        Object content = message.getContent();

        if (content instanceof MimeMultipart) {
            return extractFromMultipart((MimeMultipart) content);
        }

        if (content instanceof String) {
            return Jsoup.parse(content.toString()).text();
        }

        return "";
    }

    private static String extractFromMultipart(MimeMultipart multipart) throws Exception {

        for (int i = 0; i < multipart.getCount(); i++) {

            BodyPart bodyPart = multipart.getBodyPart(i);

            Object partContent = bodyPart.getContent();

            if (partContent instanceof MimeMultipart) {
                return extractFromMultipart((MimeMultipart) partContent);
            }

            if (partContent instanceof String) {
                String html = partContent.toString();
                return Jsoup.parse(html).text();   // THIS removes all tags
            }
        }

        return "";
    }


}