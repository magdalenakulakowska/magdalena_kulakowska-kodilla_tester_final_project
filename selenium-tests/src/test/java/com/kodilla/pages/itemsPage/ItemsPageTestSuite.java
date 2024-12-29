package com.kodilla.pages.itemsPage;

import com.kodilla.utilities.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.util.List;

import static com.kodilla.utilities.Config.BASE_URL;
import static com.kodilla.utilities.DatePickerUtils.getDateString;
import static com.kodilla.utilities.DatePickerUtils.getNextDay;
import static org.junit.jupiter.api.Assertions.*;

class ItemsPageTestSuite extends BaseTest {

    @Test
    void testNoItemsAvailable() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 0;

        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--info")));
        String successAlertMessage = itemsPage.infoAlert.getText();

        assertFalse(itemsPage.areCopiesVisible(),
                "The copies list should be empty when no copies are available."
        );
        assertEquals("No copies...", successAlertMessage,  "Info alert message should have correct text.");
    }

    @Test
    void testItemsViewDataCheck() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 1;

        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        List<WebElement> index = driver.findElements(By.className("items-list__item__index"));
        List<WebElement> purchaseDate = driver.findElements(By.className("items-list__item__purchase-date"));
        List<WebElement> status = driver.findElements(By.className("items-list__item__status"));

        assertTrue(itemsPage.areCopiesVisible(), "Titles should be visible on the page.");
        assertFalse(index.isEmpty(), "There should be at least one element with 'index' information.");
        assertFalse(purchaseDate.isEmpty(), "There should be at least one element with 'purchase date' information.");
        assertFalse(status.isEmpty(), "There should be at least one element with 'status' information.");
    }

    @Test
    void testIfShowHistoryRedirectsToRentsListView() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 1;

        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        id = 0;
        itemsPage.goToRents(id);
        WebElement element = driver.findElement(By.cssSelector(".items-list li a"));
        String elementUrl = element.getAttribute("href");
        wait.until(ExpectedConditions.urlToBe(elementUrl));
        String currentUrl = driver.getCurrentUrl();
        String expectedBaseUrl = BASE_URL + "rents/";

        assertEquals(currentUrl, elementUrl, "User should be redirected to the rents list.");
        assertTrue(currentUrl.startsWith(expectedBaseUrl), "User should be redirected to the rents list.");
    }

    @Test
    void testIfReturnButtonRedirectsToTitlesListView() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 1;

        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        itemsPage.goToTitles();
        wait.until(ExpectedConditions.urlToBe(BASE_URL));
        String currentUrl = driver.getCurrentUrl();

        assertEquals(BASE_URL, currentUrl, "User should be redirected to the titles list.");
    }

    @Test
    void testAddItem() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 1;
        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        int prevCount = itemsPage.getItemsCount();

        itemsPage.addItem();
        wait.until(driver -> driver.findElements(By.cssSelector(".items-list li")).size() > prevCount);
        int actualCount = itemsPage.getItemsCount();
        String date = driver.findElements(By.className("items-list__item__purchase-date")).getLast().getText();
        LocalDate today = LocalDate.now();

        assertEquals(prevCount + 1, actualCount, "Items should have one title more.");
        assertEquals(getDateString(today), date, "Date should be today's date.");
    }

    @Test
    void testIfEditItemDataIsCorrect() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 1;
        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        String lastItemPurchaseDateText = itemsPage.items.getLast().findElement(By.className("items-list__item__purchase-date")).getText();
        WebElement lastButton = itemsPage.editButtons.getLast();

        lastButton.click();
        wait.until(ExpectedConditions.visibilityOf(itemsPage.itemDialog));
        String editItemPurchaseDateText = itemsPage.purchaseDateField.getAttribute("value").toUpperCase();

        assertEquals(lastItemPurchaseDateText,
                editItemPurchaseDateText,
                "Purchase date field should have the same value as selected Item from Items list"
        );
    }

    @Test
    void testEditItem() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 1;
        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        String prevPurchaseDate = itemsPage.items.getLast().findElements(By.className("items-list__item__purchase-date"))
                .getLast()
                .getText();
        id = itemsPage.getItemsCount() - 1;

        itemsPage.editItem(id);
        String updatedDate = getNextDay(prevPurchaseDate);
        wait.until(driver ->
                driver.findElements(By.className("items-list__item__purchase-date"))
                        .getLast()
                        .getText()
                        .equals(updatedDate
                        ));
        String actualPurchaseDate = itemsPage.items.getLast().findElements(By.className("items-list__item__purchase-date"))
                .getLast()
                .getText();

        assertEquals(updatedDate,
                actualPurchaseDate,
                "Purchase date should be changed to one day later."
        );
    }

    @Test
    void testRemoveCopy() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 1;
        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        int prevCount = itemsPage.getItemsCount();
        itemsPage.removeButtons = driver.findElements(By.className("remove-btn"));
        id = prevCount - 1;

        itemsPage.removeItem(id, false);
        int actualCount = itemsPage.getItemsCount();

        assertEquals(prevCount-1, actualCount, "Copies should have one copy less.");
    }

    @Test
    void testRemoveCopyThatContainsRents() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int id = 2;
        titlesPage.goToCopies(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("items-list")));
        itemsPage.removeButtons = driver.findElements(By.className("remove-btn"));
        id = 0;

        titlesPage.removeTitle(id, true);
        String alertText = itemsPage.errorAlert.getText();

        assertEquals("You can't remove copy with the rents history!",
                alertText,
                "Text in error alert should be correct."
        );
    }
}