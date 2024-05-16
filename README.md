# Background
Selenium Java Framework for End to End (E2E) testing of an [e-commerce website](https://www.rahulshettyacademy.com/loginpagePractise/). 

This framework has the following features:
1. Uses Maven for dependency management and run configurations.
2. Implements the Page Object Model (POM) design pattern for ease of code maintenance and clear separation of page object responsibilities.
3. Uses PageFactory to handle element location.
4. Utilizes TestNG for assertions and test suite setup.
5. Runs tests in parallel by default and can be configured to run on Google Chrome, Microsoft Edge, or Mozilla Firefox.
6. Generates an HTML report summarizing test results. Test failures have screenshots attached to them.

# Rules and Guidance
1. Page object methods wait for elements to be visible before interacting with them. 
2. If you need to check for the visibility or invisibility of elements in the test itself, use the wait methods on the page objects.
3. All Page classes live under **src.main.java**.
4. All Test classes live under **src.test.java**.
5. If your test requires data to be used in a TestNG @DataProvider, provide the data in a .json file of the same name.
6. Every Page class must extend BasePage.
7. Every Test class must extend BaseTest.
8. The order that @Test methods are run in is determined by their order in the XML suite.

# Configuration
To properly run the tests with Maven, do the following:
1. Create a corresponding Maven profile for your suite XML file.
2. Create a Maven run configuration to run the XML suite.

Maven run configurations follow this pattern: 
_clean test -P<Name of test suite> -Dbrowser=<chrome/edge/firefox> -Dheadless=<true/false> -DrunName=<Name of this test run>_
