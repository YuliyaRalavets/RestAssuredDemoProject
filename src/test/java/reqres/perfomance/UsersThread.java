package reqres.perfomance;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UsersThread extends Thread {

    private int userNumber;
    public static int failuresCount;
    public static Map<Integer, Long> responseTime = Collections.synchronizedMap(new HashMap<>());

    public UsersThread(int userNumber){
        this.userNumber = userNumber;
    }

    @Override
    public void run() {
        ValidatableResponse response = given()
                .baseUri("https://reqres.in")
                .contentType(ContentType.JSON)
                .basePath("/api/users")
                .param("page", 2)
                .when()
                .get()
                .then();

        if (response.extract().statusCode() != 200)
            failuresCount++;

        responseTime.put(userNumber,response.extract().time());
    }
}
