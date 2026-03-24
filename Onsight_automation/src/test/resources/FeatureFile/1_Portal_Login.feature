Feature: 1_Portal_Login

  @Regression @Registration @Stage @WEB @Android
  Scenario: Verify to go to OnSight portal login screen
    Given I am at OnSight portal login screen
    And I verify Email field available on the page
    And I verify Password field available on the page
    And I verify Login button available on the page

  @Regression @Registration @Stage @WEB @Android
  Scenario: Verify to login web portal as admin
    Given I am at OnSight portal login screen
    And I login to the application with valid user
    And I verify I am at dashboard page

  @Regression @Registration @Stage @WEB @Android
  Scenario: Verify to add user in manage users list
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

  @Regression @Registration @Stage @WEB @Android
  Scenario: Verify to search added user in manage users list
    Given I am at OnSight portal login screen
    And I login to the application with valid user
    And I verify I am at dashboard page
    And I navigate to manage user screen
    And I Verify I am at manage user list
    And I apply "Email Address" filter for "ADDED_USER" value

  @Regression @Registration @Stage @WEB @Android
  Scenario: Verify to check active status of added user in manage users list
    Given I am at OnSight portal login screen
    And I login to the application with valid user
    And I verify I am at dashboard page
    And I navigate to manage user screen
    And I Verify I am at manage user list
    And I apply "Email Address" filter for "ADDED_USER" value
    And I scroll left side of the screen
    And I check IsActive status







