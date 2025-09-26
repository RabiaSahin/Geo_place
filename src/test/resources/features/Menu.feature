@ui
Feature: Greggs Menu Page Validation
  As a user
  I want to view the Greggs menu page
  So that I can see categories and product information correctly

  Background:
    Given I navigate to the Menu page
    And I accept all cookies

  Scenario: Verify all categories and menu items are listed
    Then I should see all category headings
    And I should see menu items listed for each category

  Scenario: Verify Drinks & Snacks category can be selected
    When I click on the drinks and snacks category
    Then I should see the Drinks & Snacks products displayed
    When I click on a "Regular Latte" product
    And I should see the product detail page with correct information


  Scenario Outline: Verify menu responsive design on different screen sizes
    Given I open the Greggs Menu page with a screen size of <width> x <height>
    Then the menu should be displayed properly

    Examples:
      | width | height |
      | 1920  | 1080   |
      | 1366  | 768    |
      | 375   | 667    |
      | 414   | 896    |

  Scenario: Check accessibility features on the menu page
    Then images should have alt attributes
    And menu buttons should have aria-labels