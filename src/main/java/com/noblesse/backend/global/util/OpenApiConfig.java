package com.noblesse.backend.global.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Triplay API Document")
                        .description("Triplay API 명세서")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Kamil Lee")
                                .email("parousia0918@gmail.com")
                        )
                        .termsOfService("https://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                )
                // API 가 배포된 서버들을 정의함
                .servers(List.of(
                        new Server().url("https://localhost:8443").description("Local Server"), // 로컬 서버
                        new Server().url("https://triplay.play").description("Production Server") // 프로덕션 서버
                ))
                // API 태그를 지정하여 엔드포인트들을 그룹화함
                .tags(List.of(
                        new Tag().name("Post Query").description("Post 관련 Query Controller"),
                        new Tag().name("Post Command").description("Post 관련 Command Controller"),
                        new Tag().name("Clip Query").description("Clip 관련 Query Controller"),
                        new Tag().name("Clip Command").description("Clip 관련 Command Controller"),
                        new Tag().name("Trip Query").description("Trip 관련 Query Controller"),
                        new Tag().name("Trip Command").description("Trip 관련 Command Controller")
                ));
    }
}
