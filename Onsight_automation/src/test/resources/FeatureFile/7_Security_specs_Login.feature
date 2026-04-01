Feature: 7_Security Specs


  @Regression @Security Specs @Stage_mob @Android @MOBILE @Demo2
  Scenario Outline: Login with OTP and verify dashboard for different user roles
    When I login to portal as admin
    Then I update settings for "<role>" as "menuItems>"
    When I login as "<email>" user
    Then I land to dashboard screen
    And user should see dashboard options "<menuItems>"
    Examples:
      | role      | email                 | menuItems|
      | guest     | testone@gmail.com    | Job Updates, Lead Sheet, Onboarding, My Schedule|
      | resource  | testtwo@gmail.com    | Job Updates, Lead , Onboarding, Promo Pictures, Field issues, My Schedule, Travel Expenses, Updates Needed |
      | lead      | testthree@gmail.com  | Job Updates, Lead Sheet, Onboarding, Promo Pictures, My Schedule, Travel Expenses                          |









