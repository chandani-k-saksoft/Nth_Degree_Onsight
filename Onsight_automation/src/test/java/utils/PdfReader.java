package utils;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.testng.Assert;

public class PdfReader {

    String Filename;
    static Document document;
    static PdfPTable table;
    static PdfPCell cell;

    static Phrase ph;
    static PdfWriter PdfWriter;
    static Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);

    public static void onStartTest()
    {
        //pdf Document
        document = new Document(PageSize.A2, 10, 20, 10, 10);
        // creating table and set the column width

        table= new PdfPTable(8);
        float[] widths = { 3, 3,3, 3,4,3,4,3 };
        try {
            table.setWidths(widths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.setSpacingBefore(40);

        table.setHeaderRows(1);
        // add cell of table - header cell
        cell = new PdfPCell(new Phrase("Scenario name"));
        cell.setBackgroundColor(new BaseColor(0, 173, 239));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Status"));
        cell.setBackgroundColor(new BaseColor(0, 173, 239));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Total Test"));
        cell.setBackgroundColor(new BaseColor(0, 173, 239));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Time Taken(in sec)"));
        cell.setBackgroundColor(new BaseColor(0, 173, 239));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Failed Steps"));
        cell.setBackgroundColor(new BaseColor(0, 173, 239));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Failed on"));
        cell.setBackgroundColor(new BaseColor(0, 173, 239));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Failed Reason"));
        cell.setBackgroundColor(new BaseColor(0, 173, 239));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Defect Status"));
        cell.setBackgroundColor(new BaseColor(0, 173, 239));
        table.addCell(cell);
    }

    public static void onFinishTest() {
        //Pdf Document
        try {
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(".//PdfReport.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
        Image image1 = null;
        try {
            image1 = Image.getInstance(".//360logica.jpg");
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image1.setAlignment(Element.PARAGRAPH);
        image1.scaleAbsolute(60, 60);
//Add to document
        try {
            document.add(image1);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        Font boldFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD);
        try {

            Paragraph preface = new Paragraph("UNITE - One View Report",boldFont1);
            preface.setSpacingBefore(12f);
            preface.setAlignment(Element.ALIGN_CENTER);
            document.add(preface);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {

            Paragraph preface = new Paragraph("360 Logica offers one view report to our Customer to Review Quality of Application Quickly to higher management! ");
            preface.setSpacingBefore(12f);
            preface.setAlignment(Element.ALIGN_LEFT);
            preface.setSpacingBefore(20f);
            document.add(preface);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //Add Graph
        try {
//            ph = new Phrase("");
            Paragraph ph=new Paragraph("");
            ph.setSpacingBefore(12f);
            ph.setAlignment(Element.ALIGN_RIGHT);
            ph.setSpacingBefore(20f);
            Chunk chunk = new Chunk("View on Graph", boldFont);
            chunk.setAnchor(".//Graph.png");
            chunk.setBackground(new BaseColor(173,227,54));
            ph.add(chunk);
            document.add(ph);
        }
        catch (DocumentException e)
        {

        }

        try {

            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        System.out.println("Successfull.");
    }

    public static void onSuccessTest(String Description )
    {


        String[] parts = Description.split("@");
        cell = new PdfPCell();
        cell.setPaddingTop(20);
        cell.setMinimumHeight(40f);
        ph = new Phrase(parts[0]);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase("Passed");
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase(parts[1]);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase(parts[2]);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase("");
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase("");
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase("");
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase("");
        cell.addElement(ph);
        table.addCell(cell);

    }

    public static void onFailedTest(String Description, String Message,  String Defects ) throws IOException {
        String[] parts = Description.split("@");
        cell = new PdfPCell();
        cell.setMinimumHeight(25f);
        ph = new Phrase(parts[0]);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(255, 0, 0));
        ph = new Phrase("");
        Chunk chunk = new Chunk("Failed", boldFont);
        chunk.setAnchor(".//Reports//Spark.html");
        ph.add(chunk);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase(parts[1]);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase(parts[2]);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase(parts[3]);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase(parts[4] + " Page");
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        ph = new Phrase(Message);
        cell.addElement(ph);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(210, 135, 184));
//        ph = new Phrase(DataReader.ReadDefectID(parts[3]),boldFont);
        ph = new Phrase(Defects,boldFont);

        cell.addElement(ph);
        table.addCell(cell);
    }

    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }
    public static PdfPCell createTextCell(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }


        public void CheckInternetConnection() {
            try {
                URL url = new URL("http://www.google.com");
                URLConnection connection = url.openConnection();
                connection.connect();
                System.out.println("Internet is connected, Proceeding with Test Execution!  ");
            } catch (MalformedURLException e) {
                System.out.println("Internet is not connected");

                Assert.assertEquals("Internet is not Connected! ", "Internet Connection should Connect to Proceed with Test Execution");
            } catch (IOException e) {
                System.out.println("Internet is not connected");
                Assert.assertEquals("Internet is not Connected! ", "Internet Connection should Connect to Proceed with Test Execution");

            }
        }



}
