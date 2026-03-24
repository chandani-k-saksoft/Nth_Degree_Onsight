Feature: 7_Security Specs

  @Regression @Registration @Stage @WEB @Android
  Scenario: Verify security specs allowed module
    Given I am at OnSight portal login screen
    And I login to the application with valid user
    And I verify I am at dashboard page
    And I navigate to manage user screen
    And I Verify I am at manage user list
    And I navigate to add user screen
    And I enter given detail for user
      | fullName    | firstName | lastName | mobileNo   | email                        |
      | RANDOM_Name | Automated | Random   | 7349771666 | OnSight_RANDOM@nthdegree.com |
    And I select "Lead" as "securityLevelDropdown" dropdown
    And I select "IND +91" as "countryCodeDropdown" dropdown
    And I click on add user button
    And I Verify I am at manage user list
    #And I Verify I am at manage user list








