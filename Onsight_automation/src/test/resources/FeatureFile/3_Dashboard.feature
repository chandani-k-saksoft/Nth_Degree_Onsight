Feature: 3_Dashboard

  Background:
    And I turn "ON" the wifi on the device
    And The user taps on the skip button on Marketing Page 1
    And I land to enter email screen
    And I login with registered user
#    And I login with registered added user
    And I land to dashboard screen

   # Dashboard Page 1
  # --------------------------------------------------

  @Regression @Dashboard @Stage_mob @Android @MOBILE
  Scenario: Dashboard item list
    And I verify the navigation list as follows
      | Job Updates     |
      | Lead Sheet      |
      | Onboarding      |
      | My Schedule     |
      | Travel Expenses |

  @Regression @Dashboard @Stage_mob @Android @MOBILE @Test
  Scenario: Navigate to each module
    And I navigate to "Job Updates" module
    Then I verify I am at "Job Updates" screen
    And I press device Back button
    And I land to dashboard screen
    And I navigate to "Lead Sheet" module
    Then I verify I am at "Lead Sheet" screen
    And I press device Back button
    And I land to dashboard screen
    And I navigate to "Onboarding" module
    Then I verify I am at "Onboarding" screen
    And I press device Back button
    And I land to dashboard screen
    And I navigate to "My Schedule" module
#    Then I verify I am at "My Schedule" screen
    And I press device Back button
    And I land to dashboard screen
    And I navigate to "Travel Expenses" module
    Then I verify I am at "Travel Expenses" screen
    And I press device Back button
    And I land to dashboard screen

  @Regression @Dashboard @Stage_mob @Android @MOBILE @Test
  Scenario: Verify if user try to navigate job number screen with invalid job number
    And I navigate to job Updates screen
    And I enter "w-123456" job number
    And I click on "SUBMIT" button
    And I verify I got the Job number is invalid message
#    And I verify I got the "Job number is invalid" message

  @Regression @Dashboard1 @Stage_mob @Android @MOBILE @Test
  Scenario: Verify if user try to navigate job number screen with valid job number
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    And I verify I got the Job number is invalid message
#    And I land to Job number screen

  @Regression @Dashboard1 @Stage_mob @Android @MOBILE @Test2 @Demo
  Scenario: Verify to add email address in additional contacts
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    And I land to Job number screen
    And I scroll the screen down 2 times
    And I expand list
    And I add email address
    And I click on add email button

  @Regression @Dashboard1 @Stage_mob @Android @MOBILE @Test2
  Scenario: Verify show details
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    And I land to Job number screen
    Then I verify show info details for work order
#      | Title        | Value                    |
      | Show Name    |  Saksoft Test Show 2026  |
      |  Show Number | S-256517                 |
      | Venue        | N/A    |
      | Open Date | 02/27/2026 8:00 AM        |
      | Close Date | 03/27/2026 8:00 AM |
      | Hall Clear | 04/10/2026 8:00 AM |

#  @Regression @Dashboard1 @Stage_mob @Android @MOBILE
#  Scenario: verify email address is existing in the list
#    And I navigate to job Updates screen
#    And I enter "W-257644" job number
#    And I click on "SUBMIT" button
#    And I land to Job number screen
#    And I scroll down
#    And I add email address
#    And I check adding email is existing in the list
#    And I remove esisting email
#    And I add email address

  @Regression @Dashboard1 @Stage_mob @Android @MOBILE @Test
  Scenario: verify to check duplicate email address
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    And I land to Job number screen
    And I scroll the screen down 2 times
    And I add email address
#    And I verify I got the "Email already exist" message



















