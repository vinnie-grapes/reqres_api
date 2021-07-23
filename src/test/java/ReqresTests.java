import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresTests {

    static final String BASE_URL = "https://reqres.in/api/";
    static final String FIRST_NAME = "lindsay.ferguson@reqres.in";

    Faker faker = new Faker();
    String password = faker.pokemon().name();


    @Test
    void checkFirstResourceName() {
        get(BASE_URL + "unknown")
                .then()
                .statusCode(200)
                .body("data.name[0]", is("cerulean"))
                .body("data.color[0]", is("#98B2D1"));
    }

    @Test
    void ResourceNotFound() {
        get(BASE_URL + "unknown/200")
                .then()
                .statusCode(404);
    }

    @Test
    void registerUser() {
        given()
                .contentType(JSON)
                .body("{\"email\": " + "\"" + FIRST_NAME + "\"" + "," +
                        "\"password\": " + "\"" + password + "\"}")
                .when()
                .post(BASE_URL + "register")
                .then()
                .statusCode(200)
                .body("id", is(8))
                .body("token", is("QpwL5tke4Pnpja7X8"));
    }

    @Test
    void unsuccessfulRegistration() {
        given()
                .contentType(JSON)
                .body("{\"email\": \"sydney@fife\"}")
                .when()
                .post(BASE_URL + "register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void loginUser() {
        given()
                .contentType(JSON)
                .body("{\"email\": " + "\"" + FIRST_NAME + "\"" + "," +
                        "\"password\": " + "\"" + password + "\"}")
                .when()
                .post(BASE_URL + "register")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X8"));
    }

    @Test
    void unsuccessfulLogin() {
        given()
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\"}")
                .when()
                .post(BASE_URL + "login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void checkSelenoidStatus() {
        given().auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then().statusCode(200)
                .and().body("value.ready", is(true));
    }
}