package org.example.tests;

import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Пример использования самых базовых методов библиотеки Selenium.
 */
public class GeekBrainsStandTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;

    private static final String USERNAME = "MaximDav";
    private static final String PASSWORD = "a188da4213";

    @BeforeAll
    public static void setupClass() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    }

    @BeforeEach
    public void setupTest() {
        // Создаём экземпляр драйвера
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Растягиваем окно браузера на весь экран
        driver.manage().window().maximize();
        // Навигация на https://test-stand.gb.ru/login
        driver.get("https://test-stand.gb.ru/login");
        // Объект созданного Page Object
        loginPage = new LoginPage(driver, wait);
    }

    @AfterEach
    public void teardown() {
        // Закрываем все окна брайзера и процесс драйвера
        driver.quit();
    }

    @Test
    public void testAddingGroupOnMainPage() {
        checkLogin();
        // Создание группы. Даём ей уникальное имя, чтобы в каждом запуске была проверка нового имени
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
    }

    @Test
    void testArchiveGroupOnMainPage() {
        checkLogin();
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        // Требуется закрыть модальное окно
        mainPage.closeCreateGroupModalWindow();
        // Изменение созданной группы с проверками
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("inactive", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickRestoreFromTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
    }

    @Test
    public void checkAddAccount() {
        checkLogin();
        int currentCountAccounts = Integer.parseInt(mainPage.getAccountsInFirstRow());
        mainPage.addOneAccount();
        mainPage.closeAddAccountModalWindows();
        int newCountAccount = Integer.parseInt(mainPage.getAccountsInFirstRow());
        //Проверка что цифра отражающая количество студентов поменялась на 1
        assertEquals(currentCountAccounts + 1, newCountAccount);

        mainPage.clickMagnifierIcon();
        int countRowInStudentTable = mainPage.getCountStudent();
        // Проверка количества студентов в модальном окне
        assertEquals(countRowInStudentTable, newCountAccount);

        mainPage.clickTrashIcon();
        // Проверка того что статус после нажатия на кнопку изменился на block
        assertEquals("block", mainPage.getStatusStudent().getText());

        mainPage.clickRestoreFromTrashIcon();
        // Проверка того что статус после нажатия на кнопку изменился на active
        assertEquals("active", mainPage.getStatusStudent().getText());
    }

    private void checkLogin() {
        // Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса MainPage
        mainPage = new MainPage(driver, wait);
        // Проверка, что логин прошёл успешно
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
    }

    // Задание к семинару #2.
    @Test
    public void checkEnterWithoutLoginAndPasswd() {
        loginPage.clickLoginButton();
        assertEquals("401", loginPage.getErrorCodeText());
        assertEquals("Invalid credentials.", loginPage.getErrorNameText());
    }

}

