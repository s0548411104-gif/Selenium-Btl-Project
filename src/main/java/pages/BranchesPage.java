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

    // --- הנה השינוי החשוב לשלב 5! ---
    // הוספנו פונקציה שיודעת לקבל שם של סניף וללחוץ עליו
    public BranchInfoPage clickSpecificBranch(String branchName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // אנחנו מחפשים קישור שיש בו את שם הסניף (למשל "ירושלים")
        WebElement branchLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(branchName)));
        branchLink.click();

        // הפונקציה מחזירה לנו את הדף החדש שנפתח (דף פרטי הסניף)
        return new BranchInfoPage(driver);
    }
}