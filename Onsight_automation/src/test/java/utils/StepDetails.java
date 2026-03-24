package utils;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

/**
 * @author manish.m
 */
public class StepDetails implements ConcurrentEventListener {
    public static String stepName;
    public static String ClassName;
    public static String FeatureFile;
    public EventHandler<TestStepStarted> stepHandler = new EventHandler<TestStepStarted>() {
        @Override
        public void receive(TestStepStarted event) {
            handleTestStepStarted(event);
        }
        };
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, stepHandler);
//        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
    }
    private void handleTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep testStep) {
            stepName = testStep.getStep().getText();
//             ClassName = testStep.getCodeLocation().split("\\.")[2];
             ClassName=testStep.getCodeLocation().split("\\(")[0];
//             ExtentCucumberAdapter.addTestStepLog(testStep.getCodeLocation().split("\\(")[0]);
             FeatureFile = testStep.getUri().toString();
        }
    }
}

