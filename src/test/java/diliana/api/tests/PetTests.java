package diliana.api.tests;

import diliana.api.base.BaseTest;
import diliana.api.utils.ApiUtils;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.assertEquals;

public class PetTests extends BaseTest {

    private long petId;

    @BeforeClass
    public void init() {
        // Взимаме случайно petId след като BaseTest вече е настроен
        petId = ApiUtils.getRandomAvailablePetId();
        System.out.println("Using random petId: " + petId);
    }

    @Test
    public void getRandomPetById_shouldReturn200() {
        Response petResponse = given()
                .when()
                .get("/pet/" + petId)
                .then()
                .extract()
                .response();

        assertEquals(petResponse.getStatusCode(), 200);
        String petName = petResponse.jsonPath().getString("name");
        System.out.println("Pet name: " + petName);
    }
}
