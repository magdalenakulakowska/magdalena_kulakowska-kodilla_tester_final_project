package com.kodilla.pages.registerPage;

import com.kodilla.utilities.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.kodilla.utilities.Config.BASE_URL;
import static org.junit.jupiter.api.Assertions.*;

class SignUpPageTestSuite extends BaseTest {

    @Test
    void testIfSignUpButtonIsPrimaryAndLoginButtonIsDefaultButton(){
        goToRegisterView();
        String signUpButtonClasses = signUpPage.registerButton.getAttribute("class");
        String loginButtonClasses = signUpPage.loginButton.getAttribute("class");

        assertTrue(signUpButtonClasses.contains("btn--primary"), "SignUp button should have the 'btn--primary' class.");
        assertTrue(loginButtonClasses.contains("btn--default"), "Login button should have the 'btn--default' class.");
    }

    @Test
    void testRegister(){
        goToRegisterView();
        String login = signUpPage.generateRandomUsername(8);
        String password = signUpPage.generateRandomPassword(12);

        signUpPage.register(login, password, password);
        wait.until(ExpectedConditions.visibilityOf(signUpPage.successAlert));

        String successAlertMessage = signUpPage.successAlert.getText();
        String backgroundColor = signUpPage.successAlert.getCssValue("background-color");
        String textColor = signUpPage.successAlert.getCssValue("color");

        assertEquals(successAlertMessage, "You have been successfully registered!", "Success alert message should have correct text.");
        assertEquals(backgroundColor, green, "Alert background color should be green");
        assertEquals(textColor, white, "Alert text color should be white");
    }

    @Test
    void testLoginAfterSuccessfullRegister() {
        goToRegisterView();
        String login = signUpPage.generateRandomUsername(8);
        String password = signUpPage.generateRandomPassword(12);

        signUpPage.register(login, password, password);
        wait.until(ExpectedConditions.visibilityOf(signUpPage.successAlert));
        signUpPage.goToLogin();
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "login"));

        try {
            signUpPage.loginField = driver.findElement(By.id("login"));
            signUpPage.passwordField = driver.findElement(By.id("password"));
            signUpPage.loginButton = driver.findElement(By.id("login-btn"));

            loginPage.login(login, password);
        } catch (StaleElementReferenceException e) {
            signUpPage.loginField = driver.findElement(By.id("login"));
            signUpPage.passwordField = driver.findElement(By.id("password"));
            signUpPage.loginButton = driver.findElement(By.id("login-btn"));

            loginPage.login(login, password);
        }

        wait.until(ExpectedConditions.urlToBe(BASE_URL));
        String currentUrl = driver.getCurrentUrl();
        assertEquals(BASE_URL, currentUrl, "User should be redirected to the titles list.");
    }

    @ParameterizedTest
    @CsvSource({
            "'', 'test', 'test'",
            "'test_user', '', 'test'",
            "'test_user', 'test', ''",
            "'', '', ''"
    })
    void testSignUpWithMissingLoginOrPasswordOrRepeatedPasswordOrAllFields(String login, String password, String repeatedPassword) {
        goToRegisterView();
        signUpPage.register(login, password, repeatedPassword);
        wait.until(ExpectedConditions.visibilityOf(signUpPage.errorAlert));

        String errorMessage = signUpPage.errorAlert.getText();
        String backgroundColor = signUpPage.errorAlert.getCssValue("background-color");
        String textColor = signUpPage.errorAlert.getCssValue("color");

        assertEquals(errorMessage, "You can't leave fields empty", "User should see error alert with proper text");
        assertEquals(backgroundColor, red, "Alert background color should be red");
        assertEquals(textColor, white, "Alert text color should be white");
    }

    @Test
    void testSignUpWithExistingLogin() {
        goToRegisterView();
        signUpPage.register("test_user", "test", "test");
        wait.until(ExpectedConditions.visibilityOf(signUpPage.errorAlert));

        String errorMessage = signUpPage.errorAlert.getText();
        String backgroundColor = signUpPage.errorAlert.getCssValue("background-color");
        String textColor = signUpPage.errorAlert.getCssValue("color");

        assertEquals(errorMessage, "This user already exist!", "User should see error alert with proper text");
        assertEquals(backgroundColor, red, "Alert background color should be red");
        assertEquals(textColor, white, "Alert text color should be white");
    }

    @Test
    void testIfLoginButtonRedirectsToLoginPage(){
        goToRegisterView();
        signUpPage.goToLogin();
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "login"));

        String currentUrl = driver.getCurrentUrl();
        assertEquals(currentUrl, BASE_URL + "login", "User should be redirected to the login page.");
    }
}