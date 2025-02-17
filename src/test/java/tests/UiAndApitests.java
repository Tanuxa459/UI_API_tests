package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import models.ResponceAddModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.withTagAndText;
import static org.assertj.core.api.Assertions.assertThat;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@Tag("ApiUiTests")
public class UiAndApitests extends TestBase {



    @Test
    void loginWithApiTest() {

        step("Get authorization cookie by api and set it to browser", () -> {
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

            open("https://demowebshop.tricentis.com/Content/jquery-ui-themes/smoothness/images/ui-bg_flat_75_ffffff_40x100.png");
            Cookie authCookie = new Cookie(authCookieKey, authCookieValue);
            getWebDriver().manage().addCookie(authCookie);
        });

        step("Open main page", () ->
                open(""));

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }


    @Test
    void loginWithAddApiTest() {

        String authCookieValue = authMetod(login, password);
        String authCookieKey = "NOPCOMMERCE.AUTH";


        open("/Content/jquery-ui-themes/smoothness/images/ui-bg_flat_75_ffffff_40x100.png");
        Cookie authCookie = new Cookie(authCookieKey, authCookieValue);
        getWebDriver().manage().addCookie(authCookie);


        ResponceAddModel responceAdd = step("Make request", () ->
                given()
                        .cookie(authCookieKey, authCookieValue)
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .when()
                        .post("/addproducttocart/catalog/45/1/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(ResponceAddModel.class));


        step("Open main page", () ->
                open(""));

        step("Verify successful authorization", () ->
                $(".cart-qty").shouldHave(text(responceAdd.getUpdatetopcartsectionhtml())));

    }

    @Test
    void loginDeleteProductTest() {

        String authCookieValue = authMetod(login, password);
        String authCookieKey = "NOPCOMMERCE.AUTH";

        open("/Content/jquery-ui-themes/smoothness/images/ui-bg_flat_75_ffffff_40x100.png");
        Cookie authCookie = new Cookie(authCookieKey, authCookieValue);
        getWebDriver().manage().addCookie(authCookie);

        given()
                .cookie(authCookieKey, authCookieValue)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .when()
                .post("/addproducttocart/catalog/45/1/1")
                .then()
                .log().all()
                .statusCode(200);


        step("Open main page", () ->
                open("/cart"));


        $(byTagAndText("span", "Remove:")).sibling(0).click();
        $$("input").findBy(attribute("value", "Update shopping cart")).click();

    }


}
