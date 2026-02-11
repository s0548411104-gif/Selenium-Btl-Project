package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import java.time.Duration;

public class SearchTests extends BaseTest {

    @Test
    public void testSearchFunctionality() {
        System.out.println("🚀 מתחיל טסט תרגיל 3: בדיקת פונקציונליות חיפוש");
        HomePage homePage = new HomePage(driver);
        String searchText = "חישוב סכום דמי לידה ליום";
        System.out.println("Step 1: מזין את המילים '" + searchText + "' בשדה החיפוש");
        homePage.performSearch(searchText);
        System.out.println("Step 2: ממתין לטעינת דף התוצאות ובודק את הכותרת");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement resultElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + searchText + "')]"))
        );
        Assertions.assertTrue(resultElement.isDisplayed(), "התוצאות לא מוצגות על המסך");
        System.out.println("✅ הטסט הסתיים בהצלחה!");
    }
}