/*package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {


    protected WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.btl.gov.il/");
    }

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            System.out.println("❌ הטסט נכשל: " + context.getDisplayName() + ". מבצע צילום מסך...");
            takeScreenshot(context.getDisplayName());
        }
    };

    private void takeScreenshot(String testName) {
        try {
            // יצירת תיקיית screenshots אם היא לא קיימת
            Path path = Paths.get("screenshots");
            if (!Files.exists(path)) Files.createDirectories(path);

            // צילום המסך
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // שם הקובץ עם תאריך ושעה
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/" + testName + "_" + timestamp + ".png";

            Files.copy(screenshot.toPath(), Paths.get(fileName));
            System.out.println("📸 צילום מסך נשמר בכתובת: " + fileName);
        } catch (Exception e) {
            System.out.println("⚠️ נכשל ניסיון צילום המסך: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}*/
package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://www.btl.gov.il/");
    }

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            System.out.println("❌ הטסט נכשל: " + context.getDisplayName() + ". מבצע צילום מסך...");

            // צילום מסך - חייב לקרות לפני ה-quit
            takeScreenshot(context.getDisplayName());

            // סגירת הדפדפן לאחר הכישלון
            closeDriver();
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            System.out.println("✅ הטסט עבר בהצלחה: " + context.getDisplayName());
            closeDriver(); // סגירה לאחר הצלחה
        }

        private void closeDriver() {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    };

    private void takeScreenshot(String testName) {
        if (driver == null) return;
        try {
            // יצירת תיקיית screenshots אם היא לא קיימת
            Path path = Paths.get("screenshots");
            if (!Files.exists(path)) Files.createDirectories(path);

            // ביצוע הצילום
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // יצירת שם קובץ ייחודי עם תאריך ושעה
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshots/" + testName + "_" + timestamp + ".png";

            // שמירת הקובץ
            Files.copy(screenshot.toPath(), Paths.get(fileName));
            System.out.println("📸 צילום מסך נשמר בכתובת: " + fileName);
        } catch (Exception e) {
            System.out.println("⚠️ נכשל ניסיון צילום המסך: " + e.getMessage());
        }
    }

}