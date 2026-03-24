Feature: 4_JobPhoto

  Background:
    And I turn "ON" the wifi on the device
    And The user taps on the skip button on Marketing Page 1
    And I land to enter email screen
   And I login with registered user
#    And I login with registered added user
    And I land to dashboard screen

   # JobPhoto Page 1
  # --------------------------------------------------

  @Regression @JobPhoto @Stage_mob @Android @MOBILE
  Scenario: jobPhoto item list
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I verify the navigation list as follows
      | Install Freight     |
      | Install Freight Content      |
      | Install Progress      |
      | Show Ready    |
      | Dismantle Freight Content |
      | Dismantle Freight          |
      | Outbound BOL               |
      | Client Request             |
      | Special Handling           |
      | Accident                   |

  @Regression @JobPhoto @Stage_mob @Android @MOBILE @Test @Demo
  Scenario: Verify to add job photo through camera
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I click on add photo plus button
    And I navigate to camera
    And I select photo from camera

  @Regression @JobPhoto1 @Stage_mob @Android @MOBILE
  Scenario: Verify to add job photo through gallery
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I click on add photo plus button
    And I navigate to gallery
    And I select photo from gallery

  @Regression @JobPhoto @Stage_mob @Android @MOBILE
  Scenario: Verify to added job photo for one category
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I get "Install Freight" add photo
    And I navigate to gallery
    And I select photo from gallery
    And I click on add photo button
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I click on "Install Freight" check box
    And I click on "SUBMIT" button
    And I click on "OK" button

  @Regression @JobPhoto1 @Stage_mob @Android @MOBILE
  Scenario: Verify to count one category
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I get "Install Freight" image count
    And I get "Install Freight" add photo
    And I navigate to gallery
    And I select photo from gallery
    And I click on add photo button
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I click on "Install Freight" check box
    And I click on Install Freight send mail check box
    And I click on "SUBMIT" button
    And I click on "OK" button
    Then I verify "Install Freight" image count increase

  @Regression @JobPhoto @Stage_mob @Android @MOBILE
  Scenario: Verify to navigate to uploaded photo screen
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I get "Install Freight" image count
    And I verify "Install Freight" image count click
    And I scroll the screen down 2 times
    And I press device Back button
    And I verify I am at "W-256523" screen

  @Regression @JobPhoto @Stage_mob @Android @MOBILE @Test
  Scenario: Verify to get mail for install category
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I scroll the screen down 2 times
    And I expand list
    And I add email address
    And I click on add email button
    And I scroll the screen up 2 times
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I get "Install Freight" image count
    And I get "Install Freight" add photo
    And I navigate to gallery
    And I select photo from gallery
    And I click on add photo button
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I click on "Install Freight" check box
    And I click on Install Freight send mail check box
    And I click on "SUBMIT" button
    And I click on "OK" button
    Then I verify "Install Freight" image count increase
    And I receive mail from gmail having subject as "Nth Degree Project Update for Saksoft at Saksoft Test Show 2026"

  @Regression @JobPhoto @Android @MOBILE @Demo
  Scenario: Verify to added job photo for multiple category and send mail
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I get "Install Freight" image count as "oldImageCount1"
    And I get "Install Freight Content" image count as "oldImageCount2"
    And I get "Install Progress" image count as "oldImageCount3"
    And I get "Install Freight" add photo
    And I navigate to gallery
    And I select photo from gallery
    And I click on add photo button
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I get "Install Freight Content" add photo
    And I navigate to gallery
    And I select photo from gallery
    And I click on add photo button
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I get "Install Progress" add photo
    And I navigate to gallery
    And I select photo from gallery
    And I click on add photo button
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I verify "Install Freight" image count click
    And I press device Back button
#    And I click on add photo plus button
    And I click on "Install Freight" check box
    And I click on "Install Freight Content" check box
    And I click on "Install Progress" check box
    And I click on Install Freight send mail check box
    And I click on Install Freight Content send mail check box
    And I click on "SUBMIT" button
    And I click on "OK" button
    And I receive mail from gmail having subject as "Nth Degree Project Update for Saksoft at Saksoft Test Show 2026"
    Then I verify "Install Freight" image count increase by "oldImageCount1"
#    And I receive mail from gmail having subject as "Nth Degree Project Update for Nth Degree at TSR"
    And I receive mail from gmail having subject as "Nth Degree Project Update for Saksoft at Saksoft Test Show 2026"
    Then I verify "Install Freight Content" image count increase by "oldImageCount2"
    And I receive mail from gmail having subject as "Nth Degree Project Update for Saksoft at Saksoft Test Show 2026"
    Then I verify "Install Progress" image count increase by "oldImageCount3"

  @Regression @Dashboard1 @Stage_mob @Android @MOBILE @Test2 @Demo2
  Scenario: Verify job photo show details
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    And I land to Job number screen
    Then I verify show info details for work order
      | Title        | Value                    |
      | Show Name    |  Saksoft Test Show 2026  |
      |  Show Number | S-256517                 |
      | Venue        | N/A    |
      | Open Date | 02/27/2026 8:00 AM        |
      | Close Date | 03/27/2026 8:00 AM |
      | Hall Clear | 04/10/2026 8:00 AM |

  @Regression @JobPhoto @Stage_mob @Android @MOBILE @Demo2
  Scenario: Verify to added job photo for multiple category with evaluation screen navigation
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I get "Install Freight" image count as "oldImageCount1"
    And I get "Show Ready" image count as "oldImageCount2"
    And I get "Install Freight" add photo
    And I navigate to gallery
    And I select photo from gallery
    And I click on add photo button
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I get "Show Ready" add photo
    And I navigate to camera
    And I select photo from camera
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I verify "Install Freight" image count click
    And I press device Back button
#    And I click on add photo plus button
    And I click on "Install Freight" check box
    And I click on "Show Ready" check box
    And I click on Install Freight send mail check box
    And I click on "SUBMIT" button
    And I click on "OK" button
    And I click on "YES" button
    And I select answer of questions at the screen
      | question                                  | answer |
      | Was freight on time?                      | NO    |
      | Any issues with the hanging sign?         | NO     |
      | Any damages to exhibit properties?        | No     |
      | Were all services ordered?                | YES    |
      | Were any extra services ordered on-site?  | YES     |
      | Did set up start on time?                 | YES    |
      | Was source client satisfied with install? | YES    |
      | Were the drawings correct?                | YES    |
      | Did you have set-up/electrical drawings?  | YES     |
      | Any issues with the carpet?               | NO     |
    And I click on "SUBMIT" button
    And I click on "YES" button
    And I click on "OK" button
    And I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I receive mail from gmail having subject as "Nth Degree Project Update for Saksoft at Saksoft Test Show 2026"
    Then I verify "Install Freight" image count increase by "oldImageCount1"
    Then I verify "Show Ready" image count increase by "oldImageCount2"

  @Regression @JobPhoto @Stage_mob @Android @MOBILE @Demo2
  Scenario: Verify if user try to navigate job number screen with invalid job number
    And I navigate to job Updates screen
    And I enter "w-123456" job number
    And I click on "SUBMIT" button
    And I verify I got the Job number is invalid message
    And I navigate to "Job Photos" sub module