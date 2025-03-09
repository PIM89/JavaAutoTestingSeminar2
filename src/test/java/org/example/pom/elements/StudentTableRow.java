package org.example.pom.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.function.Function;

public class StudentTableRow {
    private final WebElement root;

    public StudentTableRow(WebElement root) {
        this.root = root;
    }

    public void clickTrashIcon() {
        root.findElement(By.xpath("./td/button[text()='delete']")).click();

    }

    public void clickRestoreFromTrashIcon() {
        root.findElement(By.xpath("./td/button[text()='restore_from_trash']")).click();
        waitUntil(root -> root.findElement(By.xpath("//td[text()='active']")));
    }

    public WebElement getStatusStudentField() {
        waitUntil(root -> root.findElement(By.xpath("//table[@aria-label='User list']/tbody/tr/td[4]")));
        return root.findElement(By.xpath("//table[@aria-label='User list']/tbody/tr/td[4]"));
    }

    private void waitUntil(Function<WebElement, WebElement> condition) {
        new FluentWait<>(root)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class)
                .until(condition);
    }
}
