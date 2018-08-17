package com.pivotal.demo.FussballService;

import org.junit.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class MatchIntegTests {
    @Test public void
    matches_resource_returns_200_with_page0_results() {
        when().
                get("https://fussball-service-blue.cfapps.io/matches").
        then().
                statusCode(200).
                body(
                    "_embedded.matches.size()", equalTo(20),
                    "page.size", equalTo(20),
                    "page.number", equalTo(0)
                );
    }

}
