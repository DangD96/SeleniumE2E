package core;

import org.openqa.selenium.WebElement;

public class Assert extends org.testng.Assert {
    public static void elementIsVisible(WebElement element) {if (!element.isDisplayed()) fail();}

    public static void elementIsInvisible(WebElement element) {if(element.isDisplayed()) fail();}

    public static void elementIsClickable(WebElement element) {if(!element.isEnabled()) fail();}

    public static void elementIsNotClickable(WebElement element) {if(element.isEnabled()) fail();}
}
