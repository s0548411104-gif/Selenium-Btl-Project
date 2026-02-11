package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UnemploymentPage extends BtlBasePage {

    @FindBy(xpath = "//a[contains(text(), 'חישוב דמי אבטלה')]")
    private WebElement calcLink;

    @FindBy(xpath = "//input[@value='המשך'] | //button[contains(., 'המשך')]")
    private WebElement continueButton;

    public UnemploymentPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToCalculator() {
        WebElement mainLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'מחשבוני דמי אבטלה') or contains(text(), 'מחשבונים')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mainLink);

        wait.until(ExpectedConditions.elementToBeClickable(calcLink)).click();
    }

    public void fillStepOne() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        String stopDate = oneMonthAgo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id, 'Date')]")));
        dateInput.clear();
        dateInput.sendKeys(stopDate);

        WebElement over28Option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[contains(., 'מעל 28')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", over28Option);

        clickContinue();
    }

    public void fillSalaries(String salaryAmount) {
        try { Thread.sleep(1500); } catch (Exception e) {}
        List<WebElement> salaryInputs = driver.findElements(By.xpath("//input[@type='text']"));
        for (WebElement input : salaryInputs) {
            if (input.isDisplayed() && input.isEnabled()) {
                input.clear();
                input.sendKeys(salaryAmount);
            }
        }
        clickContinue();
    }

    public boolean verifyResults() {
        try { Thread.sleep(2000); } catch (Exception e) {}
        String pageText = driver.findElement(By.tagName("body")).getText();

        return pageText.contains("שכר יומי ממוצע") &&
                pageText.contains("דמי אבטלה ליום") &&
                (pageText.contains("דמי אבטלה לחודש") || pageText.contains("סה\"כ לחודש"));
    }

    private void clickContinue() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);
    }
}