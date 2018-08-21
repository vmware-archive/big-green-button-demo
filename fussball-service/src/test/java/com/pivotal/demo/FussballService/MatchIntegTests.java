package com.pivotal.demo.FussballService;

import org.junit.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class MatchIntegTests {
    @Test public void
    matches_resource_returns_200_with_page0_results() {
        String url = System.getenv("API_PREFIX") + "/matches"; 
        System.out.println("Retrieving data from: " + url);
        when().
                get(url).
        then().
                statusCode(200).
                body(
                    "_embedded.matches.size()", equalTo(20),
                    "page.size", equalTo(20),
                    "page.number", equalTo(0)
                );
    }

}
