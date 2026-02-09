package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.CalculatorPage;
import pages.HomePage;
import pages.MainMenu;
import java.util.List;

public class CalculatorTest extends BaseTest {

    @Test
    public void testYeshivaStudentCalculation() {
        HomePage homePage = new HomePage(driver);

        // 1. ניווט לתפריט הראשי
        System.out.println("--- שלב 1: ניווט לדף דמי ביטוח ---");
        homePage.clickMainMenu(MainMenu.DEMEI_BITUAH);

        // 2. חיפוש הלינק למחשבון
        System.out.println("--- מחפש קישור למחשבון בדף... ---");
        boolean foundAndClicked = false;
        List<WebElement> allLinks = driver.findElements(By.tagName("a"));

        for (WebElement link : allLinks) {
            if (link.isDisplayed() && link.getText().contains("מחשבון") && link.getText().contains("דמי ביטוח")) {
                System.out.println(">>> מצאתי קישור מתאים: " + link.getText());
                try {
                    link.click();
                    foundAndClicked = true;
                    break;
                } catch (Exception e) {}
            }
        }

        // גיבוי למקרה שהחיפוש המדויק נכשל
        if (!foundAndClicked) {
            for (WebElement link : allLinks) {
                if (link.isDisplayed() && link.getText().contains("מחשבון")) {
                    link.click();
                    foundAndClicked = true;
                    break;
                }
            }
        }

        // 3. תהליך המחשבון
        System.out.println("--- הגענו למחשבון! מתחילים למלא ---");
        CalculatorPage calcPage = new CalculatorPage(driver);

        // שלב א': מילוי פרטים אישיים
        calcPage.fillStepOne("תלמיד ישיבה");

        // שלב ב': שאלת נכות (החלק שהיה חסר לך!)
        calcPage.fillStepTwo();

        // שלב ג': בדיקת תוצאות
        boolean isCorrect = calcPage.verifyResults("48", "123", "171");

        Assertions.assertTrue(isCorrect, "החישוב שגוי או שהסכומים לא נמצאו");

        System.out.println("✅ בדיקת המחשבון עברה בהצלחה!");
    }
}