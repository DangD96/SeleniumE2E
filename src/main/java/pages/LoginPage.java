package pages;

import framework.BaseDriver;
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
        BaseDriver.typeText(usernameField(), username);
        BaseDriver.typeText(passwordField(), password);

        if (!userType.isEmpty()) selectUserType(userType);
        if (!userRole.isEmpty()) selectDropDownOption(userRole);
        if (toggleTerms) toggleTermsAndServices();

        BaseDriver.click(loginBtn());
    }

    public static void selectDropDownOption(String option) {
        Select roles = new Select(BaseDriver.getElement(roleDropdown()));
        roles.selectByVisibleText(option);
    }

    public static void selectUserType(String userType) {
        if (userType.equalsIgnoreCase("Admin")) {
            BaseDriver.click(adminRadioBtn());
        } else {
            BaseDriver.click(userRadioBtn());
        }
    }

    public static void clearUsername() {
        BaseDriver.clear(usernameField());
    }

    public static void clearPassword() {
        BaseDriver.clear(passwordField());
    }

    public static void toggleTermsAndServices() {
        BaseDriver.click(tocCheckbox());
    }
    // endregion
}
