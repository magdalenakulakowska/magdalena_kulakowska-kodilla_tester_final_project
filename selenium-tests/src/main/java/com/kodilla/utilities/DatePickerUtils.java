package com.kodilla.utilities;

import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatePickerUtils {

    private static final String PATTERN = "yyyy-MM-dd";

    public static  String getDateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return date.format(formatter);
    }

    public static LocalDate getDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return LocalDate.parse(date, formatter);
    }

    public static String getNextDay(String oldDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        LocalDate date = LocalDate.parse(oldDate, formatter);
        LocalDate nextDate = date.plusDays(1);
        return nextDate.format(formatter);
    }

    public static void clickSelectedCell(List<WebElement> elements, String selectedItem){
        for(WebElement element : elements){
            if(element.getText().toUpperCase().equals(selectedItem)) {
                element.click();
            }
        }
    }
}
