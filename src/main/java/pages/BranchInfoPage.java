package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BranchInfoPage extends BtlBasePage {

    // אנחנו מחפשים כל אלמנט בדף שיש בו את הטקסט הזה
    @FindBy(xpath = "//*[contains(text(), 'כתובת')]")
    private WebElement addressTitle;

    @FindBy(xpath = "//*[contains(text(), 'קבלת קהל')]")
    private WebElement receptionTitle;

    @FindBy(xpath = "//*[contains(text(), 'מענה טלפוני')]")
    private WebElement phoneTitle;

    public BranchInfoPage(WebDriver driver) {
        super(driver);
    }

    // פונקציות שבודקות: האם הטקסט הזה מופיע על המסך?
    public boolean isAddressDisplayed() {
        return addressTitle.isDisplayed();
    }

    public boolean isReceptionDisplayed() {
        return receptionTitle.isDisplayed();
    }

    public boolean isPhoneInfoDisplayed() {
        return phoneTitle.isDisplayed();
    }
}