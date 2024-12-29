package com.kodilla.utilities;

import com.kodilla.pages.itemsPage.ItemsPage;
import com.kodilla.pages.loginPage.LoginPage;
import com.kodilla.pages.registerPage.SignUpPage;
import com.kodilla.pages.rentsPage.RentsPage;
import com.kodilla.pages.titlesPage.TitlesPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.kodilla.utilities.Config.BASE_URL;

public abstract class BaseTest {
    public TitlesPage titlesPage;
    public LoginPage loginPage;
    public SignUpPage signUpPage;
    public ItemsPage itemsPage;
    public RentsPage rentsPage;

    public WebDriver driver;
    public WebDriverWait wait;

    public String red = "rgba(255, 93, 108, 1)";
    public String green = "rgba(0, 188, 140, 1)";
    public String blue = "rgba(53, 152, 219, 1)";
    public String white = "rgba(255, 255, 255, 1)";

    @BeforeEach
    public void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.navigate().to(BASE_URL);
        loginPage = new LoginPage(driver);
        signUpPage = new SignUpPage(driver);
        titlesPage = new TitlesPage(driver);
        itemsPage = new ItemsPage(driver);
        rentsPage = new RentsPage(driver);
        wait = new WebDriverWait(driver, 10);
    }

    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login")));

        loginPage.login(username, password);
    }

    public void goToRegisterView() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("register-btn")));
        WebElement registerButton = driver.findElement(By.id("register-btn"));
        registerButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-repeat")));
    }

    public void goToRentsView(int titleId, int copyId){
        titlesPage.goToCopies(titleId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        itemsPage.goToRents(copyId);
    }

    public List<WebElement> getRentDates() {
        return driver.findElements(
                By.xpath("//*[contains(@class, 'rents-list__rent__rent-date') " +
                        "and not(contains(text(), 'expiration'))]"
                ));
    }

    public List<WebElement> getExpirationDates() {
        return driver.findElements(
                By.xpath("//*[contains(@class, 'rents-list__rent__rent-date') " +
                        "and (contains(text(), 'expiration'))]"
                ));
    }

    @AfterEach
    public void testDown() {
        driver.close();
    }
}
