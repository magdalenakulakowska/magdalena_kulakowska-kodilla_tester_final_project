package com.kodilla.pages.itemsPage;

import com.kodilla.utilities.AbstractPom;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.util.List;

import static com.kodilla.utilities.DatePickerUtils.*;
import static com.kodilla.utilities.WebElementUtils.waitForElements;

public class ItemsPage extends AbstractPom {
    @FindBy(xpath = "//*[starts-with(@id, 'item-')]")
    List<WebElement> items;

    @FindBy(className = ("alert--info"))
    WebElement infoAlert;

    @FindBy(className = ("alert--error"))
    WebElement errorAlert;

    @FindBy(id = "add-item-button")
    WebElement addButton;

    @FindBy(className = "show-rents-btn")
    List<WebElement> rentsButtons;

    @FindBy(className = "remove-btn")
    List<WebElement> removeButtons;

    @FindBy(className = "edit-btn")
    List<WebElement> editButtons;

    @FindBy(className = "item-form")
    WebElement itemDialog;

    @FindBy(name = "purchase-date")
    WebElement purchaseDateField;

    @FindBy(name = "submit-button")
    WebElement dialogSubmitButton;

    @FindBy(name = "return-button")
    WebElement returnButton;

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

    public ItemsPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean areCopiesVisible() {
        return !items.isEmpty();
    }

    public int getItemsCount() {
        return items.size();
    }

    public void goToRents(int id) {
        rentsButtons.get(id).click();
    }

    public void goToTitles() {
        returnButton.click();
    }

    public void addItem() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        addButton.click();

        wait.until(ExpectedConditions.visibilityOf(purchaseDateField));

        String actualYear = String.valueOf(LocalDate.now().getYear());
        String actualMonth = String.valueOf(LocalDate.now().getMonth());
        String actualDay = String.valueOf(LocalDate.now().getDayOfMonth());

        purchaseDateField.click();
        wait.until(ExpectedConditions.visibilityOf(monthChangeButton)).click();
        wait.until(ExpectedConditions.visibilityOf(yearChangeButton)).click();
        clickSelectedCell(yearCells, actualYear);
        clickSelectedCell(monthCells, actualMonth);
        clickSelectedCell(dayCells, actualDay);
        dialogSubmitButton.click();
    }

    public void editItem(int id) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        editButtons.get(id).click();

        wait.until(ExpectedConditions.visibilityOf(purchaseDateField));

        String newDate = getNextDay( purchaseDateField.getAttribute("value"));
        LocalDate parsedDate = getDateFromString(newDate);

        String selectedYear = String.valueOf(parsedDate.getYear());
        String selectedMonth = parsedDate.getMonth().toString();
        String selectedDay = String.valueOf(parsedDate.getDayOfMonth());

        purchaseDateField.click();
        wait.until(ExpectedConditions.visibilityOf(monthChangeButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(yearChangeButton)).click();
        clickSelectedCell(waitForElements(driver, yearCells), selectedYear);
        clickSelectedCell(waitForElements(driver, monthCells), selectedMonth);
        clickSelectedCell(waitForElements(driver, dayCells), selectedDay);
        dialogSubmitButton.click();
    }

    public void removeItem(int id, boolean hasRents) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement buttonToRemove = removeButtons.get(id);
        buttonToRemove.click();

        if(hasRents) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        } else {
            wait.until(ExpectedConditions.stalenessOf(buttonToRemove));
        }
    }
}
