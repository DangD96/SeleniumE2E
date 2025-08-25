package pages;

import framework.SparkDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class LoginPage {
    // region Locators
    public static By usernameField() {return By.id("username");}

    public static By passwordField() {return By.id("password");}

    public static By adminRadioBtn() {return By.cssSelector("input[value='admin']");}

    public static By userRadioBtn() {return By.cssSelector("input[value='user']");}

    public static By roleDropdown() {return By.cssSelector("select.form-control");}

    public static By tocCheckbox() {return By.id("terms");}

    public static By loginBtn() {return By.id("signInBtn");}

    public static By loginErrorMsg() {return By.cssSelector(".alert.alert-danger");}
    // endregion


    // region Performers
    public static void login(String username, String password, String userType, String userRole, boolean toggleTerms) {
        SparkDriver.typeText(usernameField(), username);
        SparkDriver.typeText(passwordField(), password);

        if (!userType.isEmpty()) selectUserType(userType);
        if (!userRole.isEmpty()) selectDropDownOption(userRole);
        if (toggleTerms) toggleTermsAndServices();

        SparkDriver.click(loginBtn());
    }

    public static void selectDropDownOption(String option) {
        Select roles = new Select(SparkDriver.getElement(roleDropdown()));
        roles.selectByVisibleText(option);
    }

    public static void selectUserType(String userType) {
        if (userType.equalsIgnoreCase("Admin")) {
            SparkDriver.click(adminRadioBtn());
        } else {
            SparkDriver.click(userRadioBtn());
        }
    }

    public static void clearUsername() {
        SparkDriver.clear(usernameField());
    }

    public static void clearPassword() {
        SparkDriver.clear(passwordField());
    }

    public static void toggleTermsAndServices() {
        SparkDriver.click(tocCheckbox());
    }
    // endregion
}
