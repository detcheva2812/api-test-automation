package com.diliana.api.utils;

import io.restassured.response.Response;
import java.util.List;
import java.util.Random;
import static io.restassured.RestAssured.given;

public class ApiUtils {

    // Връща случаен petId от наличните
    public static long getRandomAvailablePetId() {
        Response response = given()
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then()
                .extract()
                .response();

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch pets: " + response.statusCode());
        }

        List<Long> petIds = response.jsonPath().getList("id", Long.class);
        if (petIds.isEmpty()) {
            throw new RuntimeException("No pets available");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(petIds.size());
        return petIds.get(randomIndex);
    }

    // Създава нов pet с уникално id
    public static Response createPet(String name, String status) {
        long uniqueId = System.currentTimeMillis(); // уникален id за теста

        String requestBody = String.format("""
                {
                  "id": %d,
                  "name": "%s",
                  "status": "%s"
                }
                """, uniqueId, name, status);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/pet")
                .then()
                .extract()
                .response();

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to create pet: " + response.getStatusCode());
        }

        return response;
    }

    // Update pet: подава ново име и статус за съществуващ id
    public static Response updatePet(long id, String newName, String newStatus) {
        String requestBody = String.format("""
                {
                  "id": %d,
                  "name": "%s",
                  "status": "%s"
                }
                """, id, newName, newStatus);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put("/pet")
                .then()
                .extract()
                .response();

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to update pet: " + response.getStatusCode());
        }

        return response;
    }
}
