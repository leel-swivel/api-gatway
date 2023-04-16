package com.hilltop.configuration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {


    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/user",
            "/api/v1/auth/token",
            "/eureka",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/swagger-resources",
            "/v3/api-docs",
            "/v2/api-docs"
    );

    public static Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));


    public RouteValidator() {

    }

}
