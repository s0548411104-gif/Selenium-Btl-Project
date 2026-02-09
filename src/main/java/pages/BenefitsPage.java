package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BenefitsPage extends BtlBasePage {

    @FindBy(xpath = "//a[contains(text(), 'קצבאות והטבות')]")
    private WebElement mainBenefitsLink;

    public BenefitsPage(WebDriver driver) {
        super(driver);
    }

    public void enterBenefitsSection() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(mainBenefitsLink)).click();
        } catch (Exception e) {
            System.out.println("❌ לא הצלחתי ללחוץ על 'קצבאות והטבות'");
        }
    }

    public void clickCategory(String categoryName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // זמן המתנה נדיב
        System.out.println("מחפש ולוחץ על: " + categoryName);

        try {
            // התיקון הגדול: מחכים שהאלמנט יהיה קיים וגלוי!
            // משתמשים ב-partialLinkText שהוא מעולה לחיפוש טקסט בקישורים
            WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(categoryName)));

            // גלילה למרכז האלמנט (למקרה שהוא למטה)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", link);
            Thread.sleep(500); // נשימה קצרה אחרי הגלילה

            link.click();
            System.out.println("✅ נלחץ בהצלחה: " + categoryName);

            // מחכים שהדף החדש ייטען (לפי הכותרת H1)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

        } catch (Exception e) {
            System.out.println("❌ נכשלתי בלחיצה על: " + categoryName);
            // נסיון אחרון עם XPath גנרי אם ה-LinkText נכשל
            try {
                WebElement link = driver.findElement(By.xpath("//a[contains(text(), '" + categoryName + "')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
            } catch (Exception ex) {}
        }
    }

    public String getPageTitle() {
        try {
            Thread.sleep(1000); // וידוא אחרון שהכותרת התעדכנה
            return driver.findElement(By.tagName("h1")).getText();
        } catch (Exception e) {
            return "";
        }
    }
}