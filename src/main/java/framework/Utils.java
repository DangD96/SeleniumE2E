package framework;

import org.openqa.selenium.By;

public class Utils {
    // To help with building locators via composition (parent-child elements in repeatable UI)
    public static By appendToXpath(By baseLocator, String relative) {
        if (!baseLocator.toString().startsWith("By.xpath: ")) {
            throw new IllegalArgumentException("This method only supports XPath By objects");
        }
        String xpathAsString = baseLocator.toString().replace("By.xpath: ", "");
        return By.xpath(xpathAsString + relative);
    }
}
