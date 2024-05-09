# Intro
Maven project for End to End (E2E) testing of an [e-commerce website](https://www.rahulshettyacademy.com/loginpagePractise/) using Selenium Java.

Implements the Page Object Model (POM) design pattern for ease of maintenance and clear division of responsibilities. Uses PageFactory to handle element location.

Utilizes TestNG for assertions and test suite setup.

# Rules and Guidance
1. Page object methods wait for elements to be visible before interacting with them. If you're checking for the visibility or invisibility of discrete elements, that needs to be handled by your test. All page objects have wait methods you can call.
2. All Page classes must live under **src.main.java.org.djd**
3. If your test requires data to be used in a data provider, provide the data in a .json file of the same name.
4. Every Test must extend BaseTest.
