package runners;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = { "html:target/failed-html-report", "json:target/failedcucumber.json"},
        glue = "step_definitions",
        features = "@target/rerun.txt"
)
public class FailedTestRunner {
}
