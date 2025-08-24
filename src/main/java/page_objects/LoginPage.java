package page_objects;

import framework.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // region Locators
    public By UsernameField() {return By.id("username");}

    public By PasswordField() {return By.id("password");}

    public By AdminRadioBtn() {return By.cssSelector("input[value='admin']");}

    public By UserRadioBtn() {return By.cssSelector("input[value='user']");}

    public By RoleDropdown() {return By.cssSelector("select.form-control");}

    public By TocCheckbox() {return By.id("terms");}

    public By LoginBtn() {return By.id("signInBtn");}

    public By LoginErrorMsg() {return By.cssSelector(".alert.alert-danger");}
    // endregion


    // region Performers
    public CatalogPage Login(String username, String password, String userType, String userRole, boolean toggleTerms) {
        TypeText(UsernameField(), username);
        TypeText(PasswordField(), password);

        if (!userType.isEmpty()) SelectUserType(userType);
        if (!userRole.isEmpty()) SelectDropDownOption(userRole);
        if (toggleTerms) ToggleTermsAndServices();

        Click(LoginBtn());
        return new CatalogPage(driver);
    }

    public void SelectDropDownOption(String option) {
        Select roles = new Select(GetElement(RoleDropdown()));
        roles.selectByVisibleText(option);
    }

    public void SelectUserType(String userType) {
        if (userType.equalsIgnoreCase("Admin")) {
            Click(AdminRadioBtn());
        } else {
            Click(UserRadioBtn());
        }
    }

    public void ClearUsername() {
        Clear(UsernameField()); //js.executeScript("arguments[0].value = ''", GetElement(UsernameField));
    }

    public void ClearPassword() {
        Clear(PasswordField());
    }

    public void ToggleTermsAndServices() {
        Click(TocCheckbox());
    }
    // endregion
}
