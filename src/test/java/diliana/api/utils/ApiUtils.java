package diliana.api.utils;

import io.restassured.response.Response;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class ApiUtils {

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

        // Използваме Long вместо Integer
        List<Long> petIds = response.jsonPath().getList("id", Long.class);

        if (petIds.isEmpty()) {
            throw new RuntimeException("No pets available");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(petIds.size());
        return petIds.get(randomIndex);
    }
}
