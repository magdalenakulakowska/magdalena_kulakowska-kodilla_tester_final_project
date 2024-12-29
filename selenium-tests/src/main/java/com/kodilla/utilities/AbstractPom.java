package com.kodilla.utilities;

import org.openqa.selenium.WebDriver;

public abstract class AbstractPom {
    protected WebDriver driver;

    public AbstractPom(WebDriver driver) {
        this.driver = driver;
    }
}
