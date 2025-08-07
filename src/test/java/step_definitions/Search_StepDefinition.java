package step_definitions;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import utilities.BrowserUtils;
import utilities.Driver;
import java.time.Duration;


public class Search_StepDefinition {

    HomePage homePage = new HomePage();

    @Given("Navigate to the home page")
    public void navigate_to_the_home_page() {
    }

    @When("I enter the name {string} into inputbox")
    public void i_enter_the_name_into_inputbox(String name) {
        homePage.nameInput.sendKeys(name);
    }

    @And("Click the search button")
    public void click_the_search_button() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(homePage.searchButton));

        BrowserUtils.waitForMilliseconds(2000);
        homePage.searchButton.click();
    }

    @Then("I should see the predicted gender as {string}")
    public void i_should_see_the_predicted_gender_as(String expectedGender) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.textToBePresentInElement(homePage.predictedGender, expectedGender.toLowerCase()));

        String genderText = homePage.predictedGender.getText().trim().toLowerCase();
        Assert.assertEquals(expectedGender.toLowerCase(), genderText);
    }
}