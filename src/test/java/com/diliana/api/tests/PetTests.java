package com.diliana.api.tests;

import com.diliana.api.base.BaseTest;
import com.diliana.api.utils.ApiUtils;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class PetTests extends BaseTest {

    private long petId;

    @BeforeClass
    public void init() {
        petId = ApiUtils.getRandomAvailablePetId();
        System.out.printf("===== [Setup] Random petId selected: %d =====%n", petId);
    }

    @Test
    public void getRandomPetById_shouldReturn200() {
        System.out.println("===== [Test] getRandomPetById_shouldReturn200 START =====");

        Response petResponse = given()
                .when()
                .get("/pet/" + petId)
                .then()
                .extract()
                .response();

        assertEquals(petResponse.getStatusCode(), 200);
        String petName = petResponse.jsonPath().getString("name");

        System.out.printf("[Result] Pet name: %s%n", petName);
        System.out.println("===== [Test] getRandomPetById_shouldReturn200 END =====");
    }

    @Test
    public void createPet_thenGetPet_shouldMatch() {
        System.out.println("===== [Test] createPet_thenGetPet_shouldMatch START =====");

        Response createResponse = ApiUtils.createPet("Rex", "available");
        long createdPetId = createResponse.jsonPath().getLong("id");
        String createdPetName = createResponse.jsonPath().getString("name");

        System.out.printf("[Result] Created petId: %d, name: %s%n", createdPetId, createdPetName);

        Response getResponse = given()
                .when()
                .get("/pet/" + createdPetId)
                .then()
                .extract()
                .response();

        assertEquals(getResponse.getStatusCode(), 200);
        assertEquals(getResponse.jsonPath().getLong("id"), createdPetId);
        assertEquals(getResponse.jsonPath().getString("name"), createdPetName);
        assertEquals(getResponse.jsonPath().getString("status"), "available");

        System.out.println("===== [Test] createPet_thenGetPet_shouldMatch END =====");
    }

    @Test
    public void updatePet_shouldChangeNameAndStatus() {
        System.out.println("===== [Test] updatePet_shouldChangeNameAndStatus START =====");

        // Създаваме нов pet за update
        Response createResponse = ApiUtils.createPet("Bella", "available");
        long createdPetId = createResponse.jsonPath().getLong("id");
        System.out.printf("[Result] Created petId: %d, name: %s%n",
                          createdPetId, createResponse.jsonPath().getString("name"));

        // Update pet
        Response updateResponse = ApiUtils.updatePet(createdPetId, "BellaUpdated", "sold");
        assertEquals(updateResponse.getStatusCode(), 200);

        // Проверяваме, че промяната е валидна
        Response getResponse = given()
                .when()
                .get("/pet/" + createdPetId)
                .then()
                .extract()
                .response();

        assertEquals(getResponse.getStatusCode(), 200);
        assertEquals(getResponse.jsonPath().getString("name"), "BellaUpdated");
        assertEquals(getResponse.jsonPath().getString("status"), "sold");

        System.out.printf("[Result] Updated petId: %d, name: %s, status: %s%n",
                          createdPetId,
                          getResponse.jsonPath().getString("name"),
                          getResponse.jsonPath().getString("status"));

        System.out.println("===== [Test] updatePet_shouldChangeNameAndStatus END =====");
    }
}
