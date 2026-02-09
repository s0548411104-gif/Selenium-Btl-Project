package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor; // הנה השורה שהייתה חסרה!
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        System.out.println("--- מנסה ללחוץ על: " + linkText + " ---");

        // שלב 1: פתיחת התפריט (Hover / Click)
        String menuXpath = String.format("//a[contains(text(), '%s')]", linkText);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement mainElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(menuXpath)));

        // אנחנו משתמשים ב-Actions כדי לרחף מעל וללחוץ
        org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.moveToElement(mainElement).click().perform();

        // שלב 2: בדיקה אם עברנו דף
        try {
            // מחכים שניה קטנה לראות אם משהו זז
            Thread.sleep(1500);

            // אם הכותרת השתנתה, מצוין!
            if (driver.getTitle().contains(menu.getExpectedTitle())) {
                System.out.println("✅ עברנו דף בהצלחה!");
                return;
            }

            // אם לא עברנו דף, סימן שהתפריט נפתח. עכשיו נלחץ על הכותרת בתוך התפריט שנפתח.
            // בדרך כלל יש קישור נוסף בתוך התפריט שנפתח עם אותו שם
            System.out.println("לא עברנו דף, מנסה ללחוץ על הקישור הפנימי בתפריט שנפתח...");

            // אנחנו מחפשים קישור *אחר* עם אותו טקסט שהוא כרגע גלוי (Visible)
            java.util.List<WebElement> links = driver.findElements(By.partialLinkText(linkText));

            for (WebElement link : links) {
                if (link.isDisplayed() && !link.equals(mainElement)) {
                    System.out.println("מצאתי קישור פנימי! לוחץ עליו...");
                    link.click();
                    return;
                }
            }

            // מוצא אחרון: לחיצה כפולה על הראשי
            System.out.println("מנסה דאבל קליק על הראשי...");
            actions.doubleClick(mainElement).perform();

        } catch (Exception e) {
            System.out.println("משהו השתבש בלחיצה.");
        }
    }
    public void clickSubMenu(String subMenuText) {
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement subMenu = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(subMenuText)));
        subMenu.click();
    }

    public void performSearch(String textToSearch) {
        wait.until(ExpectedConditions.visibilityOf(searchField));
        searchField.clear();
        searchField.sendKeys(textToSearch);
        searchButton.click();
    }

    public BranchesPage clickBranches() {
        branchesLink.click();
        return new BranchesPage(driver);
    }
}