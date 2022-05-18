package dneonline;

import Specifications.Specifications;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class SoapTest {
    private final static String URL = "http://www.dneonline.com/";

    @Test
    public void addTest() throws IOException {
        Specifications.installSpecification(Specifications.requestSoapSpecification(URL),Specifications.responseSpecification(200));

        String file = "src/test/resources/SoapReqAdd.xml";
        String body = new String(Files.readAllBytes(Paths.get(file)));

            Response response = given()
                    .body(body)
                    .when()
                    .post("calculator.asmx")
                    .then().log().body()
                    .extract().response();

        XmlPath xmlPath = new XmlPath(response.asString());
        String actual = xmlPath.getString("AddResult");
        Assert.assertEquals("5",actual);
    }
}
