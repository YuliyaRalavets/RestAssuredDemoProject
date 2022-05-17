package reqres;

import Specifications.Specifications;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import reqres.pojo.users.UserCreateData;
import reqres.pojo.users.UserCreateDataResponse;
import reqres.pojo.users.UserData;
import reqres.pojo.registration.RegisterData;
import reqres.pojo.registration.SuccessRegisterData;
import reqres.pojo.registration.UnSuccessRegisterData;

import java.util.List;

import static io.restassured.RestAssured.given;

public class PojoTests {
    private final static String URL = "https://reqres.in/";

    @BeforeTest
    public void setFilter(){
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void checkAvatarsContainsIdTest() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecification(200));

        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().body()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(item-> Assert.assertTrue(item.getAvatar().contains(item.getId().toString())));
    }

    @Test
    public void successUserRegistrationTest(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecification(200));

        RegisterData user = new RegisterData("eve.holt@reqres.in", "pistol");

        SuccessRegisterData successRegisterData = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().body()
                .extract().as(SuccessRegisterData.class);


        Assert.assertNotNull(successRegisterData.getId());
        Assert.assertNotNull(successRegisterData.getToken());
        Assert.assertEquals(4, successRegisterData.getId());
        Assert.assertEquals("QpwL5tke4Pnpja7X4",successRegisterData.getToken());
    }

    @Test
    public void unSuccessUserRegistrationTest(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecification(400));

        RegisterData user = new RegisterData("sydney@fife", null);

        UnSuccessRegisterData unSuccessRegisterData = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().body()
                .extract().as(UnSuccessRegisterData.class);

        Assert.assertEquals("Missing password", unSuccessRegisterData.getError());
    }

    @Test
    public void createUserTest() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecification(201));

        UserCreateData user = new UserCreateData("morpheus","leader");

        UserCreateDataResponse response = given()
                .body(user)
                .when()
                .post("api/users")
                .then().log().body()
                .extract()
                .as(UserCreateDataResponse.class);

        Assert.assertEquals("morpheus",response.getName());
        Assert.assertEquals("leader", response.getJob());
    }
}
