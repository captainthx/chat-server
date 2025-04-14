package com.yutsuki.chatserver.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfiguration {

    @Value("${api.server.url}")
    private String serverUrl;

    @Value("${api.server.env}")
    private String serverEnv;

    @Value("${api.server.name}")
    private String serverName;

    @Value("${api.server.version}")
    private String serverVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        var server = new Server()
                .url(serverUrl)
                .description(serverEnv);

        var info = new Info()
                .title(serverName)
                .version(serverVersion);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
