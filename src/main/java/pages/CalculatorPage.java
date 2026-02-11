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

    @FindBy(xpath = "//input[@value='המשך'] | //button[contains(., 'המשך')] | //input[@value='חישוב']")
    private WebElement continueButton;

    public CalculatorPage(WebDriver driver) {
        super(driver);
    }

    public void fillStepOne(String statusText) {
        try {
            WebElement statusLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[contains(., 'ישיבה')]")));
            scrollToElement(statusLabel);
            statusLabel.click();
        } catch (Exception e) {
                List<WebElement> radios = driver.findElements(By.xpath("//input[@type='radio']"));
                if (radios.size() >= 3) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();",  radios.get(2));
                }
        }

        try {
            WebElement genderMale = driver.findElement(By.xpath("//label[contains(., 'זכר')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", genderMale);
        } catch (Exception e) {
            System.out.println("ℹ️ לא נלחץ מין (אולי כבר מסומן)");
        }

        String randomDate = generateRandomDate();
        WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id, 'Date')]")));
            dateInput.clear();
            dateInput.sendKeys(randomDate);

        clickContinue();
    }

    public void fillStepTwo() {

        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "נכות"));

            WebElement noLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[normalize-space(.)='לא']")));
            scrollToElement(noLabel);
            noLabel.click();

        } catch (Exception e) {
                List<WebElement> radios = driver.findElements(By.xpath("//input[@type='radio']"));
                if (radios.size() >= 2) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radios.get(1));
                }

        }
        clickContinue();

    }

    public boolean verifyResults(String btlPrice, String healthPrice, String totalPrice) {
        try { Thread.sleep(2500); } catch (Exception e) {}
        String pageText = driver.findElement(By.tagName("body")).getText();

        return pageText.contains(btlPrice) &&
                pageText.contains(healthPrice) &&
                pageText.contains(totalPrice);
    }


    private void clickContinue() {

        try {
            scrollToElement(continueButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);
        } catch (Exception e) {
            continueButton.click();
        }
    }


    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);
    }

    private String generateRandomDate() {
        long minDay = LocalDate.now().minusYears(70).toEpochDay(); // שיניתי קצת טווח גילאים
        long maxDay = LocalDate.now().minusYears(19).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return String.format("%02d/%02d/%d", randomDate.getDayOfMonth(), randomDate.getMonthValue(), randomDate.getYear());
    }
}