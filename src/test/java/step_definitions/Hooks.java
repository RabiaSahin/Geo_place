package step_definitions;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ConfigurationReader;
import utilities.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.time.Duration;


public class Hooks {

    @Before
    public void setUp(){
        Actions actions;
        WebDriverWait wait;
        WebDriver driver = Driver.getDriver();
        driver.get(ConfigurationReader.getProperty("url"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        RestAssured.baseURI = ConfigurationReader.getProperty("api.baseUrl");
//        actions = new Actions(driver);
//        wait = new WebDriverWait(driver,Long.parseLong(ConfigurationReader.get("explicitWait")));
    }
    @After
    public void teardownScenario(Scenario scenario){

        //scenario.isFailed() --> if scenario fails this method will return TRUE boolean value

        if (scenario.isFailed()){
            byte [] screenshot = ((TakesScreenshot)Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());

        }

       //    BrowserUtils.sleep(3);
           Driver.closeDriver();

    }

    //    @BeforeStep
    public void setupStep(){
        System.out.println("--------> applying setup using @BeforeStep");
    }

    //    @AfterStep
    public void afterStep(){
        System.out.println("--------> applying tearDown using @AfterStep");
    }

}
