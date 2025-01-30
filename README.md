# Table of Contents
- [Background](#background)
- [Framework Features](#framework-features)
- [Requirements](#requirements)
- [How To Run Tests](#how-to-run-tests)
- [Continuous Integration (CI) with GitHub Actions](#continuous-integration-ci-with-github-actions)
- [Considered but Not Done](#considered-but-not-done)
- [Appendix](#appendix)


# Background
Selenium Framework written in Java for End to End (E2E) testing. Driven by an XML Test Suite. Tests can be run locally or remotely (via GitHub Actions).

Page objects and Tests are included to demonstrate the integration with the framework. 

Website under test located here: https://www.rahulshettyacademy.com/loginpagePractise/

Core framework files are located in: `src/main/java/framework`

Example Page Objects are located in: `src/main/java/page_objects`

Example Tests are located in: `src/test/java`

Example Test Suite is located in: `test-suites`


# Framework Features
* Implements **Page Object Model** (POM) design pattern for ease of code maintenance and clear separation of page object responsibilities.
* Provides wait methods in `BasePage` superclass so page objects can wait for elements to be visible/clickable before interacting with them.
* Uses **Maven** for build and dependency management.
* Uses **TestNG** for assertions and test annotations.
* Uses the **Maven Surefire plugin** to integrate with TestNG to run tests.
* Generates a HTML report summarizing test results and how long the tests took. Test failures have screenshots attached to them.
  * When running tests locally, the report is saved to the `test-results` folder. 
  * > **Note**: I'm using the Extent Report Framework to generate the report instead of using Maven Surefire Plugin due to the former's higher level of customization.
* Has support for TestNG's DataProvider feature to run test methods with multiple configurations.
* All tests are run on parallel threads.
* Supports tests on the following browsers:
  * Google Chrome
  * Microsoft Edge
  * Mozilla Firefox


# Requirements
* OS: Windows or MacOS or Linux
* IDE: [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/) (This works in Eclipse, but I think IntelliJ is much smoother to use)
* JDK: [OpenJDK](https://openjdk.org/)
* Java Version: 17


# How To Run Tests
To run tests locally using this framework:
* Clone this repository.
* Create your Page Objects in `src/main/java/page_objects`
  * The example `PageObjectTemplate` class has been provided for reference.
  * > **Note:** For page object standards, see the [Page Objects](#page-objects) section.
* Create your Tests in `src/test/java`
  * The example `TestTemplate` class has been provided for reference.
  * > **Note:** For test class standards, see the [Tests](#tests) section.
* In IntelliJ, create Maven run configurations that follow this format: 
  * `mvn clean test -D<browser> -D<testSuite> -D<url>`
* The info in the angle brackets are runtime arguments/properties that are retrieved via `BaseTest.getSetupProperties()`:
  * testSuite: The name of your XML testing suite.
  * browser: The name of the browser to test on. Takes "Chrome", "Edge", or "Firefox".
  * url: The URL of the website for the tests.
  * The following Maven run configurations have been provided for reference in `.idea/runConfigurations`:
    * DJD Chrome Run
    * DJD Edge Run
    * DJD Firefox Run
> **Note**: `BaseTest.getSetupProperties()` also retrieves an "env" property. If you run tests locally, this property does not need to be included in the Maven run configuration.
> However, I also set up this repository to run remotely via GitHub Actions. When running remotely, the "env" property must be set to "PRD" to make the tests run on headless browsers.
> This is because GitHub's Runners are headless environments. For more info about this, refer to the **Continuous Integration (CI) with GitHub Actions** section below.


# Continuous Integration (CI) with GitHub Actions
I configured this repository to run via GitHub Actions as well so we can get some Continuous Integration. This is controlled by the [.github/workflows/actions.yml](https://github.com/DangD96/SeleniumE2E/blob/master/.github/workflows/actions.yml) file.

With GitHub Actions CI:
* Tests can be triggered to run upon pushing to the master branch with changes to files under specific directories.
* Tests can be manually triggered to run by going to the [Actions tab and selecting the Selenium Tests in GitHub Runner workflow](https://github.com/DangD96/SeleniumE2E/actions/workflows/actions.yml).
* Tests are configured to run on a schedule using Cron:
  * On the 10th of every month, tests will run on Chrome.
  * On the 20th of every month, tests will run on Edge.
  * On the 30th of every month, tests will run on Firefox.


# Considered but Not Done

## IRetryAnalyzer
This framework does not use TestNG's `IRetryAnalyzer` interface for re-running flaky tests. 

**I consider this bad practice**. If tests are flaky to begin with, they need to be updated to be more robust.

## PageFactory
This framework does not use `PageFactory` to locate elements.

This is because this approach [is not recommended by Selenium contributors and can lead to StaleElementExceptions](https://ultimateqa.com/pagefactory-vs-page-object/).


# Appendix

## Page Objects
I split Page Object classes into 3 regions: Locators, Getters, and Performers:
* The Locator region holds the `By` locators for the page elements.
* The Getter region can return things to be used for test assertions.
* The Performer region contains methods that act on WebElements found by using the values in the Locator region.

All Page Objects extend the `BasePage` superclass. By doing this, all page objects will automatically wait for AJAX upon instantiation. Additionally, by calling the superclass's constructor, Page Objects get access to JavascriptExecutor, Wait methods, and other convenience methods. 


## Tests
* All test class names must end in "Test". This is a requirement for TestNG to work correctly.
* All test classes extend the `BaseTest` class.
* Test methods must use the `@Test` annotation.
* Use the custom `Assertion` class to make test assertions.
* Test classes must be specified in an XML Test Suite file located in `test-suites` in order for the framework to pick up on them.
