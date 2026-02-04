package com.diliana.api.utils;

import com.diliana.api.enums.PetStatus;
import io.restassured.response.Response;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class PetUtils {

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


    public static Response createPet(String name, PetStatus status) {
        long uniqueId = System.currentTimeMillis(); // уникален id за теста

        String requestBody = String.format("""
                {
                  "id": %d,
                  "name": "%s",
                  "status": "%s"
                }
                """, uniqueId, name, status.name().toLowerCase());

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


    public static Response updatePet(long id, String newName, PetStatus newStatus) {
        String requestBody = String.format("""
                {
                  "id": %d,
                  "name": "%s",
                  "status": "%s"
                }
                """, id, newName, newStatus.name().toLowerCase());

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


    public static Response deletePet(long id) {
        Response response = given()
                .when()
                .delete("/pet/" + id)
                .then()
                .extract()
                .response();

        if (response.statusCode() != 200 && response.statusCode() != 404) {
            throw new RuntimeException("Failed to delete pet: " + response.getStatusCode());
        }

        return response;
    }

}

