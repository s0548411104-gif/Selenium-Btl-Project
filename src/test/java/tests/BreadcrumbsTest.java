package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.BenefitsPage;

public class BreadcrumbsTest extends BaseTest {

    @ParameterizedTest
    @CsvSource({
            "דמי לידה, לידה",
            "קצבת ילדים, ילדים",
            "נכות, נכות",
            "אזרח ותיק, אזרח ותיק",
            "אבטלה, אבטלה"
    })
    public void testBenefitsNavigation(String linkText, String expectedTitle) {
        System.out.println("\n--- בודק: " + linkText + " ---");

        driver.get("https://www.btl.gov.il");
        driver.manage().window().maximize();

        BenefitsPage page = new BenefitsPage(driver);
        page.enterBenefitsSection();
        page.clickCategory(linkText);

        String actualTitle = page.getPageTitle();
        System.out.println("כותרת שנמצאה: " + actualTitle);

        Assertions.assertTrue(actualTitle.contains(expectedTitle),
                "הכותרת (" + actualTitle + ") לא הכילה את: " + expectedTitle);
    }
}