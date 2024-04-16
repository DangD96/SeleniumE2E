package org.djd;

import org.testng.annotations.Test;

public class ProductTest extends BaseTest{
    @Test(description = "Add correct product to shopping cart")
    public void addProductTest() {

    }

    @Test(description = "Verify correct product was added to shopping cart", dependsOnMethods = {"addProductTest"})
    public void verifyProductTest() {

    }

    @Test(description = "Remove the product from shopping cart", dependsOnMethods = {"verifyProductTest"})
    public void removeProductTest() {

    }
}
