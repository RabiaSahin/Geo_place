@api
Feature: Genderize.io API Functional and Edge-Case Tests

  # =====================
  # 1. Predicting the gender of a single name
  # =====================

  Scenario Outline: Predict gender of a single name
    Given the Genderize API is available
    When I send a GET request with the name "<name>"
    Then the response should contain gender "<expectedGender>"
    And the probability should be greater than 0
    And the count should be greater than 0

    Examples:
      | name   | expectedGender |
      | Peter  | male           |
      | Emily  | female         |
      | Alex   | male           |

  # =====================
  # 2. Predict gender using country-specific classification
  # =====================

  Scenario Outline: Predict gender with a country parameter
    Given the Genderize API supports country context
    When I send a GET request with name "<name>" and country code "<country>"
    Then the gender returned should be "<expectedGender>" based on "<country>"

    Examples:
      | name   | country | expectedGender |
      | Andrea | IT      | male           |
      | Andrea | US      | female         |
      | Elif   | TR      | female         |

  # =====================
  # 3. Checking the gender of multiple names in a single request
  # =====================

  Scenario: Predict gender of multiple names using batch request
    Given the following names are prepared
      | names  |
      | Peter  |
      | Lois   |
      | Stewie |
    When I send a batch GET request with those names
    Then the response should return gender information for each name
    And each result should include name, gender, probability and count

  # =====================
  # 4. Edge-case and boundary scenarios
  # =====================
  # Note:
  # This API returns HTTP status code 200 for all requests,
  # including those with invalid inputs that would typically return 4xx errors.
  # Therefore, scenarios expecting 4xx errors are implemented by checking
  # warning or error messages in the response body instead of status codes.
  # =======================

  Scenario Outline: Invalid name input should return a warning
    Given I send a GET request with the name "<invalidName>"
    Then the response should contain a warning for invalid name

    Examples:
      | invalidName |
      | 12345       |
      | @#$%        |
      |             |

  Scenario Outline: Invalid or unsupported country code
    Given I send a GET request with name "Peter" and country code "<code>"
    Then the response should indicate the country code is invalid or unsupported

    Examples:
      | code |
      | UKS  |
      | XX   |
      | ABC  |

  Scenario: Missing 'name' parameter returns a warning
    Given I send a GET request without the 'name' parameter
    Then the error message should include "Missing 'name' parameter"

 #Scenario: Missing API key returns a warning
    #Given I send a request without an API key
    #Then the error message should include "API key required"

  Scenario: Invalid API key returns a warning
    Given I send a request with an invalid API key
    Then the error message should include "Invalid API key"


  # =====================
  # 5. Boundary test for name length and batch size
  # =====================

  Scenario Outline: Name length boundary test
    Given I send a GET request with name "<testName>"
    Then if name length exceeds limit, response should contain a warning message

    Examples:
      | testName                 |
      | A                        |
      | <255-char-name>          |
      | <256-char-name>          |

  Scenario Outline: Batch request name count boundary
    Given I send a batch request with <sentCount> names
    Then the response should contain at most 10 results

    Examples:
      | sentCount |
      | 1         |
      | 10        |
      | 11        |

  # =====================
  # 6. Unicode and non-ASCII characters support
  # =====================

  Scenario Outline: Predict gender using international characters
    Given I send a GET request with name "<unicodeName>"
    When the request is processed
    Then the response status code should be <expectedStatus>

    Examples:
      | unicodeName | expectedStatus |
      | José        | 200            |
      | İsmail      | 200            |
      | 张伟         | 200            |
      | Анна        | 200            |

