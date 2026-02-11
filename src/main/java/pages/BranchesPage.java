package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BranchesPage extends BtlBasePage {

    @FindBy(tagName = "h1")
    private WebElement pageTitle;

    public BranchesPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageTitleCorrect() {
        return pageTitle.getText().contains("סניפים");
    }

    public BranchInfoPage clickSpecificBranch(String branchName) {

        WebElement branchLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(branchName)));
        branchLink.click();

        return new BranchInfoPage(driver);
    }
}