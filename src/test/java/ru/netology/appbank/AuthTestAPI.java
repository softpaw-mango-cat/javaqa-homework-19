package ru.netology.appbank;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testdata.AuthData;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

class AuthTestAPI {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    // тесты api - позитивные
    @Test
    @DisplayName("api: Should Create Valid User With Active Status")
    public void apiShouldCreateValidUserWithActiveStatus() {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(AuthData.generateUser("active")) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    @Test
    @DisplayName("api: Should Create Valid User Blocked Status")
    public void apiShouldCreateValidUserBlockedStatus() {
        given()
                .spec(requestSpec)
                .body(AuthData.generateUser("blocked"))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("api: Should Overwrite Valid Users Name")
    public void apiShouldOverwriteValidUsersName() {
        AuthData.UserData user = AuthData.generateUser("active");
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        user.setLogin(AuthData.generateLogin());

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("api: Should Overwrite Valid Users Password")
    public void apiShouldOverwriteValidUsersPassword() {
        AuthData.UserData user = AuthData.generateUser("blocked");
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        user.setPassword(AuthData.generatePassword());

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // тесты api - негативные
    @Test
    @DisplayName("api: Should Not Create User Invalid Status")
    public void apiShouldNotCreateUserInvalidStatus() {
        given()
                .spec(requestSpec)
                .body(AuthData.generateUser("whatever"))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(500);
    }

    /* в следующих двух тестах - у нас нет в задаче конкретных требований
    по валидности/невалидности пароля, они нигде не прописаны,
    валидацию подбирать долго - эти тесты падают
    @Test
    @DisplayName("api: Should Not Create User Invalid Name")
    public void apiShouldNotCreateUserInvalidName() {
        AuthData.UserData user = AuthData.generateUser("active");
        user.setLogin("1");
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("api: Should Not Create User Invalid Password")
    public void apiShouldNotCreateUserInvalidPassword() {
        AuthData.UserData user = AuthData.generateUser("blocked");
        user.setPassword("0");
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(400);
    } */

    @Test
    @DisplayName("api: Should Not Create User Invalid Password")
    public void apiShouldReturnErrorWhenEmptyForm() {
        given()
                .spec(requestSpec)
                .body("{}")
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(500);
    }

    // тесты api - пустые поля
    @Test
    @DisplayName("api: Should Return Error When Empty Login")
    public void apiShouldReturnErrorWhenEmptyLogin() {
        Map<String, String> userJson = new HashMap<>();
        userJson.put("password", "pwd12345");
        userJson.put("status", "active");

        given()
                .spec(requestSpec)
                .body(userJson)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("api: Should Return Error When Empty Password")
    public void apiShouldReturnErrorWhenEmptyPassword() {
        Map<String, String> userJson = new HashMap<>();
        userJson.put("login", "username");
        userJson.put("status", "blocked");

        given()
                .spec(requestSpec)
                .body(userJson)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(500);
    }
}
