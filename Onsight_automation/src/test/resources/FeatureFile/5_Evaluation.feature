Feature: 4_JobPhoto

  Background:
    And I turn "ON" the wifi on the device
    And The user taps on the skip button on Marketing Page 1
    And I land to enter email screen
    And I login with registered user
    And I land to dashboard screen

   # Evaluation Page 1
  # --------------------------------------------------

  @Regression @Evaluation @Stage_mob @Android @MOBILE @Demo
  Scenario: Verify to complete install evaluation category
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I get "Show Ready" image count
    And I get "Show Ready" add photo
    And I navigate to gallery
    And I select photo from gallery
    And I click on add photo button
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I click on "Show Ready" check box
    And I click on "SUBMIT" button
    And I click on "OK" button
    And I click on "YES" button
#    And I click on "YES" evaluation
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

  @Regression @Evaluation @Stage_mob @Android @MOBILE @Demo
  Scenario: Verify to complete dismantle evaluation category
    And I navigate to job Updates screen
    And I enter "W-256523" job number
    And I click on "SUBMIT" button
    Then I verify I am at "W-256523" screen
    And I navigate to "Job Photos" sub module
    Then I verify I am at "W-256523" screen
    And I get "Outbound BOL" image count
    And I get "Outbound BOL" add photo
    And I navigate to camera
    And I select photo from camera
    And I click on "ADD PHOTOS" button
    And I click on "OK" button
    And I click on "Outbound BOL" check box
    And I click on "SUBMIT" button
    And I click on "OK" button
    And I click on "YES" button
#    And I click on "Dismantle" evaluation
    And I select answer of questions at the screen
      | question                                     | answer |
      | Did the crates come back on time?           | No     |
      | Any delays due to hanging sign?              | No     |
      | Any excessive waiting time?                  | Yes    |
      | Any damages to exhibit properties?           | No     |
      | Was the outbound info. correct?              | Yes    |
      | Did Nth Degree submit the BOL? Picture sent? | YES     |
    And I click on "SUBMIT" button
    And I click on "YES" button
    And I click on "OK" button
    And I verify I am at "W-256523" screen























