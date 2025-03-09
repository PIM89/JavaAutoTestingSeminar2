package org.example.pom;

import org.example.pom.elements.GroupTableRow;
import org.example.pom.elements.StudentTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.crypto.spec.PSource;
import java.util.List;

public class MainPage {
    private final WebDriverWait wait;
    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private WebElement usernameLinkInNavBar;
    @FindBy(id = "create-btn")
    private WebElement createGroupButton;
    @FindBy(xpath = "//form//span[contains(text(), 'Group name')]/following-sibling::input")
    private WebElement groupNameField;
    @FindBy(css = "table[class='mdc-data-table__table'][aria-label='Tutors list']")
    private WebElement rootTable;
    @FindBy(css = "form div.submit button")
    private WebElement submitButtonOnModalWindow;
    @FindBy(xpath = "//*[@id=\"app\"]/main/div/div/div[3]/div[2]/div/div[1]/button")
    private WebElement addCountAccountModalCloseButton;
    @FindBy(xpath = "//form//span[contains(text(), 'New logins count')]/following-sibling::input")
    private WebElement countLoginsField;
    @FindBy(xpath = "//*[@id=\"generate-logins\"]/div[2]/button")
    private WebElement saveButtonOnModalWindowCreatingNewLogins;
    @FindBy(xpath = "//span[text()='Creating Study Group']" +
            "//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private WebElement closeCreateGroupIcon;
    @FindBy(xpath = "//table[@aria-label='Tutors list']/tbody/tr")
    private List<WebElement> rowsInGroupTable;
    @FindBy(xpath = "//table[@aria-label='User list']/tbody/tr")
    private List<WebElement> rowsInStudentTable;

    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void waitGroupTitleByText(String title) {
        String xpath = String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']", title);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void createGroup(String groupName) {
        wait.until(ExpectedConditions.visibilityOf(createGroupButton)).click();
        wait.until(ExpectedConditions.visibilityOf(groupNameField)).sendKeys(groupName);
        wait.until(ExpectedConditions.textToBePresentInElementValue(groupNameField, groupName));
        submitButtonOnModalWindow.click();
        waitGroupTitleByText(groupName);
    }

    public void addOneAccount() {
        // нажать на кнопку +
        clickAddCountAccounts();
        // +1 аккаунт
        insertOneAccountInCountLoginsField();
        // нажать на кнокпу сохранить
        clickSaveAddAccount();
        wait.until(ExpectedConditions.textToBePresentInElementValue(countLoginsField, "0"));
    }

    public void clickMagnifierIcon() {
        getFirstRowTutorPage().clickMagnifierIcon();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//table[@aria-label='User list']/tbody/tr")));
    }

    public int getCountStudent() {
        return rowsInStudentTable.size();
    }

    public void clickTrashIcon() {
        getFirstRowStudentsIdentities().clickTrashIcon();
        wait.until(ExpectedConditions.textToBe(By.xpath("//table[@aria-label='User list']/tbody/tr[1]/td[4]"),
                "block"));
    }

    public void clickRestoreFromTrashIcon() {
        getFirstRowStudentsIdentities().clickRestoreFromTrashIcon();
        wait.until(ExpectedConditions.textToBe(By.xpath("//table[@aria-label='User list']/tbody/tr[1]/td[4]"),
                "active"));
    }

    public WebElement getStatusStudent() {
        return getFirstRowStudentsIdentities().getStatusStudentField();
    }

    public void clickSaveAddAccount() {
        saveButtonOnModalWindowCreatingNewLogins.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeAddAccountModalWindows() {
        addCountAccountModalCloseButton.click();
    }

    public String getAccountsInFirstRow() {
        wait.until(ExpectedConditions.visibilityOfAllElements(rowsInGroupTable));
        return getFirstRowTutorPage().getAccounts();
    }

    public void closeCreateGroupModalWindow() {
        closeCreateGroupIcon.click();
        wait.until(ExpectedConditions.invisibilityOf(closeCreateGroupIcon));
    }

    public String getUsernameLabelText() {
        return wait.until(ExpectedConditions.visibilityOf(usernameLinkInNavBar))
                .getText().replace("\n", " ");
    }

    public void clickTrashIconOnGroupWithTitle(String title) {
        getRowByTitle(title).clickTrashIcon();
    }

    public void clickAddCountAccounts() {
        getFirstRowTutorPage().clickAddCountAccounts();
    }

    public void clickRestoreFromTrashIconOnGroupWithTitle(String title) {
        getRowByTitle(title).clickRestoreFromTrashIcon();
    }

    public String getStatusOfGroupWithTitle(String title) {
        return getRowByTitle(title).getStatus();
    }

    private GroupTableRow getRowByTitle(String title) {
        return rowsInGroupTable.stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getTitle().equals(title))
                .findFirst().orElseThrow();
    }

    public GroupTableRow getFirstRowTutorPage() {
        return rowsInGroupTable.stream().map(GroupTableRow::new).findFirst().orElseThrow();
    }

    public StudentTableRow getFirstRowStudentsIdentities() {
        wait.until(ExpectedConditions.visibilityOfAllElements(rowsInStudentTable));
        return rowsInStudentTable.stream().map(StudentTableRow::new).findFirst().orElseThrow();
    }

    public void insertOneAccountInCountLoginsField() {
        wait.until(ExpectedConditions.visibilityOf(countLoginsField)).sendKeys("1");
    }
}
