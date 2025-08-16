package org.flamingo.flamingohomeassingment;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.flamingo.flamingohomeassingment.api.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FlamingoHomeAssingmentApplicationTests {

    @Test
    @Order(1)
    void getUserList() {

        List<User> expectedUserList = new ArrayList<>();

        expectedUserList.add(new User(1, "Thor", "Odinson", "Strongest Avenger"));
        expectedUserList.add(new User(2, "Tony", "Stark", "Dr. Doom"));
        expectedUserList.add(new User(3, "Bruce", "Banned", "Hulk Smash"));

        List<User> actualUserList = new ArrayList<>();

        actualUserList = given().filter(new AllureRestAssured())
                .get("http://localhost:8080/users")
                .then()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .extract().body().jsonPath().getList(".", User.class);

        for  (int i = 0; i < actualUserList.size(); i++) {
            User actualUser = actualUserList.get(i);
            User expectedUser = expectedUserList.get(i);
            assertEquals(expectedUser.getId(), actualUser.getId());
            assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
            assertEquals(expectedUser.getLastName(), actualUser.getLastName());
            assertEquals(expectedUser.getUserName(), actualUser.getUserName());
        }
    }

    @Test
    @Order(2)
    void userAddedSuccessfully() throws JsonProcessingException, JSONException {

        User newUser = new User(4, "Peter", "Quill", "Star-Lord");

        JSONObject requestBody = new JSONObject();
        requestBody.put("id", newUser.getId());
        requestBody.put("firstName", newUser.getFirstName());
        requestBody.put("lastName", newUser.getLastName());
        requestBody.put("userName", newUser.getUserName());

        given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post("http://localhost:8080/users")
                .then()
                .statusCode(201);

        User actualUser = given().filter(new AllureRestAssured())
                .get("http://localhost:8080/users/byId?id=4")
                .then()
                .statusCode(200)
                .extract().body().as(User.class);

        assertEquals(newUser.getId(), actualUser.getId());
        assertEquals(newUser.getFirstName(), actualUser.getFirstName());
        assertEquals(newUser.getLastName(), actualUser.getLastName());
        assertEquals(newUser.getUserName(), actualUser.getUserName());
    }

    @Test
    @Order(3)
    void userUpdatedSuccessfully() throws JSONException {

        User updatedUser = new User(3, "Peter", "Parker", "Spider-Man");

        JSONObject requestBody = new JSONObject();
        requestBody.put("id", updatedUser.getId());
        requestBody.put("firstName", updatedUser.getFirstName());
        requestBody.put("lastName", updatedUser.getLastName());
        requestBody.put("userName", updatedUser.getUserName());

        given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put("http://localhost:8080/users")
                .then()
                .statusCode(200);

        User actualUser = given().filter(new AllureRestAssured())
                .get("http://localhost:8080/users/byId?id=3")
                .then()
                .statusCode(200)
                .extract().body().as(User.class);

        assertEquals(updatedUser.getId(), actualUser.getId());
        assertEquals(updatedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(updatedUser.getLastName(), actualUser.getLastName());
        assertEquals(updatedUser.getUserName(), actualUser.getUserName());
    }

    @Test
    @Order(4)
    void userDeletedSuccessfully() {

         List<User> preDeleteUserList = given().filter(new AllureRestAssured())
                .get("http://localhost:8080/users")
                .then()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .extract().body().jsonPath().getList(".", User.class);

        int preDeleteSize = preDeleteUserList.size();

        User userToDelete = preDeleteUserList.get(0);

        given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(1)
                .when()
                .delete("http://localhost:8080/users")
                .then()
                .statusCode(200);

        List<User> postDeleteUserList = given().filter(new AllureRestAssured())
                .get("http://localhost:8080/users")
                .then()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .extract().body().jsonPath().getList(".", User.class);

        int postDeleteSize = postDeleteUserList.size();
        assertEquals(preDeleteSize - 1, postDeleteSize);
        assertNull(postDeleteUserList.stream().filter(u -> Objects.equals(u.getId(), userToDelete.getId())).findFirst().orElse(null));
        assertNull(postDeleteUserList.stream().filter(u -> Objects.equals(u.getUserName(), userToDelete.getUserName())).findFirst().orElse(null));
    }
}
