package utils;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AutoSelectScenerioBDD {

    public static void main(String[] args) {
        UpdateUtils("@Regression1", ".\\src\\test\\resources\\FeatureFile\\inbound\\PFIB_Wholesale_Dup.feature",6);


        System.out.println("done");
    }

    public static void UpdateUtils(String TagName, String filePath, int matchingIndexPattern) {

        int Regression_Count = 0;
        try {
            String Current_Count= readproperty("Regression_Count");
            Regression_Count = Integer.parseInt(Current_Count)+1;
            String s=String.valueOf(Regression_Count);
            updateproprtywithvalue("Regression_Count",s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> ExecutedScenerio = new ArrayList<>();
        ArrayList<String> AllScenerio = new ArrayList<>();
        ArrayList<String> AllScenerioTag = new ArrayList<>();
        ArrayList<String> ExecutedTag = new ArrayList<>();

        ArrayList<String> AllModules = new ArrayList<>();
        ArrayList<String> FinalTestToExecute = new ArrayList<>();
        ArrayList<String> FinalTestToExecuteDuplicate = new ArrayList<>();

        String[] Order_Type = new String[0];
        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            String ModuleName = allLines.get(0);
            Order_Type = ModuleName.split("@");
            for (String Type : Order_Type) {
                if (!Type.isEmpty()) {
                    AllModules.add(Type);
                }
                else{
                    System.out.println("Please enter Order/Scenario type on every first line of feature File");
                }
            }
            for (String line : allLines) {
                if (line.contains("@")) {
                    AllScenerio.add(line);
                    if (line.contains(TagName)) {
                        ExecutedScenerio.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < ExecutedScenerio.size(); i++) {
            String[] allTag = ExecutedScenerio.get(i).split("@");
            for (String tag : allTag) {
                for(int m = 0; m < AllModules.size(); m++)
                {
                    if (tag.contains(AllModules.get(m).trim())) {
                        ExecutedTag.add(tag);

                    }
                }
            }
        }
        for (int i = 0; i < AllScenerio.size(); i++) {
            String[] allTag = AllScenerio.get(i).split("@");
            for (String tag : allTag) {
                for(int m = 0; m < AllModules.size(); m++)
                {
                    if (tag.contains(AllModules.get(m).trim())&&!tag.trim().equalsIgnoreCase(AllModules.get(m).trim())) {
                        AllScenerioTag.add(tag);

                    }
                }
            }
        }
        int count = 0;
        int nooftag=0;
        for (int l = 0; l < ExecutedTag.size(); l++) {
            String ShortOrderType = ExecutedTag.get(l).substring(0, matchingIndexPattern);

            for (int J = 0; J < AllScenerioTag.size(); J++) {
                if (AllScenerioTag.get(J).contains(ShortOrderType)) {
                    count=count+1;

                } else {

                }
            }
            int tagcount = Regression_Count % count;

            if(tagcount==0)
            {
                tagcount=count;
            }
            for (int J = 0; J < AllScenerioTag.size(); J++) {
                if (AllScenerioTag.get(J).contains(ShortOrderType)) {
                    nooftag=nooftag+1;

                    if (nooftag==tagcount) {
                        System.out.println(nooftag);
                        if (!FinalTestToExecuteDuplicate.contains(AllScenerioTag.get(J).trim())) {
                            FinalTestToExecuteDuplicate.add(AllScenerioTag.get(J).trim());
                            FinalTestToExecute.add(nooftag+"#"+ AllScenerioTag.get(J).trim());
                            break;
                        }
//                  else {
//                      if(count>nooftag) {
//                          int tegextended = nooftag+1;
//                          FinalTestToExecute.add( tegextended +"#" + AllScenerioTag.get(J + 1).toString().trim());
//                      }
//                  }
                    }
                }
            }
            count=0;
            nooftag=0;
        }
        System.out.println("Executed "+ ExecutedScenerio);
        System.out.println("Executed "+ ExecutedTag);
        System.out.println("All "+ AllScenerioTag);
        System.out.println("Final "+ FinalTestToExecute);


        modifyFile(filePath, TagName, "");

        for (int k=0;k<FinalTestToExecute.size();k++)
        {
            String[] fin= FinalTestToExecute.get(k).split("#");
            int noofTag =Integer.parseInt(fin[0]);
            String FinalTesttoExecute =fin[1];
            String ShortOrderType1 = FinalTesttoExecute.substring(0, matchingIndexPattern);

            modifyFile1(filePath, FinalTesttoExecute, FinalTesttoExecute +" "+TagName, ShortOrderType1, noofTag);
        }
    }
    public static void modifyFile(String filePath, String oldString, String newString) {
        File fileToBeModified = new File(filePath);

        String oldContent = "";

        BufferedReader reader = null;

        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldString with newString in the oldContent

            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent

            writer = new FileWriter(fileToBeModified);

            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources

                reader.close();

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void modifyFile1(String filePath, String oldString, String newString,String Matchtag, int Tagno) {
        File fileToBeModified = new File(filePath);

        String oldContent = "";

        BufferedReader reader = null;

        FileWriter writer = null;
        int count=0;
        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null) {
                if (line.contains(Matchtag)) {
                    count=count+1;
                    if(count==Tagno) {
                        oldContent = oldContent + line.replace(oldString, newString) + System.lineSeparator();
                    }
                    else{
                        oldContent = oldContent + line + System.lineSeparator();
                    }

                }
                else{
                    oldContent = oldContent + line + System.lineSeparator();
                }
                line = reader.readLine();
            }
            //Replacing oldString with newString in the oldContent
            String newContent = oldContent;
            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);

            writer.write(newContent);
            count=0;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources

                reader.close();

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void updateproprtywithvalue(String key,String value) throws IOException {
        FileInputStream in = new FileInputStream("staging_config.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();

        FileOutputStream out = new FileOutputStream("staging_config.properties");
        props.setProperty(key, value);
        props.store(out, null);
        out.close();
    }
    public static String readproperty(String key) throws IOException
    { String St=null;

        Properties pr= new Properties();
        InputStream file= new FileInputStream("staging_config.properties");
        pr.load(file);
        St=pr.getProperty(key);
        file.close();
        return St;

    }
}
