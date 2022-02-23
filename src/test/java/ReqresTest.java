import io.restassured.http.ContentType;
import org.checkerframework.checker.units.qual.A;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTest {

    private final static String URL = "https://reqres.in/";

    @Test
    public void testGetNoPojo() {
        io.restassured.response.Response response =  given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "api/users/2")
                .then().log().all()
                .statusCode(200)
                .body("data.id", Matchers.equalTo(2))
                .body("support.url",notNullValue())
                .extract().response();

    }

    @Test
    public void testPostNoPojo () {
        Map<String,String> users = new HashMap<>();
        users.put("name","morpheus");
        users.put("job","leader");
        given()
                .body(users)
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .body("id",notNullValue());

    }

    @Test
    public void testGetWithPojo () {
        Specifications.InstallSpec(Specifications.requestSpec(URL),Specifications.responseOk200());
        Resp resp = given()
                .get("api/users/2")
                .then().log().all()
                .extract().body().as(Resp.class);
        Assert.assertEquals(resp.getData().getId(),2);
        Assert.assertNotNull(resp.getSupport());
        Assert.assertTrue(resp.getSupport().getUrl().contains("https://reqres.in/#support-heading"));
    }


    @Test
    public void testPostWithPojo () {
        Specifications.InstallSpec(Specifications.requestSpec(URL),Specifications.responseOk200());
        int id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        PostReq postReq = new PostReq("eve.holt@reqres.in","pistol");
        PostResp postResp = given()
                .body(postReq)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().body().as(PostResp.class);
                Assert.assertNotNull(postResp);
                Assert.assertEquals(id,postResp.getId());
                Assert.assertEquals(token,postResp.getToken());

    }
    @Test
    public void testPostWithPojoNotSuccess () {
        Specifications.InstallSpec(Specifications.requestSpec(URL),Specifications.responseError400());
        String error = "Missing password";
        PostReqNotSuccess postReqNotSuccess = new PostReqNotSuccess("sydney@fife");
        PostRespNotSuccess postRespNotSuccess = given()
                .body(postReqNotSuccess)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().body().as(PostRespNotSuccess.class);
        Assert.assertEquals(postRespNotSuccess.error,error);
    }

    @Test
    public void sortedId () {
        Specifications.InstallSpec(Specifications.requestSpec(URL),Specifications.responseOk200());
        List <GetList> getLists = given()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data",GetList.class);
                getLists.stream().forEach(x->Assert.assertTrue(x.getYear()>=2000));
                Assert.assertTrue(getLists.stream().allMatch(x->x.getPantone_value().contains("-")));
            List <Integer> id = getLists.stream().map(GetList::getId).collect(Collectors.toList());
            List <Integer> sortedId = id.stream().sorted().collect(Collectors.toList());
            Assert.assertEquals(sortedId,id);
    }

    @Test
    public void delete () {
        Specifications.InstallSpec(Specifications.requestSpec(URL),Specifications.responseSpecStatus(204));
        given().delete("api/users/2").then().extract();
    }
int i;
}


