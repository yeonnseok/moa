package com.moa.acceptance

import com.fasterxml.jackson.databind.ObjectMapper
import com.moa.TestDataLoader
import com.moa.auth.controller.request.LoginRequest
import com.moa.auth.controller.request.SignupRequest
import com.moa.auth.controller.response.TokenResponse
import com.moa.common.ApiResponse
import com.moa.common.ResultType
import com.moa.record.domain.Description
import com.moa.record.domain.DescriptionRepository
import com.moa.record.domain.EmotionType
import com.moa.user.domain.RoleType
import com.moa.user.domain.User
import com.moa.user.domain.UserRepository
import io.kotlintest.shouldBe
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
@Sql("/truncate.sql")
abstract class AcceptanceTest {

    @LocalServerPort
    protected var port: Int? = null

    @Autowired
    private lateinit var descriptionRepository: DescriptionRepository

   @Autowired
    protected lateinit var dataLoader: TestDataLoader

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    protected var userId: Long? = null

    private var bearerToken: String = ""

    fun given(): RequestSpecification {
        return RestAssured.given().log().all()
    }

    @BeforeEach
    fun setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port!!
        }

        initDescriptions()
        bearerToken = createUser()
    }

    private fun initDescriptions() {
        dataLoader.sample_description_36_to_40()
    }

    private fun createUser(): String {
        val user = dataLoader.sample_user()
        userId = user.id

        val request = LoginRequest("moa@com", "m123")

        val response = login("/api/v1/auth/login", request)

        response.result shouldBe ResultType.SUCCESS
        response.statusCode shouldBe HttpStatus.OK.value()

        val tokenResponse = getResponseData(response.data, TokenResponse::class.java) as TokenResponse
        return tokenResponse.token
    }

    protected fun getResponseData(data: Any, classType: Class<*>): Any {
        val jsonData = objectMapper.writeValueAsString(data)
        return objectMapper.readValue(jsonData, classType)
    }

    protected fun post(path: String, request: Any): ApiResponse {
        return given().
                auth().oauth2(bearerToken).
                body(request).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        `when`().
                post(path).
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                extract().`as`(ApiResponse::class.java)
    }

    protected fun login(path: String, request: Any): ApiResponse {
        return given().
                body(request).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        `when`().
                post(path).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                extract().`as`(ApiResponse::class.java)
    }

    protected fun get(path: String): ApiResponse {
        return given().
                auth().oauth2(bearerToken).
        `when`().
                get(path).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().`as`(ApiResponse::class.java)
    }

    protected fun patch(path: String, request: Any): ApiResponse {
        return given().
                auth().oauth2(bearerToken).
                body(request).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        `when`().
                patch(path).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().`as`(ApiResponse::class.java)
    }

    protected fun delete(path: String) {
        given().
                auth().oauth2(bearerToken).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        `when`().
                delete(path).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value())
    }
}
