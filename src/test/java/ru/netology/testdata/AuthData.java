package ru.netology.testdata;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class AuthData {
    private static final Faker FAKER_EN = new Faker(new Locale("en"));
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static UserData generateUser(String status) {
        String login = generateLogin();
        String password = generatePassword();
        return new UserData(login, password, status);
    }

    public static String generateLogin() {
        return FAKER_EN.name().firstName();
    }

    public static String generatePassword() {
        return FAKER_EN.internet().password();
    }

    public static void registerUser(UserData user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Value
    public static class UserData {
        String login;
        String password;
        String status;
    }
}

