# Intro
Maven project for End to End (E2E) testing of an [e-commerce website](https://www.rahulshettyacademy.com/loginpagePractise/) using Selenium Java.

Implements the Page Object Model (POM) design pattern for ease of maintenance and clear division of responsibilities. Uses PageFactory to handle element location.

Utilizes TestNG for assertions and test suite setup.

# Rules and Guidance
1. Page object methods wait for elements to be visible before interacting with them. If you're checking for the visibility or invisibility of discrete elements, that needs to be handled by your test. All page objects have wait methods you can call.
2. All Page classes must live under **src.main.java.org.djd**
3. The **src.test.java.org.djd** package will contain subpackages each representing individual tests and all files they need to run.
   1. Each subpackage must have a Test Class.
   2. Each subpackage must have a Setup.properties file.
   3. Each subpackage may optionally have a Data.json file.
4. Every Test must extend BaseTest.
