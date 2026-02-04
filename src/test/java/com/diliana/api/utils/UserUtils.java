package com.diliana.api.utils;

import io.restassured.response.Response;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class UserUtils {


    public static String generateRandomUsername() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static Response createUser(String username,
                                      String firstName,
                                      String lastName,
                                      String email,
                                      String password) {

        String requestBody = String.format("""
                {
                  "username": "%s",
                  "firstName": "%s",
                  "lastName": "%s",
                  "email": "%s",
                  "password": "%s",
                  "userStatus": 0
                }
                """, username, firstName, lastName, email, password);

        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/user")
                .then()
                .extract()
                .response();
    }

    public static Response getUser(String username) {
        return given()
                .when()
                .get("/user/" + username)
                .then()
                .extract()
                .response();
    }

    public static Response deleteUser(String username) {
        return given()
                .when()
                .delete("/user/" + username)
                .then()
                .extract()
                .response();
    }
}
