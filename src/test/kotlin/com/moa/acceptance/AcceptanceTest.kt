package com.moa.acceptance

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
abstract class AcceptanceTest {

    @LocalServerPort
    protected var port: Int? = null

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

    private val log = LoggerFactory.getLogger(javaClass)

    @BeforeEach
    fun init() {
        databaseCleanup.afterPropertiesSet()
        databaseCleanup.execute()

        RestAssured.port = port!!
    }

    fun given(): RequestSpecification {
        return RestAssured.given().log().all()
    }
}
