package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.BenefitsPage;
import pages.UnemploymentPage;

public class UnemploymentTest extends BaseTest {

    @Test
    public void testUnemploymentCalculation() {
        BenefitsPage benefitsPage = new BenefitsPage(driver);
        benefitsPage.enterBenefitsSection();
        benefitsPage.clickCategory("אבטלה");

        UnemploymentPage unemploymentPage = new UnemploymentPage(driver);

        unemploymentPage.navigateToCalculator();

        unemploymentPage.fillStepOne();

        unemploymentPage.fillSalaries("12000");

        boolean isDataVisible = unemploymentPage.verifyResults();
        Assertions.assertTrue(isDataVisible, "חלק מנתוני החישוב הנדרשים לא הופיעו בדף התוצאות!");
    }
}