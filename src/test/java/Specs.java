import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class Specs {

    public static RequestSpecification requestSpecification = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .contentType(ContentType.JSON)
            .log().all();

    public static ResponseSpecification responseToUpdate = new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .expectStatusCode(200)
            .log(LogDetail.ALL)
            .build();

    public static ResponseSpecification responseNotFound = new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .expectStatusCode(404)
            .log(LogDetail.ALL)
            .build();

    public static ResponseSpecification responseToFailed = new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .expectStatusCode(400)
            .log(LogDetail.ALL)
            .build();
}