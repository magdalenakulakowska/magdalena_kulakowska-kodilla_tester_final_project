package com.kodilla.pages.loginPage;

import com.kodilla.utilities.AbstractPom;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage  extends AbstractPom {
    @FindBy(id = "login")
    WebElement loginField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "login-btn")
    WebElement loginButton;

    @FindBy(id = "register-btn")
    WebElement registerButton;

    @FindBy(className = "alert--error")
    WebElement errorAlert;

    public LoginPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void login(String login, String password) {
        loginField.sendKeys(login);
        passwordField.sendKeys(password);
        loginButton.click();
    }

    public void goToRegister() {
        registerButton.click();
    }

    public WebElement setLoginField(WebElement element) {
        loginField = element;
        return loginField;
    }

    public WebElement setPasswordField(WebElement element) {
        passwordField = element;
        return passwordField;
    }

    public WebElement setLoginButton(WebElement element) {
        loginButton = element;
        return loginButton;
    }
}
