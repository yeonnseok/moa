package com.moa.record.controller.response

import com.moa.record.domain.Emotion
import com.moa.record.domain.EmotionType

data class EmotionResponse(
    val emotionType: EmotionType,
    val count: Int
) {
    companion object {
        fun of(emotion: Emotion): EmotionResponse {
            return EmotionResponse(
                emotionType = emotion.emotionType,
                count = emotion.count
            )
        }
    }
}
