package com.diliana.api.utils;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderUtils {

    // ================= CREATE =================с подадени id-та и количество
    public static Response createOrder(long orderId, long petId, int quantity) {

        String requestBody = String.format("""
                {
                  "id": %d,
                  "petId": %d,
                  "quantity": %d,
                  "status": "placed",
                  "complete": true
                }
                """, orderId, petId, quantity);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/store/order")
                .then()
                .extract()
                .response();

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to create order: " + response.getStatusCode());
        }

        return response;
    }

    // ================= GET =================
    public static Response getOrder(long orderId) {
        Response response = given()
                .when()
                .get("/store/order/" + orderId)
                .then()
                .extract()
                .response();

        return response;
    }

    // ================= DELETE =================
    public static Response deleteOrder(long orderId) {
        Response response = given()
                .when()
                .delete("/store/order/" + orderId)
                .then()
                .extract()
                .response();

        return response;
    }
}

