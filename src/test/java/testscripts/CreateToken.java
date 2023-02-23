package testscripts;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constants.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.request.createBooking.Bookingdates;
import pojo.request.createBooking.CreateBookingRequest;

public class CreateToken {

	String token;

	@BeforeMethod
	public void generateToken() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		Response response = RestAssured.given()
				// .log().all()
				.headers("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\" : \"admin\",\r\n" + "    \"password\" : \"password123\"\r\n" + "}")
				.when().post("/auth").then().log().all().extract().response();
		//Printing Response Status Code
		System.out.println("Response Status Code" + response.statusCode());
		//Asserting Status Code with 200
		Assert.assertEquals(response.statusCode(), 200);
		//Printing response as Preety
		System.out.println(response.asPrettyString());
		//Genarating Token
		token = response.jsonPath().getString("token");
		System.out.println("TOKEN: " + token);
	}

	/*@Test
	public void createBookingTest() {
		Response response = RestAssured.given()
			.headers("Content-Type", "application/json")
			.body("'{\r\n" + 
					"    \"firstname\" : \"Jim\",\r\n" + 
					"    \"lastname\" : \"Brown\",\r\n" + 
					"    \"totalprice\" : 111,\r\n" + 
					"    \"depositpaid\" : true,\r\n" + 
					"    \"bookingdates\" : {\r\n" + 
					"        \"checkin\" : \"2018-01-01\",\r\n" + 
					"        \"checkout\" : \"2019-01-01\"\r\n" + 
					"    },\r\n" + 
					"    \"additionalneeds\" : \"Breakfast\"\r\n" + 
					"}'")
			.when()
			.post("/booking");
		//Asserting status code. Created static variable 'OK in class StatusCode
		System.out.println("Response Status Code: " +response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), StatusCode.OK);
		
		System.out.println("Response Status Line: " +response.getStatusLine());
	}*/
	
	@Test
	public void createBookingTestWithPOJO() {
		//RestAssured.baseURI = "https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io/booking";
		Bookingdates bookingDates = new Bookingdates();
		bookingDates.setCheckin("2023-02-02");
		bookingDates.setCheckout("2023-02-05");
		
		CreateBookingRequest payload = new CreateBookingRequest();
		payload.setFirstname("Mickey");
		payload.setLastname("Mouse");
		payload.setTotalprice(123);
		payload.setDepositpaid(true);
		payload.setAdditionalneeds("BreakFast");
		payload.setBookingdates(bookingDates);
		
		
		Response response = RestAssured.given()
			.headers("Content-Type", "application/json")
			.header("Accept", "application/json")
			.body(payload)
			.when()
			.post("/booking");
		//Asserting status code. Created static variable 'OK in class StatusCode
		System.out.println("Response Status Code: " +response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), StatusCode.OK);
		
		int bookingId = response.jsonPath().getInt("bookingid");
		System.out.println("Booking ID: " +bookingId);
		Assert.assertTrue(bookingId>0);
		
		Assert.assertEquals(response.jsonPath().getString("booking.firstname"), payload.getFirstname());
		Assert.assertEquals(response.jsonPath().getString("booking.lastname"), payload.getLastname());
		Assert.assertEquals(response.jsonPath().getInt("booking.totalprice"), payload.getTotalprice());
		Assert.assertEquals(response.jsonPath().getBoolean("booking.depositpaid"), payload.isDepositpaid());
		Assert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkin"), payload.getBookingdates().getCheckin());
		Assert.assertEquals(response.jsonPath().getString("booking.bookingdates.checkout"), payload.getBookingdates().getCheckout());
		Assert.assertEquals(response.jsonPath().getString("booking.totalprice"), payload.getTotalprice());
		
	}

	/*
	 * @Test public void createBookingTestInPlainMode() { RestAssured.baseURI =
	 * "https://restful-booker.herokuapp.com";
	 * 
	 * RequestSpecification reqSpec = RestAssured.given();
	 * 
	 * reqSpec.baseUri("https://restful-booker.herokuapp.com")
	 * .headers("Content-Type", "application/json") .body("{\r\n" +
	 * "    \"username\" : \"admin\",\r\n" +
	 * "    \"password\" : \"password123\"\r\n" + "}") .when() .post("/auth");
	 * 
	 * System.out.println("Response Status Code" + response.statusCode());
	 * Assert.assertEquals(response.statusCode(), 200);
	 * System.out.println(response.asPrettyString());
	 * 
	 * }
	 */

}
