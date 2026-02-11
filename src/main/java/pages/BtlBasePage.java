package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor; // הנה השורה שהייתה חסרה!
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BtlBasePage extends BasePage {

    @FindBy(id = "TopQuestions")
    private WebElement searchField;

    @FindBy(id = "ctl00_SiteHeader_reserve_btnSearch")
    private WebElement searchButton;

    @FindBy(id = "ctl00_Topmneu_BranchesHyperLink")
    private WebElement branchesLink;


    public BtlBasePage(WebDriver driver) {
        super(driver);
    }

    public void clickMainMenu(MainMenu menu) {
        String linkText = menu.getLinkText();

        String menuXpath = String.format("//a[contains(text(), '%s')]", linkText);
        WebElement mainElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(menuXpath)));

        Actions actions = new Actions(driver);
        actions.moveToElement(mainElement).click().perform();

            if (driver.getTitle().contains(menu.getExpectedTitle())) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(linkText))).click();
            }
    }
    public void clickSubMenu(String subMenuText) {
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(subMenuText))).click();
    }

    public void performSearch(String textToSearch) {
        wait.until(ExpectedConditions.visibilityOf(searchField));
        searchField.clear();
        searchField.sendKeys(textToSearch);
        searchButton.click();
    }

    public BranchesPage clickBranches() {
        wait.until(ExpectedConditions.elementToBeClickable(branchesLink)).click();
        return new BranchesPage(driver);
    }
}