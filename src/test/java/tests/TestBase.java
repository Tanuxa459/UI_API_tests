package tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;

public class TestBase {

    String login = "reg@mail.ru",
            password = "qwerty123";


    @BeforeAll
    static void setup() {

        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        RestAssured.baseURI = "https://demowebshop.tricentis.com";


        Configuration.pageLoadStrategy = "eager";

        String remoteHost = System.getProperty("remoteHost", "host");
        String userdata = System.getProperty("userdata", "userdata");
        Configuration.remote = userdata + remoteHost + "/wd/hub";
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "114");
        Configuration.browserSize = System.getProperty("browserSize", "1920×1080");
        //SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
//                "enableVNC", true,
//                "enableVideo", true
//        ));
//        Configuration.browserCapabilities = capabilities;
    }








    String authMetod(String login,String password) {


        String authCookieKey = "NOPCOMMERCE.AUTH";
        String authCookieValue = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", login)
                .formParam("Password", password)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie(authCookieKey);

        return authCookieValue;
    }


}
