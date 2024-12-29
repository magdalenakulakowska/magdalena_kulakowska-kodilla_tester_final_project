package com.kodilla.pages.rentsPage;

import com.kodilla.utilities.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.util.List;

import static com.kodilla.utilities.Config.BASE_URL;
import static com.kodilla.utilities.DatePickerUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class RentsPageTestSuite extends BaseTest {

    @Test
    void testNoRentsAvailable() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 1;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--info")));
        String successAlertMessage = rentsPage.infoAlert.getText();

        assertFalse(itemsPage.areCopiesVisible(),
                "The copies list should be empty when no copies are available."
        );
        assertEquals("No rents...", successAlertMessage, "Info alert message should have correct text.");
    }

    @Test
    void testRentsViewDataCheck() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 2;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rents-list")));
        List<WebElement> customerName = driver.findElements(By.className("rents-list__rent__customer-name"));
        List<WebElement> rentDate = getRentDates();
        List<WebElement> expirationDate = getExpirationDates();

        assertTrue(rentsPage.areRentsVisible(), "Titles should be visible on the page.");
        assertFalse(customerName.isEmpty(), "There should be at least one element with 'customerName' information.");
        assertFalse(rentDate.isEmpty(), "There should be at least one element with 'rent date' information.");
        assertFalse(expirationDate.isEmpty(), "There should be at least one element with 'expiration date' information.");
    }

    @Test
    void testIfReturnButtonRedirectsToCopiesListView() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 2;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rents-list")));

        rentsPage.goToCopies();
        wait.until(ExpectedConditions.urlContains(BASE_URL + "items/"));
        String currentUrl = driver.getCurrentUrl();
        String expectedBaseUrl = BASE_URL + "items/";

        assertTrue(currentUrl.startsWith(expectedBaseUrl), "User should be redirected to the copies list.");
    }

    @Test
    void testAddRent() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 2;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rents-list")));
        int prevCount = rentsPage.getRentsCount();
        String customerName = "Jane Doe";

        rentsPage.addRent(customerName);
        wait.until(driver -> driver.findElements(By.cssSelector(".rents-list li")).size() > prevCount);
        int actualCount = rentsPage.getRentsCount();
        String addedCustomerName = driver.findElements(By.className("rents-list__rent__customer-name")).getLast().getText();
        String rentDate = getRentDates().getLast().getText();
        String expirationDate = getExpirationDates().getLast().getText();
        LocalDate today = LocalDate.now();
        LocalDate expectedExpirationDate = today.plusDays(3);

        assertEquals(prevCount + 1, actualCount, "Items should have one title more.");
        assertEquals(customerName.toUpperCase(), addedCustomerName, "Customer name should be Jane Doe.");
        assertEquals(getDateString(today), rentDate, "Rent date should be today's date.");
        assertEquals("(expiration: " + getDateString(expectedExpirationDate) + ")", expirationDate,
                "Expiration date should be 7 days after today's date."
        );
    }

    @Test
    void testAddRentWithoutCustomerName() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 2;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rents-list")));

        rentsPage.addRent("");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = rentsPage.errorAlert.getText();

        assertEquals("\"customerName\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testIfEditRentDataIsCorrect() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 2;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rents-list")));
        String lastRentCustomerNameText = rentsPage.rents.getLast().findElement(By.className("rents-list__rent__customer-name")).getText();
        String lastRentRentDateText = getRentDates().getLast().getText();
        WebElement lastButton = rentsPage.editButtons.getLast();

        lastButton.click();
        wait.until(ExpectedConditions.visibilityOf(rentsPage.rentDialog));
        String editRentCustomerNameText = rentsPage.customerNameField.getAttribute("value").toUpperCase();
        String editRentRentDateText = rentsPage.rentDateField.getAttribute("value").toUpperCase();

        assertEquals(lastRentCustomerNameText,
                editRentCustomerNameText,
                "Customer name field should have the same value as selected Rent from Rents list"
        );
        assertEquals(lastRentRentDateText,
                editRentRentDateText,
                "Rent date field should have the same value as selected Rent from Rents list"
        );
    }

    @Test
    void testEditRent() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 2;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rents-list")));
        String lastRentCustomerNameText = rentsPage.rents.getLast().findElement(By.className("rents-list__rent__customer-name")).getText();
        String lastRentRentDateText = getRentDates().getLast().getText();
        String lastRentExpirationDateText = getExpirationDates().getLast().getText();
        lastRentExpirationDateText = lastRentExpirationDateText.substring(lastRentExpirationDateText.indexOf(":") + 2, lastRentExpirationDateText.indexOf(")"));
        int rentId = rentsPage.getRentsCount() - 1;
        String customerName = lastRentCustomerNameText + " updated";

        rentsPage.editRent(rentId,customerName);
        String updatedRentDate = getNextDay(lastRentRentDateText);
        String updatedExpirationDate = getNextDay(lastRentExpirationDateText);
        wait.until(driver ->
                driver.findElements(By.className("rents-list__rent__customer-name"))
                        .getLast()
                        .getText()
                        .equals(customerName.toUpperCase()
                        ));
      String actualCustomerName = rentsPage.rents.getLast().findElements(By.className("rents-list__rent__customer-name"))
                .getLast()
                .getText();
        String actualRentDate = getRentDates().getLast().getText();
        String actualExpirationDate = getExpirationDates().getLast().getText();

        assertEquals(customerName.toUpperCase(),
                actualCustomerName,
                "Customer name should be changed with added word 'updated'."
        );
        assertEquals(updatedRentDate,
                actualRentDate,
                "Rent date should be changed to one day later."
        );
        assertEquals("(expiration: " + updatedExpirationDate + ")",
                actualExpirationDate,
                "Expiration date should be changed to one day later."
        );
    }

    @Test
    void testEditRentWithEmptyCustomerName() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 2;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rents-list")));

        int rentId = 0;
        rentsPage.editRent(rentId, "");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = rentsPage.errorAlert.getText();

        assertEquals("\"customerName\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testRemoveRent() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        int titleId = 2;
        int copyId = 0;
        goToRentsView(titleId, copyId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rents-list")));
        int prevCount = rentsPage.getRentsCount();
        rentsPage.removeButtons = driver.findElements(By.className("remove-btn"));
        int rentId = prevCount - 1;

        rentsPage.removeRent(rentId);
        int actualCount = rentsPage.getRentsCount();

        assertEquals(prevCount-1, actualCount, "Rents should have one rent less.");
    }
}