Feature: 2_Mobile_Login
  As a new or existing user
  I want to navigate from the marketing pages to the dashboard
  So that I can access the core features of the Nth Degree application

  Background:
    And I turn "ON" the wifi on the device


   # Marketing Page 1
  # --------------------------------------------------

  @Regression @Login @Stage_mob @Android @MOBILE
  Scenario: Verify Marketing Page 1 UI elements
    Then Marketing Page 1 should be displayed
    And The app logo should be visible
    And The marketing banner image should be displayed
    And The Next button should be enabled
    And The Skip button should be visible

    # --------------------------------------------------
  # Marketing Page 2
  # --------------------------------------------------

  @Regression @Login @Stage_mob @Android @MOBILE
  Scenario: Navigate from Marketing Page 1 to Marketing Page 2 using Next button
    Then Marketing Page 1 should be displayed
    When The user taps on the Next button on Marketing Page 1
    Then Marketing Page 2 should be displayed


  # --------------------------------------------------
  # Marketing Page 3
  # --------------------------------------------------

  @Regression @Login @Stage_mob @Android @MOBILE
  Scenario: Navigate from Marketing Page 2 to Marketing Page 3
    Then Marketing Page 1 should be displayed
    When The user taps on the Next button on Marketing Page 1
    Then Marketing Page 2 should be displayed
    When The user taps on the Next button on Marketing Page 2
    Then Marketing Page 3 should be displayed

    # --------------------------------------------------
  # Login Screen
  # --------------------------------------------------
  @Regression @Login @Stage_mob @Android @MOBILE
  Scenario: Navigate from Marketing Page 3 to email page
    Then Marketing Page 1 should be displayed
    When The user taps on the Next button on Marketing Page 1
    Then Marketing Page 2 should be displayed
    When The user taps on the Next button on Marketing Page 2
    Then Marketing Page 3 should be displayed
    When The user taps on the Next button on Marketing Page 3
    Then I land to enter email screen

  @Regression @Login @Stage_mob @Android @MOBILE
  Scenario: Skip marketing pages from Marketing Page 1
    Then Marketing Page 1 should be displayed
    When The user taps on the skip button on Marketing Page 1
    Then I land to enter email screen

  @Regression @Login @Stage_mob @Android @MOBILE @Test
  Scenario: Verify to login with unregistered user
    Then Marketing Page 1 should be displayed
    When The user taps on the skip button on Marketing Page 1
    Then I land to enter email screen
    When I login with unregistered user
    And I click on "OK" button
    And I click on "OK" button
#    And I verify I got the "We will be verifying the phone number attached with this email" message
    And I land to enter email screen

  @Regression @Login @Stage_mob @Android @MOBILE @TestDemo2
  Scenario: Verify to login with registered user with invalid otp
    Then Marketing Page 1 should be displayed
    When The user taps on the skip button on Marketing Page 1
    Then I land to enter email screen
   Given I login with resource registered user
    And I clear otp field
    And I clear otp field and enter invalid
    And I click on "VERIFY" button
    And I verify I got the verification code is invalid message
#   And I verify I got the "Verification code is invalid" message
    And I land to dashboard screen
#
  @Regression @Login @Stage_mob @Android @MOBILE @Test
  Scenario: Verify to login with registered user with resend otp
    Then Marketing Page 1 should be displayed
    When The user taps on the skip button on Marketing Page 1
    Then I land to enter email screen
   Given I login with registered user
#    And I login with registered added user
    #And I verify I got the "We will be verifying the phone number attached with this email" message
    And I click on "OK" button
    And I clear otp field
    And I click on resend code link
    And I verify otp from clipboard
    And I click on "VERIFY" button
    And I land to dashboard screen

  @Regression @Login @Stage_mob @Android @MOBILE @Test
  Scenario: Verify to login with registered user
    Then Marketing Page 1 should be displayed
    When The user taps on the skip button on Marketing Page 1
    Then I land to enter email screen
    Given I login with registered user
#    And I login with registered added user
    #And I verify I got the "We will be verifying the phone number attached with this email" message
    And I land to dashboard screen
#    And I verify I am at "Dashboard" screen


