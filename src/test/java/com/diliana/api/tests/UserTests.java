package com.diliana.api.tests;

import com.diliana.api.base.BaseTest;
import com.diliana.api.utils.UserUtils;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UserTests extends BaseTest {

    private String username; // за cleanup след всеки тест

    // ================= POSITIVE TESTS =================

    @Test(groups = "positive")
    public void createUser_shouldReturn200() {
        System.out.println("===== [Test] createUser_shouldReturn200 START =====");

        username = UserUtils.generateRandomUsername();

        Response response = UserUtils.createUser(
                username,
                "Diliana",
                "Detcheva",
                "didi@mail.com",
                "123456"
        );

        assertEquals(response.getStatusCode(), 200);

        System.out.println("[Result] User created: " + username);
        System.out.println("===== [Test] createUser_shouldReturn200 END =====");
    }

    @Test(groups = "positive")
    public void createAndGetUser_shouldMatch() {
        System.out.println("===== [Test] createAndGetUser_shouldMatch START =====");

        username = UserUtils.generateRandomUsername();

        UserUtils.createUser(
                username,
                "Ana",
                "Ivanova",
                "ana@mail.com",
                "pass123"
        );

        Response getResponse = UserUtils.getUser(username);

        assertEquals(getResponse.getStatusCode(), 200);
        assertEquals(getResponse.jsonPath().getString("username"), username);
        assertEquals(getResponse.jsonPath().getString("firstName"), "Ana");

        System.out.println("[Result] User fetched successfully");
        System.out.println("===== [Test] createAndGetUser_shouldMatch END =====");
    }

    // ================= NEGATIVE TESTS =================

    @Test(groups = "negative")
    public void getNonExistingUser_shouldReturn404() {
        System.out.println("===== [Test] getNonExistingUser_shouldReturn404 START =====");

        username = "non_existing_user_123456";

        Response response = UserUtils.getUser(username);

        assertEquals(response.getStatusCode(), 404);

        System.out.println("[Result] 404 returned as expected");
        System.out.println("===== [Test] getNonExistingUser_shouldReturn404 END =====");
    }

    @Test(groups = "negative")
    public void deleteNonExistingUser_shouldReturn404() {
        System.out.println("===== [Test] deleteNonExistingUser_shouldReturn404 START =====");

        username = "ghost_user_999";

        Response response = UserUtils.deleteUser(username);

        assertEquals(response.getStatusCode(), 404);

        System.out.println("[Result] Delete non-existing user returned 404");
        System.out.println("===== [Test] deleteNonExistingUser_shouldReturn404 END =====");
    }

    // ================= CLEANUP =================

    @AfterMethod
    public void cleanup() {
        if (username != null) {
            UserUtils.deleteUser(username);
            System.out.println("[Cleanup] User deleted (if existed): " + username);
            username = null;
        }
    }
}
