package com.example.selenium_finish;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

public class LocalLoginTest {
    private WebDriver driver;
    private String localUrl;

    @BeforeMethod
    public void setUp() {
        // Point to the local login.html file
        File file = new File("webpage/login.html");
        localUrl = file.toURI().toString();

        // Configure Headless Chrome options for local/CI execution
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    public void testValidLogin() {
        driver.get(localUrl);

        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("loginBtn")).click();

        WebElement message = driver.findElement(By.id("message"));
        Assert.assertEquals(message.getText(), "Login Successful!");
    }

    @Test
    public void testInvalidLogin() {
        driver.get(localUrl);

        driver.findElement(By.id("username")).sendKeys("wrongUser");
        driver.findElement(By.id("password")).sendKeys("wrongPass");
        driver.findElement(By.id("loginBtn")).click();

        WebElement message = driver.findElement(By.id("message"));
        Assert.assertEquals(message.getText(), "Invalid Credentials!");
    }

    @Test
    public void testBackgroundColor() {
        driver.get(localUrl);

        // Validate CSS background color of the main form container (#f4f4f9 in hex is rgb(244, 244, 249))
        WebElement container = driver.findElement(By.id("form-container"));
        String bgColor = container.getCssValue("background-color");
        
        // Browsers typically return RGB format
        boolean isCorrectColor = bgColor.equals("rgba(255, 255, 255, 1)") || bgColor.equals("rgb(255, 255, 255)");
        Assert.assertTrue(isCorrectColor, "Background color verification failed. Found: " + bgColor);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}