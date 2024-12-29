package com.kodilla.pages.titlesPage;

import com.kodilla.utilities.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kodilla.utilities.Config.BASE_URL;
import static org.junit.jupiter.api.Assertions.*;

class TitlesPageTestSuite extends BaseTest {

    @Test
    void testNoTitlesAvailable() {
        login("test_user_empty", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--info")));

        String successAlertMessage = titlesPage.infoAlert.getText();
        String backgroundColor = titlesPage.infoAlert.getCssValue("background-color");
        String textColor = titlesPage.infoAlert.getCssValue("color");

        assertFalse(titlesPage.areTitlesVisible(),
                "The title list should be empty when no titles are available."
        );
        assertEquals(successAlertMessage, "No titles", "Info alert message should have correct text.");
        assertEquals(backgroundColor, blue, "Alert background color should be blue");
        assertEquals(textColor, white, "Alert text color should be white");
    }

    @Test
    void testTilesViewDataCheck() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));

        List<WebElement> title = driver.findElements(By.className("titles-list__item__title"));
        List<WebElement> author = driver.findElements(By.className("titles-list__item__author"));
        List<WebElement> year = driver.findElements(By.className("titles-list__item__year"));

        assertTrue(titlesPage.areTitlesVisible(), "Titles should be visible on the page.");
        assertFalse(title.isEmpty(), "There should be at least one element with 'title' information.");
        assertFalse(author.isEmpty(), "There should be at least one element with 'author' information.");
        assertFalse(year.isEmpty(), "There should be at least one element with 'year' information.");
    }

    @Test
    void testIfShowCopiesRedirectsToCopiesListView() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));

        int id = 0;
        titlesPage.goToCopies(id);
        WebElement element = driver.findElement(By.cssSelector(".titles-list li a"));
        String elementUrl = element.getAttribute("href");
        wait.until(ExpectedConditions.urlToBe(elementUrl));

        String currentUrl = driver.getCurrentUrl();
        String expectedBaseUrl = BASE_URL + "items/";
        assertEquals(currentUrl, elementUrl, "User should be redirected to the copies list.");
        assertTrue(currentUrl.startsWith(expectedBaseUrl), "User should be redirected to the copies list.");
    }

    @Test
    void testAddTitle() {
        login("test_user", "test");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fog")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-title-button")));
        int prevCount = titlesPage.getTitlesCount();

        titlesPage.addTitle("Lion King" + prevCount, "Disney Production", "1993");
        wait.until(driver -> driver.findElements(By.cssSelector(".titles-list li")).size() > prevCount);
        int actualCount = titlesPage.getTitlesCount();
        String title = driver.findElements(By.className("titles-list__item__title")).getLast().getText();

        assertEquals(prevCount + 1, actualCount, "Titles should have one title more.");
        assertEquals("Lion King".toUpperCase() + prevCount, title, "Titles should be the same");
    }

    @Test
    void testAddTitleWithTitleFieldEmpty() {
        login("test_user", "test");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fog")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-title-button")));

        titlesPage.addTitle("", "Disney Production", "1993");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();

        assertEquals("\"title\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testAddTitleWithAuthorFieldEmpty() {
        login("test_user", "test");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fog")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-title-button")));

        titlesPage.addTitle("Lion King", "", "1993");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();
        assertEquals("\"author\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testAddTitleWithYearFieldEmpty() {
        login("test_user", "test");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fog")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-title-button")));

        titlesPage.addTitle("Lion King", "Disney Production", "");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();

        assertEquals("\"year\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testAddTitleWithAllFieldsEmpty() {
        login("test_user", "test");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fog")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-title-button")));

        titlesPage.addTitle("", "", "");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();

        assertEquals("\"title\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testIfEditTitleDataIsCorrect() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        WebElement lastTitle = titlesPage.titles.getLast();
        String lastTitleTitleText = lastTitle.findElement(By.className("titles-list__item__title")).getText();
        String lastTitleAuthorText = lastTitle.findElement(By.className("titles-list__item__author")).getText();
        String lastTitleYearText = lastTitle.findElement(By.className("titles-list__item__year")).getText();
        WebElement lastButton = titlesPage.editButtons.getLast();

        lastButton.click();
        wait.until(ExpectedConditions.visibilityOf(titlesPage.titleDialog));
        String editTitleTitleText = titlesPage.titleField.getAttribute("value").toUpperCase();
        String editTitleAuthorText = titlesPage.authorField.getAttribute("value");
        String editTitleYearText = titlesPage.yearField.getAttribute("value");

        assertEquals(lastTitleTitleText,
                editTitleTitleText,
                "Title field should have the same value as selected Title from Titles list"
        );

        assertEquals(lastTitleAuthorText,
                "by " + editTitleAuthorText,
                "Author field should have the same value as selected Title from Titles list"
        );

        assertEquals(lastTitleYearText,
                "(" + editTitleYearText + ")",
                "Year field should have the same value as selected Title from Titles list"
        );
    }

    @Test
    void testEditTitle() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));
        String prevTitle = titlesPage.titles.getLast().findElements(By.className("titles-list__item__title"))
                .getLast()
                .getText();
        String prevAuthor = titlesPage.titles.getLast().findElements(By.className("titles-list__item__author"))
                .getLast()
                .getText();
        String prevYear = titlesPage.titles.getLast().findElements(By.className("titles-list__item__year"))
                .getLast()
                .getText();

        titlesPage.editTitle(" updated", " updated", "1");
        wait.until(driver ->
                driver.findElements(By.className("titles-list__item__title"))
                        .getLast()
                        .getText()
                        .equals(prevTitle + " UPDATED"
                ));
        String actualTitle = titlesPage.titles.getLast().findElements(By.className("titles-list__item__title"))
                .getLast()
                .getText();
        String actualAuthor = titlesPage.titles.getLast().findElements(By.className("titles-list__item__author"))
                .getLast()
                .getText();
        String actualYear = titlesPage.titles.getLast().findElements(By.className("titles-list__item__year"))
                .getLast()
                .getText();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(prevYear);
        if (matcher.find()) {
            prevYear = matcher.group();
        }

        assertEquals(prevTitle + " UPDATED",
                actualTitle,
                "Title should be updated with \"updated\" word"
        );
        assertEquals(prevAuthor + " updated",
                actualAuthor,
                "Author should be updated with \"updated\" word"
        );
        assertEquals("(" + (Integer.parseInt(prevYear) + 1) + ")",
                actualYear,
                "Year should be increased by 1"
        );
    }

    @Test
    void testEditTitleWithEmptyTitleField() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));

        titlesPage.editTitle("remove", " updated", "1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();

        assertEquals("\"title\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testEditTitleWithEmptyAuthorField() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));

        titlesPage.editTitle(" updated", "remove", "1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();

        assertEquals("\"author\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testEditTitleWithEmptyYearField() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));

        titlesPage.editTitle(" updated", " updated", "-1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();

        assertEquals("\"year\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testEditTitleWithYearFieldEquals0() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));

        titlesPage.editTitle(" updated", " updated", "0");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();

        assertEquals("\"year\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testEditTitleWithAllFieldsEmpty() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("titles-list")));

        titlesPage.editTitle("remove", "remove", "-1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--error")));
        String alertText = titlesPage.errorAlert.getText();
        assertEquals("\"author\" field shouldn't be empty...",
                alertText,
                "Text in error alert should be correct."
        );
    }

    @Test
    void testRemoveTitle() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("remove-btn")));
        int prevCount = titlesPage.getTitlesCount();
        titlesPage.removeButtons = driver.findElements(By.className("remove-btn"));
        int id = prevCount - 1;

        titlesPage.removeTitle(id, false);
        int actualCount = titlesPage.getTitlesCount();

        assertEquals(prevCount-1, actualCount, "Titles should have one title less.");
    }

    @Test
    void testRemoveTitleThatContainsCopies() {
        login("test_user", "test");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("remove-btn")));
        titlesPage.removeButtons = driver.findElements(By.className("remove-btn"));
        int id = 1;

        titlesPage.removeTitle(id, true);
        String alertText = titlesPage.errorAlert.getText();

        assertEquals("You can't remove titles with copies!",
                alertText,
                "Text in error alert should be correct."
        );
    }
}