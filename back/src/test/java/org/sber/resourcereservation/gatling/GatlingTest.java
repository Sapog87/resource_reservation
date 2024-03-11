package org.sber.resourcereservation.gatling;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GatlingTest extends Simulation {
    public GatlingTest() {
        setUp(POST_SCENARIO_BUILDER
                .injectClosed(constantConcurrentUsers(50).during(60))
                .throttle(reachRps(10000).in(1), holdFor(60))
                .protocols(HTTP_PROTOCOL_BUILDER));
    }

    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();

    private static final Iterator<Map<String, Object>> FEED_DATA = setupTestFeedData();

    private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildPostScenario();

    private static HttpProtocolBuilder setupProtocolForSimulation() {
        return http.baseUrl("http://localhost:8080");
    }

    private static Iterator<Map<String, Object>> setupTestFeedData() {
        LocalDateTime startOfYear = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(2024, 12, 24, 23, 59);
        List<String> users = List.of("aaa", "bbb", "ccc", "ddd", "eee");
        List<String> resources = List.of("aaa", "bbb", "ccc", "ddd", "eee");
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {
            LocalDateTime randomStartDate = generateRandomDateTime(startOfYear, endOfYear);
            LocalDateTime randomEndDate = generateRandomDateTime(randomStartDate, randomStartDate.plusDays(1));
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("user", users.get(ThreadLocalRandom.current().nextInt(0, 5)));
            stringObjectMap.put("resource", resources.get(ThreadLocalRandom.current().nextInt(0, 5)));
            stringObjectMap.put("start", randomStartDate);
            stringObjectMap.put("end", randomEndDate);
            return stringObjectMap;
        }).iterator();
        return iterator;
    }

    private static LocalDateTime generateRandomDateTime(LocalDateTime start, LocalDateTime end) {
        long startEpochSecond = start.toEpochSecond(java.time.ZoneOffset.UTC);
        long endEpochSecond = end.toEpochSecond(java.time.ZoneOffset.UTC);

        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);

        return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, java.time.ZoneOffset.UTC);
    }

    private static ScenarioBuilder buildPostScenario() {
        String acquireBody = "{\"user\":{\"name\":\"#{user}\"},\"resource\":{\"name\":\"#{resource}\"},\"start\":\"#{start}\",\"end\":\"#{end}\"}";

        return CoreDsl
                .scenario("Load Test Acquire Resources")
                .feed(FEED_DATA)
                .exec(http("acquire")
                        .post("/resources/acquire")
                        .basicAuth("#{user}", "aaa")
                        .header("Content-Type", "application/json")
                        .body(StringBody(acquireBody))
                        .check(status().in(List.of(200, 409)))
                );
    }
}
