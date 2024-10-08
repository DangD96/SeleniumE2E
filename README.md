# Background
Selenium Framework written in Java for End to End (E2E) testing of an [e-commerce website](https://www.rahulshettyacademy.com/loginpagePractise/). 

Page objects and Tests are included to demonstrate the integration with the framework.

# Features
1. Uses Maven for dependency management.
2. Utilizes TestNG for assertions and test annotations.
3. Uses Maven's Surefire plugin to integrate with TestNG to run tests.
4. Runs tests in parallel by default.
5. Tests can be configured to run on Google Chrome, Microsoft Edge, or Mozilla Firefox.
6. Generates an HTML report summarizing test results and how long the tests took. Test failures have screenshots attached to them.
7. Implements the Page Object Model (POM) design pattern for ease of code maintenance and clear separation of page object responsibilities.
8. Waits for elements to be visible before interacting with them.
9. Adds customization to tests by leveraging TestNG's DataProvider feature.

# Run Parameters
Maven run configurations follow this pattern: 

mvn clean test -D _testSuite_ -D _browser_ -D _url_

Where:
* testSuite: The name of your XML testing suite.
* browser: The name of the browser to test on. Supports Chrome, Edge, or Firefox.
* url: The URL of the starting website for the tests.

# Considered but not done
* This framework does not use TestNG's IRetryAnalyzer interface for re-running flaky tests. If tests are flaky to begin with, then they need to be updated to be more robust.
