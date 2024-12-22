package core;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ISuiteListener {
    public static String suiteName;

    @Override
    public void onStart(ISuite suite) {
        suiteName = suite.getName(); // Suite name is always required in XML file
    }
}
