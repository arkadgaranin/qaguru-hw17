package com.gmail.arkgaranin;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@Owner("Arkadiy Garanin")
@Story("API тесты на https://reqres.in")
public class ReqresTests extends TestBase {

  @DisplayName("Успешная регистрация")
  @Severity(SeverityLevel.NORMAL)
  @Test
  void successfulRegistrationTest() {
    String data = "{ \"email\": \"eve.holt@reqres.in\", " +
        "\"password\": \"pistol\" }";

    given()
        .contentType(ContentType.JSON)
        .body(data)
        .when()
        .post("/api/register")
        .then()
        .statusCode(200)
        .body("id", is(4), "token", is("QpwL5tke4Pnpja7X4"));
  }

  @DisplayName("Не успешная регистрация")
  @Severity(SeverityLevel.NORMAL)
  @Test
  void unsuccessfulRegistrationTest() {
    String data = "{ \"email\": \"sydney@fife\"}";

    given()
        .contentType(ContentType.JSON)
        .body(data)
        .when()
        .post("/api/register")
        .then()
        .statusCode(400)
        .body("error", is("Missing password"));
  }

  @DisplayName("Проверка значений из json-а списка пользователей")
  @Severity(SeverityLevel.NORMAL)
  @Test
  void listUsersTest() {
    given()
        .when()
        .get("/api/users?page=2")
        .then()
        .statusCode(200)
        .body("total", is(12))
        .body("data.email[3]", is("byron.fields@reqres.in"),
            "data.first_name[3]", is("Byron"),
            "data.last_name[3]", is("Fields"));
  }

  @DisplayName("Создание пользователя")
  @Severity(SeverityLevel.NORMAL)
  @Test
  void createUserTest() {
    String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

    given()
        .contentType(ContentType.JSON)
        .body(data)
        .when()
        .post("/api/users")
        .then()
        .statusCode(201)
        .body("name", is("morpheus"), "job", is("leader"));
  }

  @DisplayName("Обновление пользователя")
  @Severity(SeverityLevel.NORMAL)
  @Test
  void updateUserTest() {
    String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

    given()
        .contentType(ContentType.JSON)
        .body(data)
        .when()
        .put("/api/users/2")
        .then()
        .statusCode(200)
        .body("name", is("morpheus"), "job", is("zion resident"));
  }

  @DisplayName("Удаление пользователя")
  @Severity(SeverityLevel.NORMAL)
  @Test
  void deleteUserTest() {
    given()
        .when()
        .delete("/api/users/2")
        .then()
        .statusCode(204);
  }
}
