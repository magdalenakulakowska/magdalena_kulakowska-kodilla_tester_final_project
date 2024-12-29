package com.kodilla.pages.titlesPage;

import com.kodilla.utilities.AbstractPom;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TitlesPage extends AbstractPom {

    @FindBy(xpath = "//*[starts-with(@id, 'title-')]")
    List<WebElement> titles;

    @FindBy(className = ("alert--info"))
    WebElement infoAlert;

    @FindBy(className = ("alert--error"))
    WebElement errorAlert;

    @FindBy(id = "add-title-button")
    WebElement addButton;

    @FindBy(className = "show-copies-btn")
    List<WebElement> copiesButtons;

    @FindBy(className = "remove-btn")
    List<WebElement> removeButtons;

    @FindBy(className = "edit-btn")
    List<WebElement> editButtons;

    @FindBy(className = "title-form")
    WebElement titleDialog;

    @FindBy(name = "title")
    WebElement titleField;

    @FindBy(name = "author")
    WebElement authorField;

    @FindBy(name = "year")
    WebElement yearField;

    @FindBy(name = "submit-button")
    WebElement dialogSubmitButton;

    public TitlesPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean areTitlesVisible() {
        return !titles.isEmpty();
    }

    public int getTitlesCount() {
        return titles.size();
    }

    public void goToCopies(int id) {
        copiesButtons.get(id).click();
    }

    public void addTitle(String title, String author, String year) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        addButton.click();

        wait.until(ExpectedConditions.visibilityOf(titleDialog));

        titleField.sendKeys(title);
        authorField.sendKeys(author);
        yearField.sendKeys(year);
        dialogSubmitButton.click();
    }

    public void editTitle(String title, String author, String year) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        editButtons.getLast().click();

        wait.until(ExpectedConditions.visibilityOf(titleDialog));

        String newTitle = titleField.getAttribute("value") + title;
        String newAuthor = authorField.getAttribute("value") + author;
        String newYear = (year.equals("0")) ? "0" : String.valueOf(Integer.parseInt(yearField.getAttribute("value")) + Integer.parseInt(year));

        titleField.sendKeys(Keys.CONTROL + "a");
        titleField.sendKeys(Keys.DELETE);
        authorField.sendKeys(Keys.CONTROL + "a");
        authorField.sendKeys(Keys.DELETE);
        yearField.sendKeys(Keys.CONTROL + "a");
        yearField.sendKeys(Keys.DELETE);

        if(!title.equals("remove")) {
            titleField.sendKeys(newTitle);
        }
        if(!author.equals("remove")) {
            authorField.sendKeys(newAuthor);
        }
        if(!year.equals("-1")){
            yearField.sendKeys(newYear);
        }
        dialogSubmitButton.click();
    }

    public void removeTitle(int id, boolean hasCopies) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement buttonToRemove = removeButtons.get(id);
        buttonToRemove.click();

        if(hasCopies) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        } else {
            wait.until(ExpectedConditions.stalenessOf(buttonToRemove));
        }
    }
}
