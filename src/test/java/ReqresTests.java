import com.github.javafaker.Faker;
import models.Register;
import models.ResourceData;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests {

    static final String FIRST_NAME = "lindsay.ferguson@reqres.in";

    Faker faker = new Faker();
    String password = faker.pokemon().name();


    @Test
    void checkFirstResourceNameGroovy() {
        Specs.requestSpecification
                .when()
                .get("/unknown")
                .then()
                .spec(Specs.responseToUpdate)
                .log().body()
                .body("data.name[0]", is("cerulean"))
                .body("data.color[0]", is("#98B2D1"))
                .body("data.findAll{it.year < 2001}.year",
                        hasItem(2000));
    }

    @Test
    void checkFirstResourceNameLombok() {
        ResourceData resp = given()
                .spec(Specs.requestSpecification)
                .when()
                .get("/unknown/2")
                .then()
                .spec(Specs.responseToUpdate)
                .log().body()
                .extract().as(ResourceData.class);

        assertEquals("fuchsia rose", resp.getResource().getName());
        assertEquals("#C74375", resp.getResource().getColor());
    }


    @Test
    void ResourceNotFound() {
        Specs.requestSpecification
                .get("/unknown/200")
                .then()
                .spec(Specs.responseNotFound);
    }

    @Test
    void registerUser() {
        Specs.requestSpecification
                .body("{\"email\": " + "\"" + FIRST_NAME + "\"" + "," +
                        "\"password\": " + "\"" + password + "\"}")
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseToUpdate)
                .body("id", is(8))
                .body("token", is("QpwL5tke4Pnpja7X8"));
    }

    @Test
    void registerUserLombok() {
        Register register = given()
                .spec(Specs.requestSpecification)
                .body("{\"email\": " + "\"" + FIRST_NAME + "\"" + "," +
                        "\"password\": " + "\"" + password + "\"}")
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseToUpdate)
                .extract().as((Type) Register.class);

        assertEquals("QpwL5tke4Pnpja7X8", register.getToken());
        assertEquals(8, register.getId());
    }

    @Test
    void unsuccessfulRegistration() {
        Specs.requestSpecification
                .body("{\"email\": \"sydney@fife\"}")
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseToFailed)
                .body("error", is("Missing password"));
    }

    @Test
    void loginUser() {
        Specs.requestSpecification
                .body("{\"email\": " + "\"" + FIRST_NAME + "\"" + "," +
                        "\"password\": " + "\"" + password + "\"}")
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseToUpdate)
                .body("token", is("QpwL5tke4Pnpja7X8"));
    }

    @Test
    void unsuccessfulLogin() {
        Specs.requestSpecification
                .body("{\"email\": \"eve.holt@reqres.in\"}")
                .when()
                .post("/login")
                .then()
                .spec(Specs.responseToFailed)
                .body("error", is("Missing password"));
    }

    @Test
    void checkSelenoidStatus() {
        given().auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .spec(Specs.responseToUpdate)
                .body("value.ready", is(true));
    }
}