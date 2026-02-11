package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.BranchInfoPage;
import pages.BranchesPage;
import pages.HomePage;

public class BranchesTests extends BaseTest {

    @Test
    public void testNavigateToBranches() {
        HomePage homePage = new HomePage(driver);

        BranchesPage branchesPage = homePage.clickBranches();

        boolean isPageCorrect = branchesPage.isPageTitleCorrect();

        Assertions.assertTrue(isPageCorrect, "הכותרת בדף הסניפים לא תקינה - כנראה שלא עברנו דף");

    }

    @Test
    public void testBranchDetails() {
        HomePage homePage = new HomePage(driver);

        BranchesPage branchesPage = homePage.clickBranches();

        BranchInfoPage infoPage = branchesPage.clickSpecificBranch("ירושלים");

        Assertions.assertAll("אימות הצגת פרטי סניף",
                () -> Assertions.assertTrue(infoPage.isAddressDisplayed(), "חסר מידע על כתובת הסניף"),
                () -> Assertions.assertTrue(infoPage.isReceptionDisplayed(), "חסר מידע על קבלת קהל בסניף"),
                () -> Assertions.assertTrue(infoPage.isPhoneInfoDisplayed(), "חסר מידע על מענה טלפוני בסניף")
        );
    }
}