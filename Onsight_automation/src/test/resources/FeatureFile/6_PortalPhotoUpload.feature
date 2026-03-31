Feature: 5_PortalJobPhoto

   # Portal JobPhoto upload
  # --------------------------------------------------
  @Regression @Portal @Stage @WEB @Android @Demo2
  Scenario: Verify uploaded job photo in web portal
    Given I am at OnSight portal login screen
    And I login to the application with valid user
    And I verify I am at dashboard page
    And I enter show number "S-256517"
    And I select "Picture Report" as "selectReportFilterbtn" dropdown
    And I click on "Submit" login button
    And I get "Install Freight" image count as "W-256523" in web
    And I get "Show Ready" image count as "W-256523" in web
    And I click on "Install Freight" image count of job number "W-256523"
    And I switch to "Resource View" tab
    And I verify 2 uploaded photos
    And I close the "Resource View" tab
    And I switch to "nth degree" tab
    And I click on "Show Ready" image count of job number "W-256523"
    And I switch to "Resource View" tab
    And I verify 1 uploaded photos
    And I close the "Resource View" tab
    And I switch to "nth degree" tab




