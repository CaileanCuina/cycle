package vacation.planner.cycle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Cycle Planner API")
                        .version("1.0")
                        .description("API für Termin Plannung auf den Monat abgestimmt"));
    }
}
