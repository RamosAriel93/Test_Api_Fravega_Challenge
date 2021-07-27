import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.logging.Logger;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestGetApiBreweries {

    private static final Logger LOGGER = Logger.getLogger("InfoLogging");

    @Before
    public void setURLBase() {
        RestAssured.baseURI = "https://api.openbrewerydb.org/breweries";
    }

    @Test
    public void testApiBreweriesLagunitas()
    {
        LOGGER.info("Se busca por queryParam las que contengan lagunitas");
        Response response = given().
                queryParam("query", "lagunitas")
                .get(baseURI + "/autocomplete")
                .then().extract().response();

        LOGGER.info("Se filtra por nombre Lagunitas Brewing Co");
        JsonPath jsonPath = response.jsonPath();
        int count = jsonPath.getInt("name.size()");
        String name = "Lagunitas Brewing Co";

        for(int i = 0; i < count; i++)
        {
            String search = jsonPath.getString("name["+ i +"]");
            if(search.equalsIgnoreCase(name))
            {
                jsonPath.getString("name["+ i +"]");
            }
        }

        LOGGER.info("Se filtra por id, aquellas que contengan estado California y se validan los datos, id, name, street, phone");
        given()
                .get(baseURI + "/12040")
                .then()
                .body("id", equalTo(12040))
                .body("name", equalTo("Lagunitas Brewing Co"))
                .body("street", equalTo("1280 N McDowell Blvd"))
                .body("phone", equalTo("7077694495"))
                .extract()
                .response().then().log().all();
    }

    @After
    public void afterClass() {
        System.out.println("Fin del test");
    }
}