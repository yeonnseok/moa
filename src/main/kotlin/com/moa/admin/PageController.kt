package com.moa.admin

import com.moa.recommendation.domain.ContentFinder
import com.moa.record.domain.DescriptionFinder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class PageController(
    private val descriptionFinder: DescriptionFinder,
    private val contentFinder: ContentFinder
) {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/admin")
    fun admin(): String {
        return "admin"
    }

    @GetMapping("/admin/descriptions")
    fun descriptionList(model: Model): String {
        val descriptions = descriptionFinder.findAll()
        model.addAttribute("descriptions", descriptions)
        return "description-list"
    }

    @GetMapping("/admin/descriptions/{id}")
    fun description(
        @PathVariable id: Long,
        model: Model
    ): String {
        val description = descriptionFinder.findById(id)
        model.addAttribute("description", description)
        return "description"
    }

    @GetMapping("/admin/descriptions/create")
    fun descriptionForm(): String {
        return "description-form"
    }
}
