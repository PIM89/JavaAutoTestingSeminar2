package org.example.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriverWait wait;

    @FindBy(css = "form#login input[type='text']")
    private WebElement usernameField;

    @FindBy(css = "form#login input[type='password']")
    private WebElement passwordField;

    @FindBy(css = "form#login button")
    private WebElement loginButton;

    @FindBy(css = "div.error-block h2")
    private WebElement errorCode;
    @FindBy(css = "div.error-block p:nth-of-type(1)")
    private WebElement errorName;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.wait = wait;
    }

    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    public void typeUsernameInField(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField)).sendKeys(username);
    }

    public void typePasswordInField(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.visibilityOf(loginButton)).click();
    }

    public String getErrorCodeText() {
        return wait.until(ExpectedConditions.visibilityOf(errorCode)).getText();
    }

    public String getErrorNameText() {
        return wait.until(ExpectedConditions.visibilityOf(errorName)).getText();
    }

}
