package org.example.pom.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.function.Function;

public class GroupTableRow {
    private final WebElement root;

    public GroupTableRow(WebElement root) {
        this.root = root;
    }

    public String getTitle() {
        return root.findElement(By.xpath("./td[2]")).getText();
    }

    public String getStatus() {
        return root.findElement(By.xpath("./td[3]")).getText();
    }

    public String getAccounts() {
        return root.findElement(By.xpath("./td[4]/button[1]/span/span")).getText();
    }

    public void clickMagnifierIcon() {
        root.findElement(By.xpath("./td/button[text()='zoom_in']")).click();
        waitUntil(root -> root.findElement(By.xpath("//table[@aria-label='User list']/tbody/tr")));
    }

    public void clickTrashIcon() {
        root.findElement(By.xpath("//table[@aria-label='User list']/tbody/tr[1]/td[4]" +
                "/button[text()='delete']")).click();
        waitUntil(root -> root.findElement(By.xpath("//table[@aria-label='User list']/tbody/" +
                "tr[1]/td[4]/button[text()='restore_from_trash']")));
    }

    public void clickRestoreFromTrashIcon() {
        root.findElement(By.xpath("//table[@aria-label='User list']/tbody/tr[1]/td[4]/" +
                "button[text()='restore_from_trash']")).click();
        waitUntil(root -> root.findElement(By.xpath("./td/button[text()='delete']")));
    }

    public void clickAddCountAccounts() {
        root.findElement(By.xpath("//table[@aria-label='Tutors list']/tbody/tr[1]/td[4]/" +
                "button/i[text()='add']")).click();
    }

    private void waitUntil(Function<WebElement, WebElement> condition) {
        new FluentWait<>(root)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .until(condition);
    }
}
