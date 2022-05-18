package reqres;

import Specifications.Specifications;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class NoPojoTest {
    private final static String URL = "https://reqres.in/";

    @BeforeTest
    public void setFilter(){
        RestAssured.filters(new AllureRestAssured());
    }
    
    @Test(description = "Is avatars contains id")
    public void checkAvatarsContainsIdTest(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecification(200));

        Response response = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.getList("data.id");
        List<String> avatars = jsonPath.getList("data.avatar");

        for(int i=0; i<avatars.size(); i++){
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }
    }

    @Test(description = "Successful user registration")
    public void successUserRegistrationTest(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecification(200));

        Map<String,String> user = new HashMap<>();
        user.put("email","eve.holt@reqres.in");
        user.put("password","pistol");

        Response response = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        int id = jsonPath.get("id");
        String token = jsonPath.get("token");

        Assert.assertTrue(id==4 && token.equals("QpwL5tke4Pnpja7X4"));
    }

    @Test(description = "Unsuccessful user registration")
    public void unsuccessUserRegistrationTest(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecification(400));

        Map<String,String> user = new HashMap<>();
        user.put("email","sydney@fife");

        Response response = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.get("error");

        Assert.assertEquals("Missing password", error);
    }

    @Test(description = "Create user")
    public void createUserTest(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecification(201));

        Map<String,String> user = new HashMap<>();
        user.put("name","morpheus");
        user.put("job", "leader");

        Response response = given()
                .body(user)
                .when()
                .post("api/users")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String name = jsonPath.getString("name");
        String job = jsonPath.getString("job");

        Assert.assertEquals("morpheus", name);
        Assert.assertEquals("leader",job);
    }

    @Test(description = "Delete user")
    public void deleteUserTest(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecification(204));

         given()
                .when()
                .delete("/api/users/2")
                .then().log().all();
    }
}
