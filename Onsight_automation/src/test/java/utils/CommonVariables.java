package utils;

import pageHelper.BDDDriver;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.spec.IvParameterSpec;

public class CommonVariables {

    public static Map<String, String> commonVariables = new ConcurrentHashMap<>();

    private static String encrypt(String password) throws Exception {
        String key = "myEncryptionKey";
        key = String.format("%-16s", key).substring(0, 16);
        SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "AES");

        byte[] iv = new byte[16];
        java.security.SecureRandom random = new java.security.SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encryptedPassword = cipher.doFinal(password.getBytes());

        String encryptedString = Base64.getEncoder().encodeToString(encryptedPassword);
        String ivString = Base64.getEncoder().encodeToString(iv);

        return ivString + ":" + encryptedString; // Concatenate IV and encrypted password
    }

    public static String decrypt(String encryptedText) throws Exception {
        String key = "myEncryptionKey";
        key = String.format("%-16s", key).substring(0, 16);
        SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "AES");

        String[] parts = encryptedText.split(":");
        String iv = parts[0];
        String encryptedPassword = parts[1];

        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
        byte[] encryptedPasswordBytes = Base64.getDecoder().decode(encryptedPassword);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decryptedPassword = cipher.doFinal(encryptedPasswordBytes);

        return new String(decryptedPassword);
    }

    public static void writeUsersFiles(String userType, String email) {
        try (BufferedWriter appendWriter = new BufferedWriter(new FileWriter("signup_user.txt", true))) {
            appendWriter.write(userType + ":" + email + "\n");
        } catch (Exception ignored) {
        }
    }

    public static void writeFile(String filePath, String text) {
        try (BufferedWriter appendWriter = new BufferedWriter(new FileWriter(filePath, true))) {
            appendWriter.write(text);
        } catch (Exception ignored) {
        }
    }

    public static String readUsersFile(String userType, String platformType) throws Exception {
        List<String> users = new ArrayList<>();
        String env = new PropertyReader().readProperty("env");
        try (BufferedReader reader = new BufferedReader(new FileReader("signup_user.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(userType))
                    if (line.split(":")[1].contains(env) && line.split(":")[1].contains("+" + platformType))
                        users.add(line.split(":")[1]);
            }
        } catch (Exception ignored) {
        }
        int num = Math.max(1, users.size() - 1);
        if (users.isEmpty())
            return null;
        else
            return users.getLast();
    }

    public static String readFile(String filePath) throws Exception {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (Exception ignored) {
        }
        return data.toString();
    }

    public String getRandomNumber() {
        if (BDDDriver.getRandomValue() == null) {
            String date = new SimpleDateFormat("HHmmssddMMyyS", Locale.ENGLISH).format(new Date());
            String substring = date.substring(0, date.length() - 2);
            if (substring.length() == 12)
                substring = substring + "0";
            BDDDriver.setRandomValue(substring);
//            BDDDriver.setRandomValue(new Random().nextInt(9999999 - 1000000) + 1000000);
        }
        return BDDDriver.getRandomValue();
    }

    public String convertAndUpdateDateTime(String format, String type, int number) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime modified = now;

        switch (type.toUpperCase()) {
            case "M":
                modified = now.plusMonths(number);
                break;
            case "D":
                modified = now.plusDays(number);
                break;
            case "H":
                modified = now.plusHours(number);
                break;
            default:
                break;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return modified.format(formatter);
    }

    public String getDate(String format, String startDate) {
        int number = 0;
        String type = "D";
        if (!startDate.equalsIgnoreCase("CURRENT")) {
            number = Integer.parseInt(startDate.split(" ")[0].trim());
            type = startDate.split(" ")[1];
        }

        return convertAndUpdateDateTime(format, type, number);
    }

    public String updateDate(String inputDate, String inputFormat, int day, String outputFormat) throws ParseException {
        SimpleDateFormat inputSDF = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        SimpleDateFormat outputSDF = new SimpleDateFormat(outputFormat, Locale.ENGLISH);

        Date date = inputSDF.parse(inputDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, day);

        return outputSDF.format(cal.getTime());
    }

    public int daysDifference(int y1, int m1, int d1, int y2, int m2, int d2) {
        LocalDate date1 = LocalDate.of(y1, m1, d1);
        LocalDate date2 = LocalDate.of(y2, m2, d2);

        // Calculate difference
        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
        return Math.toIntExact(daysBetween);
    }

    public static void main(String... as) throws Exception {
        System.out.println(decrypt("YRVff9HRArqTDnaNgnW5WA==:TpQCz2qeoMIApyH1XSXROA=="));
    }
}