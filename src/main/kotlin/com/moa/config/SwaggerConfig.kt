package com.moa.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .securitySchemes(listOf(apiKey()))
            .securityContexts(listOf(securityContext()))
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.moa"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(this.metaInfo())
    }

    private fun apiKey(): ApiKey {
        return ApiKey("JWT", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.any()).build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return listOf(SecurityReference("JWT", authorizationScopes))
    }

    private fun metaInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("API 문서")
            .description("테스트 API 문서입니다.")
            .version("1.0")
            .termsOfServiceUrl("http://terms.com")
            .license("Apache")
            .licenseUrl("http://license.com")
            .contact(
                Contact(
                    "yeonseok",
                    "https://github.com/yeonnseok",
                    "ljinsk3@gmail.com"
                )
            )
            .build()
    }
}
