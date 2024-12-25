# Background
Selenium Framework written in Java for End to End (E2E) testing. Driven by an XML Test Suite.

NOTE: Page objects and Tests are included to demonstrate the integration with the framework. Website under test located here: [e-commerce website](https://www.rahulshettyacademy.com/loginpagePractise/)

Core framework files are located in `src/main/java/framework`

Example Page Objects are located in `src/main/java/page_objects`

Example Tests are located in `src/test/java`

Example Test Suite is located in `test-suites`


# Framework Features
* Implements Page Object Model (POM) design pattern for ease of code maintenance and clear separation of page object responsibilities.
* Provides the `getElement()` and `getElements()` APIs in `BasePage` to wait for elements to be visible before interacting with them.
* Uses **Maven** for build and dependency management.
* Uses **TestNG** for assertions and test annotations.
* Uses the **Maven Surefire plugin** to integrate with TestNG to run tests.
* Generates a HTML Extent Report summarizing test results and how long the tests took. Test failures will have screenshots attached to them.
  * I'm using the Extent Report Framework to generate a report instead of using Maven Surefire Plugin due to the former's higher level of customization.
* Has support for TestNG's DataProvider feature to run test methods with multiple configurations.
* All tests are run on parallel threads.
* Tests can be run on the following browsers:
  * Google Chrome
  * Microsoft Edge
  * Mozilla Firefox


# Requirements
* OS: Windows or MacOS or Linux
* IDE: [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/?section=windows)
  * This works in Eclipse too, but I think IntelliJ is just so much smoother to use.
* JDK: [OpenJDK](https://openjdk.org/)
* VCS: [Git](https://git-scm.com/downloads)


# Running Tests
To run tests using this framework:
* Make sure you have the core files in `src/main/java/framework` (if you forked this repo, the core files should all be there)
* Create your Page Objects in `src/main/java/page_objects`
  * The example `PageObjectTemplate` class has been provided for reference.
  * For a more detailed explanation, see the [Page Objects](#Page-Objects) section.
* Create your Tests in `src/test/java`
  * The example `TestTemplate` class has been provided for reference.
  * For a more detailed explanation, see the [Tests](#Tests) section.
* In IntelliJ, create Maven run configurations that follow this format: `mvn clean test -D<testSuite> -D<browser> -D<url>`
  * testSuite: The name of your XML testing suite.
  * browser: The name of the browser to test on. Takes "Chrome", "Edge", or "Firefox".
  * url: The URL of the website for the tests.
  * The following Maven run configurations have been provided for reference:
    * DJD Chrome Run
    * DJD Edge Run
    * DJD Firefox Run


# Considered but not done
This framework does not use TestNG's `IRetryAnalyzer` interface for re-running flaky tests. 

**This is bad practice**. If tests are flaky to begin with, they need to be updated to be more robust.


# Appendix

## Page Objects
* (TODO)



## Tests
* (TODO)
* All test class names must end in "Test". This is a requirement for TestNG to work correctly.