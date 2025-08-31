package framework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Helpers {
    /** Helps with building locators via composition (parent/child elements in repeatable UI) */
    public static By extendXPath(By baseLocator, String relative) {
        if (!baseLocator.toString().startsWith("By.xpath: ")) {
            throw new IllegalArgumentException("This method only supports XPath By objects");
        }
        String xpathAsString = baseLocator.toString().replace("By.xpath: ", "");
        return By.xpath(xpathAsString + relative);
    }

    /** Return as ArrayList because SparkTest.getTestData will need to retrieve data from it and that is faster compared to LinkedList */
    public static ArrayList<HashMap<String, String>> deserializeJSON(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), new TypeReference<>(){});
    }

    /** Given absolute path, return the relative path FROM the user directory */
    public static String getPathRelativeToUserDir(String absolutePath) {
        String OS = System.getProperty("os.name");
        String[] results;

        if (OS.contains("Windows")) {
            // Backslash is a special character in both regex AND string literals -> double escape so resulting string evalutates as "\\"
            // i.e. match on a literal single backslash
            results = SparkTest.USER_DIR.split("\\\\"); // Split on "\"
        } else {
            results = SparkTest.USER_DIR.split("/");   // Split on "/"
        }

        String userDirAKAProjectName = results[results.length-1];
        return absolutePath.split(userDirAKAProjectName)[1]; // Get everything that comes after the project name
    }

    public static boolean isPRD() {
        return "PRD".equals(SparkTest.env);
    }
}
