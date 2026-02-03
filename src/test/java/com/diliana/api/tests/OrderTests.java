package com.diliana.api.tests;

import com.diliana.api.base.BaseTest;
import com.diliana.api.utils.OrderUtils;
import com.diliana.api.utils.ApiUtils;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class OrderTests extends BaseTest {

    private long orderId; // за cleanup
    private long petId;

    @Test(groups = "positive")
    public void createOrder_shouldReturn200() {
        System.out.println("===== [Test] createOrder_shouldReturn200 START =====");

        // Вземаме случайно petId
        this.petId = ApiUtils.getRandomAvailablePetId();

        // Генерираме уникално orderId
        this.orderId = System.currentTimeMillis();

        Response response = OrderUtils.createOrder(orderId, petId, 2);


        assertEquals(response.getStatusCode(), 200);
        System.out.printf("[Result] Order created: orderId=%d, petId=%d%n", orderId, petId);

        System.out.println("===== [Test] createOrder_shouldReturn200 END =====");
    }



    @Test(groups = "positive")
    public void createAndGetOrder_shouldMatch() {
        System.out.println("===== [Test] createAndGetOrder_shouldMatch START =====");

        this.petId = ApiUtils.getRandomAvailablePetId();
        this.orderId = System.currentTimeMillis();
        int quantity = 3;

        // Създаваме order
        Response createResponse = OrderUtils.createOrder(orderId, petId, quantity);
        assertEquals(createResponse.getStatusCode(), 200);

        // Вземаме order по ID
        Response getResponse = OrderUtils.getOrder(orderId);
        assertEquals(getResponse.getStatusCode(), 200);
        assertEquals(getResponse.jsonPath().getLong("id"), orderId);
        assertEquals(getResponse.jsonPath().getLong("petId"), petId);
        assertEquals(getResponse.jsonPath().getInt("quantity"), quantity);
        assertEquals(getResponse.jsonPath().getString("status"), "placed");
        assertEquals(getResponse.jsonPath().getBoolean("complete"), true);

        System.out.printf("[Result] Order fetched successfully: orderId=%d, petId=%d%n", orderId, petId);
        System.out.println("===== [Test] createAndGetOrder_shouldMatch END =====");
    }

    @Test(groups = "negative")
    public void getNonExistingOrder_shouldReturn404() {
        System.out.println("===== [Test] getNonExistingOrder_shouldReturn404 START =====");

        long nonExistingOrderId = System.currentTimeMillis() + 9999;

        Response response = OrderUtils.getOrder(nonExistingOrderId);

        assertEquals(response.getStatusCode(), 404);
        System.out.printf("[Result] GET non-existing order returned 404 as expected: orderId=%d%n", nonExistingOrderId);

        System.out.println("===== [Test] getNonExistingOrder_shouldReturn404 END =====");
    }

    @Test(groups = "negative")
    public void deleteNonExistingOrder_shouldReturn404() {
        System.out.println("===== [Test] deleteNonExistingOrder_shouldReturn404 START =====");

        long nonExistingOrderId = System.currentTimeMillis() + 8888;

        Response response = OrderUtils.deleteOrder(nonExistingOrderId);

        assertEquals(response.getStatusCode(), 404);
        System.out.printf("[Result] DELETE non-existing order returned 404 as expected: orderId=%d%n", nonExistingOrderId);

        System.out.println("===== [Test] deleteNonExistingOrder_shouldReturn404 END =====");
    }




    @AfterMethod
    public void cleanup() {
        if (orderId != 0) {
            OrderUtils.deleteOrder(orderId);
            System.out.printf("[Cleanup] Order deleted: orderId=%d%n", orderId);
            orderId = 0;
        }
    }



}
