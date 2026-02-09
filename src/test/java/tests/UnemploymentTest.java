package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.UnemploymentPage;

public class UnemploymentTest extends BaseTest {

    @Test
    public void testUnemploymentCalculation() {
        System.out.println("--- מתחיל תרגיל 7: מחשבון אבטלה ---");

        // 1. ניווט לקצבאות והטבות -> אבטלה
        // הערה: מכיוון שאין לנו Enum מסודר לזה, נעשה ניווט ישיר דרך ה-UI
        try {
            WebElement menu = driver.findElement(By.xpath("//a[contains(text(), 'קצבאות והטבות')]"));
            Actions actions = new Actions(driver);
            actions.moveToElement(menu).perform(); // רחף על התפריט
            Thread.sleep(1000);

            // לחיצה על אבטלה
            WebElement avtalaLink = driver.findElement(By.xpath("//a[contains(text(), 'אבטלה')]"));
            avtalaLink.click();
            System.out.println("✅ הגענו לדף אבטלה");

        } catch (Exception e) {
            System.out.println("⚠️ ניווט דרך תפריט נכשל, מנסה לינק ישיר...");
            driver.get("https://www.btl.gov.il/benefits/Unemployment/Pages/default.aspx");
        }

        // 2. הפעלת דף המחשבון
        UnemploymentPage avtalaPage = new UnemploymentPage(driver);

        // כניסה למחשבון
        avtalaPage.navigateToCalculator();

        // שלב 1: תאריך וגיל (ביקשו מעל 28, נשים 30)
        avtalaPage.fillStepOne("30");

        // שלב 2: מילוי שכר (ביקשו סכומים כרצוננו, נשים 10,000)
        avtalaPage.fillSalaries("10000");

        // בדיקה שהתוצאות מופיעות
        boolean resultsOk = avtalaPage.verifyResults();

        Assertions.assertTrue(resultsOk, "❌ לא כל נתוני התוצאות הופיעו בדף!");
    }
}