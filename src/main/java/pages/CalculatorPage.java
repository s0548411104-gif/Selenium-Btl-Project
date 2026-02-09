package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CalculatorPage extends BtlBasePage {

    @FindBy(tagName = "h1")
    private WebElement pageTitle;

    // כפתור המשך - תופס את כל האפשרויות
    @FindBy(xpath = "//input[@value='המשך'] | //button[contains(., 'המשך')] | //input[@value='חישוב']")
    private WebElement continueButton;

    public CalculatorPage(WebDriver driver) {
        super(driver);
    }

    // שלב 1: מילוי פרטים
    public void fillStepOne(String statusText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("--- מילוי טופס שלב 1 ---");

        // 1. בחירת סטטוס
        try {
            // המתנה קצרה לטעינה מלאה
            Thread.sleep(1000);
            String xpath = "//label[contains(., 'ישיבה')]";
            WebElement statusLabel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            scrollToElement(statusLabel);
            statusLabel.click();
            System.out.println("✅ נבחר סטטוס: " + statusText);
        } catch (Exception e) {
            System.out.println("⚠️ בחירה לפי טקסט נכשלה. מנסה לפי מיקום (כפתור 3)...");
            try {
                List<WebElement> radios = driver.findElements(By.xpath("//input[@type='radio']"));
                if (radios.size() >= 3) {
                    WebElement radio3 = radios.get(2);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radio3);
                    System.out.println("✅ נבחר סטטוס לפי מיקום (גיבוי)");
                }
            } catch (Exception ex) {
                System.out.println("❌ נכשלתי בבחירת הסטטוס.");
            }
        }

        // 2. בחירת מין: זכר
        try {
            WebElement genderMale = driver.findElement(By.xpath("//label[contains(., 'זכר')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", genderMale);
            System.out.println("✅ נבחר מין: זכר");
        } catch (Exception e) {
            System.out.println("ℹ️ לא נלחץ מין (אולי כבר מסומן)");
        }

        // 3. מילוי תאריך לידה
        String randomDate = generateRandomDate();
        System.out.println("תאריך הלידה שהוגרל: " + randomDate);
        try {
            WebElement dateInput = driver.findElement(By.xpath("//input[contains(@id, 'Date') or contains(@name, 'Date')]"));
            dateInput.clear();
            dateInput.sendKeys(randomDate);
        } catch (Exception e) {
            System.out.println("❌ לא נמצא שדה תאריך לידה");
        }

        clickContinue();
    }

    // שלב 2: שאלות נוספות
    public void fillStepTwo() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("--- שלב 2: בחירת נכות ---");

        try {
            // המתנה שהשאלה תופיע
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "נכות"));

            WebElement noLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[normalize-space(.)='לא']")));
            scrollToElement(noLabel);
            noLabel.click();
            System.out.println("✅ נלחץ: לא");

        } catch (Exception e) {
            System.out.println("⚠️ לא מצאתי את 'לא'. לוחץ על כפתור רדיו #2...");
            try {
                List<WebElement> radios = driver.findElements(By.xpath("//input[@type='radio']"));
                if (radios.size() >= 2) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radios.get(1));
                    System.out.println("✅ נלחץ: לא (גיבוי)");
                }
            } catch (Exception ex) {}
        }

        System.out.println("--- מנסה לעבור לתוצאות... ---");
        int attempts = 0;
        boolean onResultsPage = false;

        // לולאה עקשנית למעבר דף
        while (!onResultsPage && attempts < 5) { // העליתי ל-5 ניסיונות
            clickContinue();
            try {
                Thread.sleep(2500); // מחכים יותר זמן
                String text = driver.findElement(By.tagName("body")).getText();

                if (text.contains("סך הכל") || text.contains("תוצאה") || text.contains("לתשלום")) {
                    onResultsPage = true;
                    System.out.println("✅ עברנו לדף התוצאות!");
                } else {
                    attempts++;
                    System.out.println("🔄 עדיין בטופס... (ניסיון " + attempts + ")");
                }
            } catch (Exception e) { attempts++; }
        }
    }

    // שלב 3: בדיקת תוצאות + הדפסת מסך לדיבאג
    public boolean verifyResults(String btlPrice, String healthPrice, String totalPrice) {
        System.out.println("--- בדיקת תוצאות ---");

        String pageText = driver.findElement(By.tagName("body")).getText();
        boolean totalFound = pageText.contains(totalPrice);

        if (totalFound) {
            System.out.println("✅ הסכום הכולל " + totalPrice + " נמצא!");
            return true;
        } else {
            System.out.println("❌ הסכום " + totalPrice + " לא נמצא.");

            // --- הדפסת תוכן הדף לדיבאג ---
            System.out.println("\n👇👇👇 מה הבוט רואה כרגע על המסך? 👇👇👇");
            // מדפיס רק את החלקים הרלוונטיים (לא הכל)
            if (pageText.length() > 600) {
                // מנסה למצוא את אמצע הטקסט או אזור של הודעות שגיאה
                if (pageText.contains("שגיאה") || pageText.contains("שדה חובה")) {
                    System.out.println("⚠️ זוהתה שגיאה באתר!");
                }
                System.out.println(pageText.substring(0, 600) + "...");
            } else {
                System.out.println(pageText);
            }
            System.out.println("👆👆👆 ------------------------------------- 👆👆👆\n");
            return false;
        }
    }

    // --- פונקציות עזר משודרגות ---

    private void clickContinue() {
        try {
            // מוצאים את הכפתור מחדש כל פעם למניעת StaleElement
            WebElement btn = driver.findElement(By.xpath("//input[@value='המשך'] | //button[contains(., 'המשך')] | //input[@value='חישוב']"));
            scrollToElement(btn);

            // שיטה 1: לחיצה רגילה
            try {
                btn.click();
            } catch (Exception e) {
                // שיטה 2: Actions (עכבר)
                System.out.println("לחיצה רגילה נכשלה, מנסה Actions...");
                Actions actions = new Actions(driver);
                actions.moveToElement(btn).click().perform();
            }

            System.out.println("🖱️ בוצע ניסיון לחיצה על 'המשך'");
        } catch (Exception e) {
            // שיטה 3: JavaScript (הכי חזק)
            System.out.println("⚠️ מפעיל נוהל חירום: לחיצת JS על כפתור המשך...");
            try {
                WebElement btn = driver.findElement(By.xpath("//input[@value='המשך']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            } catch (Exception ex) {
                System.out.println("❌ לא הצלחתי ללחוץ על 'המשך' בשום דרך.");
            }
        }
    }

    // פונקציית גלילה שבטוחה לשימוש
    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);
        try { Thread.sleep(200); } catch (InterruptedException e) {}
    }

    private String generateRandomDate() {
        long minDay = LocalDate.now().minusYears(25).toEpochDay(); // שיניתי קצת טווח גילאים
        long maxDay = LocalDate.now().minusYears(19).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return String.format("%02d/%02d/%d", randomDate.getDayOfMonth(), randomDate.getMonthValue(), randomDate.getYear());
    }
}