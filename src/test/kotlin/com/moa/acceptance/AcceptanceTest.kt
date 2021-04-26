package com.moa.acceptance

import com.fasterxml.jackson.databind.ObjectMapper
import com.moa.common.ApiResponse
import com.moa.common.JwtTokenProvider
import com.moa.common.ResultType
import com.moa.auth.controller.request.SignupRequest
import com.moa.auth.controller.response.SignupResponse
import io.kotlintest.shouldBe
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
abstract class AcceptanceTest {

    @LocalServerPort
    protected var port: Int? = null

    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private val log = LoggerFactory.getLogger(javaClass)

    private var userId: Long? = null

    private var bearerToken: String = ""

    fun given(): RequestSpecification {
        return RestAssured.given().log().all()
    }

    @BeforeEach
    fun setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port!!
            databaseCleanup.afterPropertiesSet()
        }
        databaseCleanup.execute()

        userId = createUser()
        bearerToken = jwtTokenProvider.createToken(userId!!)
    }

    private fun createUser(): Long {
        val request = SignupRequest("moa", "moa@com", "m123", "m123")

        val response = post("/api/auth/signup", request)

        response.result shouldBe ResultType.SUCCESS
        response.statusCode shouldBe HttpStatus.CREATED.value()

        val jsonData = objectMapper.writeValueAsString(response.data)
        val signupResponse = objectMapper.readValue(jsonData, SignupResponse::class.java)
        return signupResponse.userId
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
}
