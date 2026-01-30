package diliana.api.base;


import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetTests extends BaseTest {

    @Test
    public void getPetById() {
        given().pathParam("petId", 1).
               when().get("/pet/{petId}")
               .then().statusCode(200).body("id", equalTo(1));
    }
}