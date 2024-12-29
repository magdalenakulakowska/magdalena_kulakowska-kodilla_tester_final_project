package com.kodilla.pages.registerPage;

import com.kodilla.utilities.AbstractPom;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Random;

public class SignUpPage extends AbstractPom {
    @FindBy(id = "login")
    WebElement loginField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "password-repeat")
    WebElement repeatPasswordField;

    @FindBy(id = "login-btn")
    WebElement loginButton;

    @FindBy(id = "register-btn")
    WebElement registerButton;

    @FindBy(className = "alert--error")
    WebElement errorAlert;

    @FindBy(className = "alert--success")
    WebElement successAlert;

    public SignUpPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void register(String login, String password, String repeatPassword) {
        loginField.sendKeys(login);
        passwordField.sendKeys(password);
        repeatPasswordField.sendKeys(repeatPassword);
        registerButton.click();
    }

    public void goToLogin() {
        loginButton.click();
    }

    public String generateRandomUsername(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder username = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            username.append(characters.charAt(index));
        }

        return username.toString();
    }

    public String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=<>?";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
}
