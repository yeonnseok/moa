package com.moa.acceptance

import com.google.common.base.CaseFormat
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.metamodel.EntityType
import javax.transaction.Transactional

@Service
@Profile("acceptance")
class DatabaseCleanup(
    private val entityManager: EntityManager
) : InitializingBean {

    private var tableNames: List<String>? = null

    override fun afterPropertiesSet() {
        tableNames = entityManager.metamodel.entities.stream()
            .filter { e: EntityType<*> ->
                e.javaType.getAnnotation(
                    Entity::class.java
                ) != null
            }
            .map({ e: EntityType<*> ->
                CaseFormat.UPPER_CAMEL.to(
                    CaseFormat.LOWER_UNDERSCORE,
                    e.name
                )
            })
            .collect(Collectors.toList<Any>()) as List<String>?
    }

    @Transactional
    fun execute() {
        entityManager.flush()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
        for (tableName in tableNames!!) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
            entityManager.createNativeQuery("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1").executeUpdate()
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
    }
}
