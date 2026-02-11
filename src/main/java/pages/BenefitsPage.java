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
        wait.until(ExpectedConditions.elementToBeClickable(mainBenefitsLink)).click();
    }

    public void clickCategory(String categoryName) {

        try {
            WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(categoryName)));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", link);
            Thread.sleep(500);

            link.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

        } catch (Exception e) {
                WebElement link = driver.findElement(By.xpath("//a[contains(text(), '" + categoryName + "')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }
    }

    public String getBreadcrumbsText() {
        try {
            WebElement breadcrumbs = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//ul[contains(@class,'breadcrumb')] | //nav[@aria-label='מורי דרך']")
            ));
            return breadcrumbs.getText();
        } catch (Exception e) {
            return driver.findElement(By.tagName("h1")).getText();
        }
    }
    public String getPageIdentifier() {
        try {
            return wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
                    org.openqa.selenium.By.className("breadcrumb")
            )).getText();
        } catch (Exception e) {
            return driver.findElement(org.openqa.selenium.By.tagName("h1")).getText();
        }
    }
}