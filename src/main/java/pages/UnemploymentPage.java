package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UnemploymentPage extends BtlBasePage {

    @FindBy(xpath = "//a[contains(text(), 'חישוב דמי אבטלה')]")
    private WebElement calcLink;

    @FindBy(xpath = "//input[@value='המשך'] | //button[contains(., 'המשך')] | //input[@value='חישוב']")
    private WebElement continueButton;

    public UnemploymentPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToCalculator() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("--- ניווט למחשבון אבטלה ---");
        try {
            WebElement mainLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'מחשבוני דמי אבטלה') or contains(text(), 'מחשבונים')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mainLink);
        } catch (Exception e) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(calcLink)).click();
            System.out.println("✅ נכנסנו למחשבון חישוב סכום.");
        } catch (Exception e) {
            System.out.println("❌ לא הצלחתי להיכנס למחשבון.");
        }
    }

    public void fillStepOne(String ignoredAge) {
        // ההערה שלך: הפרמטר ignoredAge לא בשימוש כי אנחנו בוחרים קבוע "מעל 28" לפי ההוראות
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("--- שלב 1: מילוי פרטים אישיים ---");

        // 1. תאריך הפסקת עבודה (חודש אחורה)
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        String stopDate = oneMonthAgo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        try {
            // מילוי התאריך
            WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id, 'Date')]")));
            dateInput.clear();
            dateInput.sendKeys(stopDate);
            System.out.println("✅ תאריך הפסקת עבודה: " + stopDate);
        } catch (Exception e) {
            System.out.println("❌ לא נמצא שדה תאריך הפסקת עבודה.");
        }

        // 2. בחירת גיל: "מעל 28" (כפתור רדיו)
        try {
            // התיקון הגדול: לחיצה על הלייבל "מעל 28" במקום לנסות להקליד מספר
            WebElement over28Option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[contains(., 'מעל 28')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", over28Option);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", over28Option);
            System.out.println("✅ נבחרה אפשרות: מעל 28");
        } catch (Exception e) {
            System.out.println("⚠️ לא הצלחתי ללחוץ על 'מעל 28'. מנסה כפתור רדיו שני...");
            try {
                // גיבוי: כפתור הרדיו השני (אינדקס 1)
                List<WebElement> radios = driver.findElements(By.xpath("//input[@type='radio']"));
                if (radios.size() >= 2) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radios.get(1));
                    System.out.println("✅ נבחר כפתור רדיו #2 (גיבוי)");
                }
            } catch (Exception ex) {
                System.out.println("❌ נכשלתי בבחירת גיל.");
            }
        }

        clickContinue();
    }

    public void fillSalaries(String salaryAmount) {
        System.out.println("--- שלב 2: מילוי משכורות ---");

        // נותנים לטבלה רגע להיטען
        try { Thread.sleep(2000); } catch (Exception e) {}

        // מוצאים את כל שדות הטקסט בדף
        List<WebElement> salaryInputs = driver.findElements(By.xpath("//input[@type='text']"));

        int count = 0;

        // עוברים על *כל* השדות שמצאנו (בלי הגבלה של 6)
        for (WebElement input : salaryInputs) {
            // בודקים שהשדה מוצג וניתן לעריכה
            if (input.isDisplayed() && input.isEnabled()) {
                try {
                    // גלילה עדינה אל השדה כדי לוודא שהוא במסך
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

                    input.clear();
                    input.sendKeys(salaryAmount);
                    count++;
                } catch (Exception e) {
                    System.out.println("⚠️ דילגתי על שדה אחד בעייתי.");
                }
            }
        }
        System.out.println("✅ מולאו " + count + " שדות שכר (כל השדות שנמצאו בטבלה).");

        clickContinue();
    }

    public boolean verifyResults() {
        System.out.println("--- בדיקת תוצאות ---");
        try { Thread.sleep(3000); } catch (Exception e) {}

        String pageText = driver.findElement(By.tagName("body")).getText();

        boolean check1 = pageText.contains("שכר יומי ממוצע");
        boolean check2 = pageText.contains("דמי אבטלה ליום");

        // בדיקה רחבה יותר למושג החודשי
        boolean check3 = pageText.contains("דמי אבטלה לחודש") ||
                pageText.contains("סה\"כ לחודש") ||
                pageText.contains("סה\"כ דמי אבטלה");

        if (check1 && check2 && check3) {
            System.out.println("✅ כל הנתונים נמצאו!");
            return true;
        } else {
            System.out.println("❌ חסרים נתונים.");
            System.out.println("נמצא שכר יומי? " + check1);
            System.out.println("נמצא ליום? " + check2);
            System.out.println("נמצא לחודש? " + check3);

            // דיבאג למקרה של כישלון
            if (!check3) {
                System.out.println("\n👇👇👇 טקסט מהאתר (חפשי את הסכום החודשי) 👇👇👇");
                if (pageText.length() > 600) {
                    int start = Math.max(0, pageText.indexOf("תוצאות"));
                    System.out.println(pageText.substring(start, Math.min(start + 800, pageText.length())));
                } else {
                    System.out.println(pageText);
                }
                System.out.println("👆👆👆 ------------------------------------- 👆👆👆\n");
            }

            return false;
        }
    }

    private void clickContinue() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            btn.click();
            System.out.println("🖱️ נלחץ כפתור המשך");
        } catch (Exception e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);
            } catch (Exception ex) {}
        }
    }
}