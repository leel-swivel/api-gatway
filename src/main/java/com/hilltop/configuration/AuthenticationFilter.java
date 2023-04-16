package com.hilltop.configuration;

import com.google.common.net.HttpHeaders;
import com.hilltop.configuration.utill.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    @Autowired
    private JwtUtil jwtUtil;

//    @Autowired
//    private RestTemplate template;

    public AuthenticationFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (RouteValidator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return missingAuthorizeHeader().then();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://AUTH-SERVICE//api/v1/auth/validate?token" + authHeader, String.class);
                    jwtUtil.validateToken(authHeader);

                } catch (HttpClientErrorException e) {
                    log.error("Invalid user name or password.");
                    return sendUserServiceErrorResponse(e).then();
                }
            }
            return chain.filter(exchange);
        });
    }


    private Mono<ResponseStatusException> sendUserServiceErrorResponse(HttpClientErrorException exception) {
        if (exception.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token", exception));
        return Mono.error(
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", exception));
    }

    private Mono<ResponseStatusException> missingAuthorizeHeader() {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorized header."));
    }

    public static class Config {
    }
}
