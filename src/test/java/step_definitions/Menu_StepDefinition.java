package step_definitions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.MenuActions;
import pages.MenuPage;
import utilities.BrowserUtils;
import utilities.ConfigurationReader;
import utilities.Driver;
import java.util.List;

public class Menu_StepDefinition {

    MenuPage menuPage = new MenuPage();
    WebDriver driver = Driver.getDriver();
    private String selectedCategory;

    // -------------------- Background --------------------
    @Given("I navigate to the Menu page")
    public void i_navigate_to_the_menu_page() {
    }

    @And("I accept all cookies")
    public void i_accept_all_cookies() {
        BrowserUtils.waitForVisibility(menuPage.acceptAllCookiesBtn, 5);
        BrowserUtils.clickWithJS(menuPage.acceptAllCookiesBtn);
    }

    // -------------------- Scenario 1 - All Categories--------------------
    @Then("I should see all category headings")
    public void i_should_see_all_category_headings() {
        List<WebElement> categories = menuPage.menuCategories;
        Assert.assertTrue("No categories found!", categories.size() > 0);

        System.out.println("Found categories: ");
        for (WebElement category : categories) {
            System.out.println(category.getText());
        }
    }

    @Then("I should see menu items listed for each category")
    public void i_should_see_menu_items_listed_for_each_category() {
        for (WebElement category : menuPage.menuCategories) {
            menuPage.scrollToCategory(category);
            BrowserUtils.waitFor(1);

            List<WebElement> visibleItems = BrowserUtils.getVisibleElements(menuPage.allMenuItems);
            Assert.assertTrue("No items found under category " + category.getText(),
                    visibleItems.size() > 0);
        }
    }

    // -------------------- Scenario 2 - Drinks & Snacks--------------------
    MenuActions menuActions = new MenuActions();

    @When("I click on the drinks and snacks category")
    public void i_click_on_drinks_and_snacks_category() {
        for (WebElement category : menuPage.menuCategories) {
            if (category.getText().equalsIgnoreCase("Drinks & Snacks")) {
                menuPage.scrollToCategory(category);
                BrowserUtils.waitForVisibility(category, 5);
                category.click();
                break;
            }
        }
    }

    @Then("I should see the Drinks & Snacks products displayed")
    public void i_should_see_drinks_and_snacks_products_displayed() {
        List<WebElement> products = BrowserUtils.getVisibleElements(menuPage.allMenuItems);
        Assert.assertTrue("No products found in Drinks & Snacks category!", products.size() > 0);
    }

    @When("I click on a {string} product")
    public void i_click_on_a_specific_product(String productName) {
        menuActions.clickProductFromCategory(selectedCategory, productName);
    }

    @And("I should see the product detail page with correct information")
    public void i_should_see_the_product_detail_page_with_correct_information() {
        menuActions.verifyProductDetails(); // Action class doğrulamayı içeriyor
    }


    // -------------------- Scenario 3 - Responsive design - Screen Size --------------------

    @Given("I open the Greggs Menu page with a screen size of {int} x {int}")
    public void i_open_the_greggs_menu_page_with_a_screen_size_of(int width, int height) {
        Driver.getDriver().manage().window().setSize(new Dimension(width, height));

        // get URL from configuration.properties
        String url = ConfigurationReader.getProperty("url");
        Driver.getDriver().get(url);
        BrowserUtils.waitForPageToLoad(3);
    }

    @Then("the menu should be displayed properly")
    public void the_menu_should_be_displayed_properly() {

        Assert.assertTrue("Menu categories are not visible!", !menuPage.menuCategories.isEmpty());
        Assert.assertTrue("Menu items are not visible!", !menuPage.allMenuItems.isEmpty());

    }

    // -------------------- Scenario 4 - accessibility --------------------
    @Then("images should have alt attributes")
    public void images_should_have_alt_attributes() {
        for (WebElement img : menuPage.productImages) {
            String altText = img.getAttribute("alt");
            Assert.assertNotNull("Alt attribute is missing on product image!", altText);
            Assert.assertFalse("Alt attribute is empty!", altText.trim().isEmpty());
        }
    }

    @Then("menu buttons should have aria-labels")
    public void menu_buttons_should_have_aria_labels() {
        for (WebElement button : menuPage.menuButtons) {
            String ariaLabel = button.getAttribute("aria-label");
            Assert.assertNotNull("Aria-label is missing on menu button!", ariaLabel);
            Assert.assertFalse("Aria-label is empty!", ariaLabel.trim().isEmpty());
        }
    }

}