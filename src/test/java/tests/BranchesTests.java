package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.BranchInfoPage;
import pages.BranchesPage;
import pages.HomePage;

public class BranchesTests extends BaseTest {

    // טסט 1: בדיקה שאפשר להיכנס לדף "סניפים וערוצי שירות" (מה שהיה לך קודם)
    @Test
    public void testNavigateToBranches() {
        HomePage homePage = new HomePage(driver);

        BranchesPage branchesPage = homePage.clickBranches();

        boolean isPageCorrect = branchesPage.isPageTitleCorrect();

        Assertions.assertTrue(isPageCorrect, "הכותרת בדף הסניפים לא תקינה - כנראה שלא עברנו דף");

        System.out.println("בדיקה 1 עברה: הגענו לדף הסניפים הראשי");
    }

    // טסט 2: בדיקה שאפשר להיכנס לסניף ספציפי (למשל ירושלים) ולראות פרטים (החדש!)
    @Test
    public void testBranchDetails() {
        HomePage homePage = new HomePage(driver);

        // 1. כניסה לדף סניפים
        BranchesPage branchesPage = homePage.clickBranches();

        // 2. בחירת סניף ספציפי (ירושלים)
        BranchInfoPage infoPage = branchesPage.clickSpecificBranch("ירושלים");

        // 3. בדיקה שכל המידע (כתובת, קבלת קהל, טלפון) מוצג
        Assertions.assertTrue(infoPage.isAddressDisplayed(), "חסר מידע על כתובת בסניף");
        Assertions.assertTrue(infoPage.isReceptionDisplayed(), "חסר מידע על קבלת קהל בסניף");
        Assertions.assertTrue(infoPage.isPhoneInfoDisplayed(), "חסר מידע על מענה טלפוני בסניף");

        System.out.println("בדיקה 2 עברה: כל פרטי הסניף הוצגו בהצלחה!");
    }
}