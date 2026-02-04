package com.diliana.api.tests;

import com.diliana.api.base.BaseTest;
import com.diliana.api.utils.PetUtils;
import com.diliana.api.enums.PetStatus;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class PetTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(PetTests.class);

    private long petId;

    @BeforeClass
    public void init() {
        petId = PetUtils.getRandomAvailablePetId();
        logger.info("===== [Setup] Random petId selected: {} =====", petId);
    }

    @Test(groups = "positive")
    public void getRandomPetById_shouldReturn200() {
        logger.info("===== [Test] getRandomPetById_shouldReturn200 START =====");

        Response petResponse = given()
                .when()
                .get("/pet/" + petId)
                .then()
                .extract()
                .response();

        assertEquals(petResponse.getStatusCode(), 200);
        String petName = petResponse.jsonPath().getString("name");

        logger.info("[Result] Pet name: {}", petName);
        logger.info("===== [Test] getRandomPetById_shouldReturn200 END =====");
    }

    @Test(groups = "positive")
    public void createPet_thenGetPet_shouldMatch() {
        logger.info("===== [Test] createPet_thenGetPet_shouldMatch START =====");

        Response createResponse = PetUtils.createPet("Rex", PetStatus.AVAILABLE);
        long createdPetId = createResponse.jsonPath().getLong("id");
        String createdPetName = createResponse.jsonPath().getString("name");

        logger.info("[Result] Created petId: {}, name: {}", createdPetId, createdPetName);

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

        logger.info("===== [Test] createPet_thenGetPet_shouldMatch END =====");
    }

    @Test(groups = "positive")
    public void updatePet_shouldChangeNameAndStatus() {
        logger.info("===== [Test] updatePet_shouldChangeNameAndStatus START =====");

        Response createResponse = PetUtils.createPet("Bella", PetStatus.AVAILABLE);
        long createdPetId = createResponse.jsonPath().getLong("id");
        logger.info("[Result] Created petId: {}, name: {}", createdPetId, createResponse.jsonPath().getString("name"));

        Response updateResponse = PetUtils.updatePet(createdPetId, "BellaUpdated", PetStatus.SOLD);
        assertEquals(updateResponse.getStatusCode(), 200);

        Response getResponse = given()
                .when()
                .get("/pet/" + createdPetId)
                .then()
                .extract()
                .response();

        assertEquals(getResponse.getStatusCode(), 200);
        assertEquals(getResponse.jsonPath().getString("name"), "BellaUpdated");
        assertEquals(getResponse.jsonPath().getString("status"), "sold");

        logger.info("[Result] Updated petId: {}, name: {}, status: {}",
                createdPetId,
                getResponse.jsonPath().getString("name"),
                getResponse.jsonPath().getString("status"));

        logger.info("===== [Test] updatePet_shouldChangeNameAndStatus END =====");
    }

    @Test(groups = "negative")
    public void getDeletedPet_shouldReturn404() {
        logger.info("===== [Test] getDeletedPet_shouldReturn404 START =====");

        Response createResponse = PetUtils.createPet("TempPet", PetStatus.AVAILABLE);
        long tempPetId = createResponse.jsonPath().getLong("id");
        logger.info("[Setup] Created temp petId: {}", tempPetId);

        PetUtils.deletePet(tempPetId);
        logger.info("[Setup] Deleted temp petId: {}", tempPetId);

        Response getResponse = given()
                .when()
                .get("/pet/" + tempPetId)
                .then()
                .extract()
                .response();

        assertEquals(getResponse.getStatusCode(), 404);
        logger.info("[Result] GET deleted pet returned 404 as expected");

        logger.info("===== [Test] getDeletedPet_shouldReturn404 END =====");
    }

    @Test(groups = "negative")
    public void getPetWithInvalidIdFormat_shouldReturn404() {
        logger.info("===== [Test] getPetWithInvalidIdFormat_shouldReturn400 START =====");

        String invalidId = "invalid123";

        Response response = given()
                .when()
                .get("/pet/" + invalidId)
                .then()
                .extract()
                .response();

        assertEquals(response.getStatusCode(), 404);
        logger.info("[Result] GET pet with invalid ID format returned 404 as expected");

        logger.info("===== [Test] getPetWithInvalidIdFormat_shouldReturn400 END =====");
    }
}
