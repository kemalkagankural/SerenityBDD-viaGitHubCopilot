Feature: Search product in Amazon
  @search
  Scenario: Searching a product in Amazon
    Given I am on the Amazon home page
    When I search for "iPhone"
    Then I should see "iPhone" in the search results

