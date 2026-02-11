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
        System.out.println("🚀 מתחיל טסט: חישוב דמי ביטוח תלמיד ישיבה");
        HomePage homePage = new HomePage(driver);

        homePage.clickMainMenu(MainMenu.DEMEI_BITUAH);
        System.out.println("Step 1: נכנסנו לתפריט דמי ביטוח");
        homePage.clickSubMenu("דמי ביטוח לאומי");
        try { Thread.sleep(1500); } catch (Exception e) {}
        homePage.clickSubMenu("מחשבון לחישוב דמי הביטוח");
        System.out.println("Step 2: הגענו למחשבון");
        CalculatorPage calcPage = new CalculatorPage(driver);

        calcPage.fillStepOne("תלמיד ישיבה");
        calcPage.fillStepTwo();
        System.out.println("Step 3: מילוי טפסים הסתיים");
        boolean isCorrect = calcPage.verifyResults("48", "123.00", "171");
        System.out.println("Step 4: בדיקת תוצאות סופיות");
        Assertions.assertTrue(isCorrect, "החישוב שגוי או שהסכומים לא נמצאו");
        System.out.println("✅ הטסט הסתיים בהצלחה");
    }
}