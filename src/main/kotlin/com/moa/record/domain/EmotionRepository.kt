package com.moa.record.domain

import org.springframework.data.jpa.repository.JpaRepository

interface EmotionRepository : JpaRepository<Emotion, Long>
