package utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //on class level
public @interface TestInfo {

    String testCaseId() default "NA";
    String testCaseDescription() default "NA";
    String US_ID() default "NA";
    String US_Des() default "NA";

}
