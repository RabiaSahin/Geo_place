package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.BrowserUtils;
import utilities.Driver;
import java.util.List;
import java.util.stream.Collectors;


public class MenuPage {
    public MenuPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // Cookie Accept All button
    @FindBy(xpath = "//button[@id='onetrust-accept-btn-handler']")
    public WebElement acceptAllCookiesBtn;

    // Menu Categories (horizontal scrollable menu)
    @FindBy(xpath = "//div[@class='overflow-hidden pl-3']")
    public List<WebElement> menuCategories;

    // All product cards on the page - Dynamic Link
    @FindBy(xpath = "//a[contains(@data-test-card,'')]")
    public List<WebElement> allMenuItems;

    // Product Detail Page Elements
    @FindBy(xpath = "//h1[contains(@class,'text-brand-primary-blue')]")
    public WebElement productTitle;

    @FindBy(xpath = "//img[contains(@decoding,'async')]")
    public WebElement productImage;

    @FindBy(xpath = "//div[contains(@class,'col-span-3 ')]")
    public WebElement productInfo;

    // All product images
    @FindBy(xpath = "//img[contains(@decoding,'async')]")
    public List<WebElement> productImages;

    // buttons with aria-label
    @FindBy(css = "button[aria-label]")
    public List<WebElement> menuButtons;


    // ---------- Helpers ----------

    // Scroll to a category
    public void scrollToCategory(WebElement category) {
        BrowserUtils.scrollToElement(category);
    }

    // Return the product
    public WebElement getProductFromCategory(String productName) {
        List<WebElement> productsInCategory = allMenuItems.stream()
                .filter(WebElement::isDisplayed)
                .collect(Collectors.toList());

        return productsInCategory.stream()
                .filter(p -> p.getText().trim().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(productName + " product not found"));
    }
}