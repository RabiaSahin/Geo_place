package pages;

import utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    public HomePage(){
        PageFactory.initElements(Driver.getDriver(), this);
    }
    @FindBy(xpath ="//input[@id='trial-input']")
    public WebElement nameInput;

    @FindBy(xpath = "//button[@aria-label='Check gender']")
    public WebElement searchButton;

    @FindBy(xpath = "//p[contains(@class,'text-center')]/b[2]")
    public WebElement predictedGender;

}
