package com.diliana.api.tests;

import com.diliana.api.base.BaseTest;
import com.diliana.api.utils.UserUtils;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UserTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(UserTests.class);

    private String username; // for cleanup after each test

    // ================= POSITIVE TESTS =================

    @Test(groups = "positive")
    public void createUser_shouldReturn200() {
        logger.info("===== [Test] createUser_shouldReturn200 START =====");

        username = UserUtils.generateRandomUsername();

        Response response = UserUtils.createUser(
                username,
                "Diliana",
                "Todorova",
                "didi@mail.com",
                "123456"
        );

        assertEquals(response.getStatusCode(), 200);

        logger.info("[Result] User created: {}", username);
        logger.info("===== [Test] createUser_shouldReturn200 END =====");
    }

    @Test(groups = "positive")
    public void createAndGetUser_shouldMatch() {
        logger.info("===== [Test] createAndGetUser_shouldMatch START =====");

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

        logger.info("[Result] User fetched successfully: {}", username);
        logger.info("===== [Test] createAndGetUser_shouldMatch END =====");
    }

    // ================= NEGATIVE TESTS =================

    @Test(groups = "negative")
    public void getNonExistingUser_shouldReturn404() {
        logger.info("===== [Test] getNonExistingUser_shouldReturn404 START =====");

        username = "non_existing_user_123456";

        Response response = UserUtils.getUser(username);

        assertEquals(response.getStatusCode(), 404);

        logger.info("[Result] 404 returned as expected for username={}", username);
        logger.info("===== [Test] getNonExistingUser_shouldReturn404 END =====");
    }

    @Test(groups = "negative")
    public void deleteNonExistingUser_shouldReturn404() {
        logger.info("===== [Test] deleteNonExistingUser_shouldReturn404 START =====");

        username = "ghost_user_999";

        Response response = UserUtils.deleteUser(username);

        assertEquals(response.getStatusCode(), 404);

        logger.info("[Result] Delete non-existing user returned 404 for username={}", username);
        logger.info("===== [Test] deleteNonExistingUser_shouldReturn404 END =====");
    }

    // ================= CLEANUP =================

    @AfterMethod
    public void cleanup() {
        if (username != null) {
            UserUtils.deleteUser(username);
            logger.info("[Cleanup] User deleted (if existed): {}", username);
            username = null;
        }
    }
}
