package step_definitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.ConfigurationReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class Api_StepDefinition {

    Response response;
    String baseUrl = ConfigurationReader.getProperty("api.baseUrl");
    List<String> preparedNames;
    String currentInvalidName;

    // 1. Predicting the gender of a single name
    @Given("the Genderize API is available")
    public void the_genderize_api_is_available() {
        RestAssured.baseURI = baseUrl;
    }

    // Basic GET with name
    @When("I send a GET request with the name {string}")
    public void i_send_a_get_request_with_the_name(String name) {
        response = given().queryParam("name", name)
                .when().get();
    }

    // Gender assertion only
    @Then("the response should contain gender {string}")
    public void the_response_should_contain_gender(String expectedGender) {
        response.then().assertThat()
                .body("gender", equalToIgnoringCase(expectedGender));
    }

    // Separate probability check
    @Then("the probability should be greater than {double}")
    public void the_probability_should_be_greater_than(Double value) {
        response.then().assertThat()
                .body("probability", greaterThan(value.floatValue()));
    }

    // Separate count check
    @Then("the count should be greater than {int}")
    public void the_count_should_be_greater_than(Integer value) {
        response.then().assertThat()
                .body("count", greaterThan(value));
    }

    // 2. Predict gender using country-specific classification
    @Given("the Genderize API supports country context")
    public void the_genderize_api_supports_country_context() {
        RestAssured.baseURI = baseUrl;
    }

    @When("I send a GET request with name {string} and country code {string}")
    public void i_send_a_get_request_with_name_and_country_code(String name, String country) {
        send_request_with_name_and_country(name, country);
    }

    @Given("I send a GET request with name {string} and invalid country code {string}")
    public void i_send_a_get_request_with_name_and_invalid_country_code(String name, String country) {
        send_request_with_name_and_country(name, country);
    }

    //common method for the above 2 annotation
    private void send_request_with_name_and_country(String name, String country) {
        response = given()
                .queryParam("name", name)
                .queryParam("country_id", country)
                .when()
                .get();
    }

    @Then("the gender returned should be {string} based on {string}")
    public void the_gender_returned_should_be_based_on(String expectedGender, String country) {
        response.then().assertThat()
                .body("gender", equalToIgnoringCase(expectedGender));
    }

    // 3. Checking the gender of multiple names in a single request
    @Given("the following names are prepared")
    public void the_following_names_are_prepared(io.cucumber.datatable.DataTable dataTable) {
        preparedNames = dataTable.asList(String.class);
    }

    @When("I send a batch GET request with those names")
    public void i_send_a_batch_get_request_with_those_names() {
        String nameParams = preparedNames.stream()
                .map(n -> "name[]=" + n)
                .collect(Collectors.joining("&"));

        response = given()
                .when()
                .get(baseUrl + "?" + nameParams);
    }

    @Then("the response should return gender information for each name")
    public void the_response_should_return_gender_information_for_each_name() {
        response.then().assertThat().body("size()", greaterThan(0));
    }

    @Then("each result should include name, gender, probability and count")
    public void each_result_should_include_name_gender_probability_and_count() {
        List<Map<String, Object>> list = response.jsonPath().getList("");
        for (Map<String, Object> item : list) {
            assert item.containsKey("name");
            assert item.containsKey("gender");
            assert item.containsKey("probability");
            assert item.containsKey("count");
        }
    }

    // 4. Edge-case and boundary scenarios
    /* Note:
       This API returns HTTP status code 200 for all requests,
        including those with invalid inputs that would typically return 4xx errors.
        Therefore, scenarios expecting 4xx errors are implemented by checking
        warning or error messages in the response body instead of status codes.*/

    @Given("I send a request with the name {string}")
    public void i_send_a_request_with_the_name(String invalidName) {
        currentInvalidName = invalidName == null || invalidName.trim().isEmpty() ? "" : invalidName;
    }

    // Checks that for invalid names, the API returns no gender and zero probability
    @Then("the response should contain a warning for invalid name")
    public void the_response_should_contain_warning_for_invalid_name() {
        String gender = response.jsonPath().getString("gender");
        Double probability = response.jsonPath().getDouble("probability");

        assertTrue("Gender should be null or empty", gender == null || gender.isEmpty());
        assertTrue("Probability should be 0 or null", probability == null || probability == 0.0);
    }

    // Validates that an invalid or unsupported country code results in no gender prediction
    @Then("the response should indicate the country code is invalid or unsupported")
    public void the_response_should_indicate_invalid_country_code() {
        String gender = response.jsonPath().getString("gender");
        Float probability = response.jsonPath().getFloat("probability");

        assertTrue("Gender should be null or empty for invalid country", gender == null || gender.isEmpty());
        assertTrue("Probability should be 0 or null for invalid country", probability == null || probability == 0.0f);
    }

    // Missing name parameter
    @When("I send a GET request without the 'name' parameter")
    public void i_send_a_get_request_without_the_name_parameter() {
        response = given().when().get(baseUrl);
    }

    // No API key provided
    @When("I send a request without an API key")
    public void i_send_a_request_without_an_api_key() {
        response = given().when().get(baseUrl + "?name=John");
    }

    // Invalid API key
    @When("I send a request with an invalid API key")
    public void i_send_a_request_with_an_invalid_api_key() {
        response = given().queryParam("name", "John")
                .queryParam("apikey", "invalidkey")
                .when().get();
    }

    // Checks if the response contains the expected error message in the 'error' field
    @Then("the error message should include {string}")
    public void the_error_message_should_include(String expectedMessage) {
        String message = response.jsonPath().getString("error");

        assertNotNull("Error message should be present", message);
        assertTrue("Error message should contain expected text", message.contains(expectedMessage));
    }

    //5. Boundary test for name length and batch size
    // Checks if the response includes a warning message when the name length exceeds allowed limit
    @Then("if name length exceeds limit, response should contain a warning message")
    public void if_name_length_exceeds_limit_response_should_contain_warning_message() {
        String warningMessage = response.jsonPath().getString("warning");
        String name = response.jsonPath().getString("name");

        if (name != null && name.length() > 255) {
            assertNotNull("Warning message should be present for long name", warningMessage);
        } else {
            assertNull("No warning expected for valid name length", warningMessage);
        }
    }

    @Given("I send a batch request with {int} names")
    public void i_send_a_batch_request_with_names(Integer sentCount) {

        List<String> nameList = new ArrayList<>();

        for (int i = 1; i <= sentCount; i++) {
            nameList.add("Name" + i);
        }

        String namesParam = String.join(",", nameList);

        response = given()
                .baseUri(baseUrl)
                .queryParam("name", namesParam)
                .when()
                .get();
    }

    // Checks if the response contains a warning message when batch size exceeds the limit
    @Then("if batch size exceeds limit, response should contain a warning message")
    public void if_batch_size_exceeds_limit_response_should_contain_warning_message() {
        String warningMessage = response.jsonPath().getString("warning");

        // We expect a warning message if batch size is too large
        assertNotNull("Warning message should be present for large batch", warningMessage);
    }

    //6. Unicode and non-ASCII characters support

    @Given("I send a GET request with name {string}")
    public void i_send_a_get_request_with_name(String testName) {
        response = given()
                .baseUri(baseUrl)
                .queryParam("name", testName)
                .when()
                .get();
    }

    @When("the request is processed")
    public void the_request_is_processed() {

    }

    // Generic status code check
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer code) {
        response.then().assertThat().statusCode(code);
    }
}







