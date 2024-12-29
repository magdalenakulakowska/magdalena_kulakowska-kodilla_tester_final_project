package com.kodilla.pages.loginPage;

import com.kodilla.utilities.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.kodilla.utilities.Config.BASE_URL;
import static org.junit.jupiter.api.Assertions.*;

class LoginPageTestSuite extends BaseTest {

    @Test
    void testIfLoginButtonIsPrimaryAndSignUpButtonIsDefaultButton(){
        wait.until(ExpectedConditions.visibilityOf(loginPage.loginButton));
        String loginButtonClasses = loginPage.loginButton.getAttribute("class");
        String signUpButtonClasses = loginPage.registerButton.getAttribute("class");

        assertTrue(loginButtonClasses.contains("btn--primary"), "Login button should have the 'btn--primary' class.");
        assertTrue(signUpButtonClasses.contains("btn--default"), "SignUp button should have the 'btn--default' class.");
    }

    @Test
    void testLogin(){
        login("test_user", "test");
        wait.until(ExpectedConditions.urlToBe(BASE_URL));

        String currentUrl = driver.getCurrentUrl();
        assertEquals(currentUrl, BASE_URL, "User should be redirected to the titles list.");
    }

    @ParameterizedTest
    @CsvSource({
            "'', 'test'",
            "'test_user', ''",
            "'', ''"
    })
    void testLoginWithMissingLoginOrPasswordOrBothFields(String login, String password) {
        login(login, password);
        wait.until(ExpectedConditions.visibilityOf(loginPage.errorAlert));

        String errorMessage = loginPage.errorAlert.getText();
        String backgroundColor = loginPage.errorAlert.getCssValue("background-color");
        String textColor = loginPage.errorAlert.getCssValue("color");

        assertEquals(errorMessage, "You can't leave fields empty", "User should see error alert with proper text");
        assertEquals(backgroundColor, red, "Alert background color should be red");
        assertEquals(textColor, white, "Alert text color should be white");
    }

    @ParameterizedTest
    @CsvSource({
            "'test_user', 'test123'",
            "'test_user123', 'test'",
            "'test_user123', 'test123'"
    })
    void testLoginWithGivingIncorrectLoginOrPasswordOrBothFields(String login, String password) {
        login(login, password);
        wait.until(ExpectedConditions.visibilityOf(loginPage.errorAlert));

        String errorMessage = loginPage.errorAlert.getText();
        String backgroundColor = loginPage.errorAlert.getCssValue("background-color");
        String textColor = loginPage.errorAlert.getCssValue("color");

        assertEquals(errorMessage, "Login failed", "User should see error alert with proper text");
        assertEquals(backgroundColor, red, "Alert background color should be red");
        assertEquals(textColor, white, "Alert text color should be white");
    }

    @Test
    void testRedirectionToRegisterPage() {
        wait.until(ExpectedConditions.visibilityOf(loginPage.registerButton));
        loginPage.goToRegister();
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "register"));

        String currentUrl = driver.getCurrentUrl();
        assertEquals(currentUrl, BASE_URL + "register", "User should be redirected to the register page.");
    }

    @Test
    void userShouldBeLoggedOutAfterBrowserIsClosedAndOpenedAgain() {
        login("test_user", "test");
        driver.close();
        driver = new ChromeDriver();
        driver.navigate().to(BASE_URL);
        wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "login"));

        String currentUrl = driver.getCurrentUrl();
        assertEquals(currentUrl, BASE_URL + "login", "User should be redirected to the login page.");
    }
}