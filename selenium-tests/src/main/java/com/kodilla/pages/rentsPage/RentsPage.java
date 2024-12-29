package com.kodilla.pages.rentsPage;

import com.kodilla.utilities.AbstractPom;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.util.List;

import static com.kodilla.utilities.DatePickerUtils.*;

public class RentsPage extends AbstractPom {

    @FindBy(xpath = "//*[starts-with(@id, 'rent-')]")
    List<WebElement> rents;

    @FindBy(className = ("alert--info"))
    WebElement infoAlert;

    @FindBy(className = ("alert--error"))
    WebElement errorAlert;

    @FindBy(name = "return-button")
    WebElement returnButton;

    @FindBy(name = "add-rent-button")
    WebElement rentButton;

    @FindBy(className = "rent-form")
    WebElement rentDialog;

    @FindBy(name = "customer-name")
    WebElement customerNameField;

    @FindBy(name = "rent-date")
    WebElement rentDateField;

    @FindBy(name = "expiration-date")
    WebElement expirationDateField;

    @FindBy(name = "submit-button")
    WebElement dialogSubmitButton;

    @FindBy(className = "day__month_btn")
    WebElement monthChangeButton;

    @FindBy(className = "month__year_btn")
    WebElement yearChangeButton;

    @FindBy(className = "day")
    List<WebElement> dayCells;

    @FindBy(className = "month")
    List<WebElement> monthCells;

    @FindBy(className = "year")
    List<WebElement> yearCells;

    @FindBy(className = "edit-btn")
    List<WebElement> editButtons;

    @FindBy(className = "remove-btn")
    List<WebElement> removeButtons;

    public RentsPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean areRentsVisible() {
        return !rents.isEmpty();
    }

    public void goToCopies() {
        returnButton.click();
    }

    public int getRentsCount() {
        return rents.size();
    }

    public void addRent(String customerName) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        rentButton.click();

        wait.until(ExpectedConditions.visibilityOf(rentDialog));

        String actualYear = String.valueOf(LocalDate.now().getYear());
        String actualMonth = String.valueOf(LocalDate.now().getMonth());
        String actualDay = String.valueOf(LocalDate.now().getDayOfMonth());

        customerNameField.sendKeys(customerName);
        rentDateField.click();
        wait.until(ExpectedConditions.visibilityOf(monthChangeButton)).click();
        wait.until(ExpectedConditions.visibilityOf(yearChangeButton)).click();
        clickSelectedCell(yearCells, actualYear);
        clickSelectedCell(monthCells, actualMonth);
        clickSelectedCell(dayCells, actualDay);
        dialogSubmitButton.click();
    }

    public void editRent(int id, String customerName) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        editButtons.get(id).click();

        wait.until(ExpectedConditions.visibilityOf(rentDialog));

        String newDate = getNextDay( rentDateField.getAttribute("value"));
        LocalDate parsedDate = getDateFromString(newDate);

        String selectedYear = String.valueOf(parsedDate.getYear());
        String selectedMonth = parsedDate.getMonth().toString();
        String selectedDay = String.valueOf(parsedDate.getDayOfMonth());

        customerNameField.sendKeys(Keys.CONTROL + "a");
        customerNameField.sendKeys(Keys.DELETE);
        customerNameField.sendKeys(customerName);
        rentDateField.click();
        wait.until(ExpectedConditions.visibilityOf(monthChangeButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(yearChangeButton)).click();
        clickSelectedCell(yearCells, selectedYear);
        clickSelectedCell(monthCells, selectedMonth);
        clickSelectedCell(dayCells, selectedDay);
        expirationDateField.click();
        clickSelectedCell(yearCells, selectedYear);
        clickSelectedCell(monthCells, selectedMonth);
        clickSelectedCell(dayCells, selectedDay);
        dialogSubmitButton.click();
    }

    public void removeRent(int id) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement buttonToRemove = removeButtons.get(id);
        buttonToRemove.click();

            wait.until(ExpectedConditions.stalenessOf(buttonToRemove));
    }
}
