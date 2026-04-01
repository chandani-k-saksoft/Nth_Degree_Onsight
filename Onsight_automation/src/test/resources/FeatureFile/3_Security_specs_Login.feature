Feature: 7_Security Specs

  Background:
    And I turn "ON" the wifi on the device
    And I made login mandatory
    And The user taps on the skip button on Marketing Page 1
# comment test
  @Regression @Security_Specs @Stage_mob @Android @MOBILE @WEB @TestDemo
  Scenario Outline: Login with OTP and verify dashboard for different user roles
    Given I login to the application with valid user
    And I verify I am at dashboard page
    And I click on "Manage users" portal link
    And I click on "Security Levels" portal button
    Then I update settings for "<role>" as "<menuItems>"
    When I login as "<email>" user
    Then I land to dashboard screen
    And I verify the navigation list as follows
      | <menuItems> |
    And I click on Ellipsis button
    And I click on "Logout" button
    And I click on "YES" button
    And I land to enter email screen
    Examples:
      | role     | email             | menuItems                                                                                         |
      | Guest    | testone@gmail.com | Job Updates, Lead Sheet, Onboarding, My Schedule                                                  |
      | Resource | testtwo@gmail.com | Job Updates, Lead Sheet, Onboarding, My Schedule, Promo Pictures, Travel Expenses, Updates Needed |
      |Lead     | testthree@gmail.com | Job Updates, Lead Sheet, Onboarding, My Schedule, Promo Pictures, Travel Expenses, Updates Needed,Field Issues                        |

