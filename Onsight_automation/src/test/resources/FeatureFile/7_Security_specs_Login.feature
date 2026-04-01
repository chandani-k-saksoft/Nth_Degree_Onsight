Feature: 7_Security Specs


  @Regression @Security Specs @Stage_mob @Android @MOBILE
  Scenario Outline: Login with OTP and verify dashboard for different user roles
    Given I login to the application with valid user
    And I verify I am at dashboard page
    And I navigate to securityLevel screen
    Then I update settings for "<role>" as "menuItems>"
    When I login as "<email>" user
    Then I land to dashboard screen
    And user should see dashboard options "<menuItems>"
    Examples:
      | role      | email                 | menuItems|
      | guest     | testone@gmail.com    | Job Updates, Lead Sheet, Onboarding, My Schedule|
      | resource  | testtwo@gmail.com    | Job Updates, Lead , Onboarding, Promo Pictures, Field issues, My Schedule, Travel Expenses, Updates Needed |
      | lead      | testthree@gmail.com  | Job Updates, Lead Sheet, Onboarding, Promo Pictures, My Schedule, Travel Expenses                          |









