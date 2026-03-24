package utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;

public class OCRUtil {

    private static final ITesseract tesseract;

    static {
        tesseract = new Tesseract();
        tesseract.setDatapath(String.format("%s/tessdata", System.getProperty("user.dir")));
        tesseract.setLanguage("eng");
    }


    public static String getTextFromImage(String base64) {
        try {
            byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64);
            return tesseract.doOCR(ImageIO.read(new ByteArrayInputStream(imageBytes))).replaceAll("\\nB\\n", "").replace("/", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
