package framework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Utilities {
    // To help with building locators via composition (parent-child elements in repeatable UI)
    public static By appendToXpath(By baseLocator, String relative) {
        if (!baseLocator.toString().startsWith("By.xpath: ")) {
            throw new IllegalArgumentException("This method only supports XPath By objects");
        }
        String xpathAsString = baseLocator.toString().replace("By.xpath: ", "");
        return By.xpath(xpathAsString + relative);
    }

    public static ArrayList<HashMap<String, String>> deserializeJSON(String path) throws IOException {
        /* Use Jackson API to convert JSON objects to HashMaps.
        Return as ArrayList because getTestData will need to retrieve data from it
        and that is faster compared to LinkedList */
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), new TypeReference<>(){});
    }

    /** Given absolute path, return the relative path with user directory removed */
    public static String getPathRelativeToUserDir(String absolutePath) {
        String OS = System.getProperty("os.name");
        String[] results;

        if (OS.contains("Windows")) {
            // Because backslash is a special character in both regex AND string literals,
            // need to double escape so the resulting string evalutates as "\\"
            // i.e. match on a literal single backslash
            results = SparkTest.USER_DIR.split("\\\\"); // Split on "\"
        } else {
            results = SparkTest.USER_DIR.split("/");   // Split on "/"
        }

        String userDirAKAProjectName = results[results.length-1];
        return absolutePath.split(userDirAKAProjectName)[1]; // Get everything that comes after the project name
    }
}
