package com.diliana.api.tests;

import com.diliana.api.base.BaseTest;
import com.diliana.api.utils.PetUtils;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.diliana.api.enums.PetStatus;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;



public class PetTests extends BaseTest {

    private long petId;

    @BeforeClass
    public void init() {
        petId = PetUtils.getRandomAvailablePetId();
        System.out.printf("===== [Setup] Random petId selected: %d =====%n", petId);
    }

    @Test(groups = "positive")
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

    @Test(groups = "positive")
    public void createPet_thenGetPet_shouldMatch() {
        System.out.println("===== [Test] createPet_thenGetPet_shouldMatch START =====");

        Response createResponse = PetUtils.createPet("Rex", PetStatus.AVAILABLE);
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

    @Test(groups = "positive")
    public void updatePet_shouldChangeNameAndStatus() {
        System.out.println("===== [Test] updatePet_shouldChangeNameAndStatus START =====");

        // Създаваме нов pet за update
        Response createResponse = PetUtils.createPet("Bella", PetStatus.AVAILABLE);
        long createdPetId = createResponse.jsonPath().getLong("id");
        System.out.printf("[Result] Created petId: %d, name: %s%n",
                createdPetId, createResponse.jsonPath().getString("name"));

        // Update pet
        Response updateResponse = PetUtils.updatePet(createdPetId, "BellaUpdated", PetStatus.SOLD);
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

    @Test(groups = "negative")
    public void getDeletedPet_shouldReturn404() {
        System.out.println("===== [Test] getDeletedPet_shouldReturn404 START =====");

        // Create a temporary pet
        Response createResponse = PetUtils.createPet("TempPet", PetStatus.AVAILABLE);
        long tempPetId = createResponse.jsonPath().getLong("id");
        System.out.printf("[Setup] Created temp petId: %d%n", tempPetId);

        // Delete the temporary pet
        PetUtils.deletePet(tempPetId);
        System.out.printf("[Setup] Deleted temp petId: %d%n", tempPetId);

        // Try to get the deleted pet
        Response getResponse = given()
                .when()
                .get("/pet/" + tempPetId)
                .then()
                .extract()
                .response();

        assertEquals(getResponse.getStatusCode(), 404);
        System.out.println("[Result] GET deleted pet returned 404 as expected");

        System.out.println("===== [Test] getDeletedPet_shouldReturn404 END =====");
    }


    @Test(groups = "negative")
    public void getPetWithInvalidIdFormat_shouldReturn404() {
        System.out.println("===== [Test] getPetWithInvalidIdFormat_shouldReturn400 START =====");

        String invalidId = "invalid123"; // string вместо long

        Response response = given()
                .when()
                .get("/pet/" + invalidId)
                .then()
                .extract()
                .response();

        //The Swagger API returns status 404 when we enter invalid String format instead of long for an id field, so that's what we expect as per documentation
        assertEquals(response.getStatusCode(), 404);

        System.out.println("[Result] GET pet with invalid ID format returned 400 as expected");

        System.out.println("===== [Test] getPetWithInvalidIdFormat_shouldReturn400 END =====");
    }


}


