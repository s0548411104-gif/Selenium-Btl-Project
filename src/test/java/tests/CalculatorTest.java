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

        homePage.clickMainMenu(MainMenu.DEMEI_BITUAH);
        homePage.clickSubMenu("דמי ביטוח לאומי");
        try { Thread.sleep(1500); } catch (Exception e) {}
        homePage.clickSubMenu("מחשבון לחישוב דמי הביטוח");

        CalculatorPage calcPage = new CalculatorPage(driver);

        calcPage.fillStepOne("תלמיד ישיבה");
        calcPage.fillStepTwo();

        boolean isCorrect = calcPage.verifyResults("48", "123.00", "171");

        Assertions.assertTrue(isCorrect, "החישוב שגוי או שהסכומים לא נמצאו");

    }
}