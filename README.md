# For Fun
<table>
  <tr>
    <td><strong>Files:</strong></td>
    <td align="right">22</td>
  </tr>
  <tr>
    <td><strong>Lines:</strong></td>
    <td align="right">1346</td>
  </tr>
  <tr>
    <td><strong>Blanks:</strong></td>
    <td align="right">198</td>
  </tr>
  <tr>
    <td><strong>Comments:</strong></td>
    <td align="right">78</td>
  </tr>
  <tr>
    <td><strong>Lines of Code:</strong></td>
    <td align="right">1070</td>
  </tr>
</table>

Source: https://codetabs.com/count-loc/count-loc-online.html


# Background
Selenium Framework written in Java for End to End (E2E) testing. Driven by an XML Test Suite. Tests can be run locally or remotely (via GitHub Actions).

Page classes and Tests are included to demonstrate the integration with the framework. 

Website under test located here: https://www.rahulshettyacademy.com/loginpagePractise/

Core framework files are located in: `src/main/java/framework`

Example Page classes are located in: `src/main/java/pages`

Example Tests are located in: `src/test/java`

Example Test Suite is located in: `test-suites`


# Framework Features
* Implements a **modified** version of the traditional **Page Object Model** (POM) design pattern. 
  * My version is mainly driven by static Page classes and the `BaseDriver` wrapper class.
  * I opted to go this direction for easier code maintenance and cleaner separation of responsibilities.
* Uses **Maven** for build and dependency management.
* Uses **TestNG** for assertions and test annotations.
* Uses the **Maven Surefire plugin** to integrate with TestNG to run tests.
* Generates a HTML report summarizing test results and how long the tests took. Test failures have screenshots attached to them.
  * When running tests locally, the report is saved to the `test-results` folder. 
  * > **Note**: I'm using the Extent Report Framework to generate the report instead of using Maven Surefire Plugin due to the former's higher level of customization.
* Has support for TestNG's DataProvider feature to run test methods with multiple configurations. For more details, refer to the [Data Provider](#data-provider) section.
* All tests are run on parallel threads.
* Supports tests on the following browsers:
  * Google Chrome
  * Microsoft Edge
  * Mozilla Firefox


# Requirements
* OS: Windows or MacOS or Linux
* IDE: [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/) (Recommended)
* JDK: [OpenJDK](https://openjdk.org/)
* Java Version: 17


# How To Run Tests
To run tests locally using this framework:
* Clone this repository.
* Create your Page classes in `src/main/java/pages`
  * > **Note:** For page class standards, see the [Page Objects](#page-classes) section.
* Create your Tests in `src/test/java`
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


# Continuous Integration (CI) with GitHub Actions
I configured this repository to run via GitHub Actions as well. This is controlled by the [.github/workflows/actions.yml](https://github.com/DangD96/SeleniumE2E/blob/master/.github/workflows/actions.yml) file.

With GitHub Actions CI:
* Tests can be triggered to run upon pushing to or merging to the master branch with changes to files under specific directories.
* Tests can be manually triggered to run by going to the [Actions tab and selecting the Selenium Tests in GitHub Runner workflow](https://github.com/DangD96/SeleniumE2E/actions/workflows/actions.yml).
* Tests are configured to run on a schedule using Cron:
  * On the 10th of every month, tests will run on Chrome.
  * On the 20th of every month, tests will run on Edge.
  * On the 30th of every month, tests will run on Firefox.

I receive an email upon completion of a scheduled test. 

The email tells me what browser the test ran on, whether it passed or failed, and provides the HTML results report for download:

![scheduled_run_email_result](/images/result_email.png)


# Considered but Not Done

## PageFactory
This framework does not use `PageFactory` to locate elements.

This is because this approach [is not recommended by Selenium contributors and can lead to StaleElementExceptions](https://ultimateqa.com/pagefactory-vs-page-object/).


# Appendix

## Page Classes
Static classes that encapsulate a page's elements and behavior. I split Page classes into 3 regions: Locators, Getters, and Performers:
* The Locator region holds the `By` locators for the page elements.
* The Getter region can return things to be used for test assertions.
* The Performer region contains methods that act on WebElements found by using the values in the Locator region.

## Tests
* All test class names must end in "Test". This is a requirement for TestNG to work correctly.
* All test classes extend the `BaseTest` class.
* Test methods must use the `@Test` annotation.
* Use the custom `Assert` class to make test assertions.
* Test classes must be specified in an XML Test Suite file located in `test-suites` in order for the framework to pick up on them.


## Data Provider
I included integration with TestNG's Data Provider feature so certain test methods can run with multiple sets of test data. For example, a test method that tests login credentials can take advantage of a Data Provider to efficiently test multiple combinations of usernames and passwords.

_Guidelines for use:_

**Create JSON File**
* In `src/test/java/data` create a JSON file containing a single JSON array. Give the file a name that is easily distinguishable.
* For each combination you want to test, add that many JSON objects to the array. In each JSON object, define a uniform set of keys and then enter the values you want to test with.
>**Note:** See `src/test/java/data/LoginTestLoginInvalidData.json` for an example.

**Create Data Provider Method To Get JSON Data and Link It To Test Method**
* In your `*Test.java` class, define a method that returns `Object[]` and has the @DataProvider annotation. In the method body, return the result of the call to `getTestData(<filename of your JSON file>)`. This is your Data Provider method that retrieves the test data from the JSON file.
* In your `*Test.java` class, find the @Test method you want to use the Data Provider on and link it to your Data Provider method by using the `dataProvider` parameter and specifying the name of your method from the step above.
* Update your test method's parameters so it only takes a single parameter of type `HashMap<String, String>`. I called this parameter `input`.
* Now, in your test method body, whenever you want to use your test data, call `input.get(<json key name of the data you want>)`.
> **Note:** See `src/test/java/LoginTest.java` for an example.
