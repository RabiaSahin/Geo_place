package step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class Api_StepDefinition {

    Response response;

    @Given("I send a request with name {string}")
    public void sendRequestWithName(String name) {
        response = given()
                .queryParam("name", name)
                .when()
                .get("https://api.genderize.io");
    }

    @Given("I send a request with name {string} and country code {string}")
    public void sendRequestWithCountry(String name, String countryId) {
        response = given()
                .queryParam("name", name)
                .queryParam("country_id", countryId)
                .when()
                .get("https://api.genderize.io");
    }

    /*@Then("The gender should be {string}")
    public void genderShouldBe(String expectedGender) {
        String actualGender = response.jsonPath().getString("gender");
        if (actualGender != null) {
            actualGender = actualGender.trim();
        }
        assertEquals(expectedGender.toLowerCase(), actualGender != null ? actualGender.toLowerCase() : null);
    }*/
    @Then("The gender should be {string}")
    public void genderShouldBe(String expectedGender) {
        String actualGender = response.jsonPath().getString("gender");

        if ("null".equalsIgnoreCase(expectedGender)) {
            assertEquals(null, actualGender);
        } else {
            assertEquals(expectedGender.toLowerCase(), actualGender.trim().toLowerCase());
        }
    }

    @Given("I send a request with names {string}")
    public void sendRequestWithMultipleNames(String namesCSV) {
        response = given()
                .queryParam("name[]", namesCSV.split(",\\s*"))
                .when()
                .get("https://api.genderize.io");
    }

    @Then("I should get {int} results")
    public void iShouldGetMultipleResults(int expectedCount) {
        List<Map<String, Object>> resultList = response.jsonPath().getList("$");
        assertEquals(expectedCount, resultList.size());
    }

    @Then("The API should return status code {int}")
    public void verifyStatusCode(int statusCode) {
        int actualStatusCode = response.getStatusCode();

        // information log
        System.out.println("Actual Status Code: " + actualStatusCode);
        System.out.println("Response Body: " + response.asString());
        assertEquals(statusCode, actualStatusCode);
    }
}
