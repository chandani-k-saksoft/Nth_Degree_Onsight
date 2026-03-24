package utils;

import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ReadAnnotation {

    public  String GetAnnotation(Method method, String Description) throws Exception {

        String TCID ="";
        String TCDes ="";
        String USID ="";
        String USDes ="";



            if (method.isAnnotationPresent(TestInfo.class)) {

                Annotation annotation = method.getAnnotation(TestInfo.class);
                TestInfo test = (TestInfo) annotation;

                TCID= test.testCaseId();
                TCDes =Description;
                USDes =test.US_Des();
                USID= test.US_ID();
                System.out.println(test.testCaseId());
                System.out.println(test.testCaseDescription());
                System.out.println(test.US_Des());
                System.out.println(test.US_ID());

            }

        return  USID + "@"+ USDes +"@" +TCID  +"@"+TCDes;
    }
}
