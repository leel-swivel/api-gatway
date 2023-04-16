package com.hilltop.configuration;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@EnableAutoConfiguration

public class SwaggerConfig implements SwaggerResourcesProvider {

    @Override
    public List get() {
        List resources = new ArrayList();
        resources.add(swaggerResource("hotel-service", "/hotel-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("room-service", "/room-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("booking-service", "/booking-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("auth-service", "/auth-service/v2/api-docs", "2.0"));
        resources.add(swaggerResource("search-service", "/search-service/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }


}
