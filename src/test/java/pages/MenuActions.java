package pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.BrowserUtils;
import utilities.Driver;
import java.time.Duration;

public class MenuActions {

    private MenuPage menuPage = new MenuPage();
    private WebElement selectedProduct;


    public void clickProductFromCategory(String categoryName, String productName) {
        selectedProduct = menuPage.getProductFromCategory(productName);
        BrowserUtils.clickWithJS(selectedProduct);

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(menuPage.productTitle));
    }

    public void verifyProductDetails() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(menuPage.productTitle));
        wait.until(ExpectedConditions.visibilityOf(menuPage.productImage));
        wait.until(ExpectedConditions.visibilityOf(menuPage.productInfo));

        Assert.assertTrue("Product name is missing", !menuPage.productTitle.getText().isEmpty());
        Assert.assertTrue("Product image is missing", !menuPage.productImage.getAttribute("src").isEmpty());
        Assert.assertTrue("Product info is missing", !menuPage.productInfo.getText().isEmpty());
    }
}