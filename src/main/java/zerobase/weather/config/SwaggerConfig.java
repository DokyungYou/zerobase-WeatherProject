package zerobase.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //  any() -> basePackage()로 변경했음 (error-controller 을 없애기 위함)
                .apis(RequestHandlerSelectors.basePackage("zerobase.weather"))
                //path 기준으로 swagger 에 표시될 api 를 정할 수 있는 부분 any()로 적을 시 기준을 두지 않고 모든 api 가 나오게끔함
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

    // http://localhost:8080/swagger-ui/index.html
    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("날씨 일기 프로젝트인데요. ")
                .description("날씨 일기를 CRUD 할 수 있는 백엔드 API 입니다!")
                .version("2.0")
                .build();

    }
}