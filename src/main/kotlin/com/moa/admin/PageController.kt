package com.moa.admin

import com.moa.recommendation.domain.ContentFinder
import com.moa.record.domain.DescriptionFinder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class PageController(
    private val descriptionFinder: DescriptionFinder,
    private val contentFinder: ContentFinder
) {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping
    fun admin(): String {
        return "admin"
    }

    @GetMapping("/descriptions")
    fun descriptionList(model: Model): String {
        val descriptions = descriptionFinder.findAll()
        model.addAttribute("descriptions", descriptions)
        return "description-list"
    }

    @GetMapping("/descriptions/{id}")
    fun description(
        @PathVariable id: Long,
        model: Model
    ): String {
        val description = descriptionFinder.findById(id)
        model.addAttribute("description", description)
        return "description"
    }

    @GetMapping("/descriptions/create")
    fun descriptionForm(): String {
        return "description-form"
    }

    @GetMapping("/contents")
    fun contentList(model: Model): String {
        val contents = contentFinder.findAll()
        model.addAttribute("contents", contents)
        return "content-list"
    }

    @GetMapping("/contents/create")
    fun contentForm(): String {
        return "content-form"
    }

    @GetMapping("/contents/{id}")
    fun content(
        @PathVariable id: Long,
        model: Model
    ): String {
        val content = contentFinder.findById(id)
        model.addAttribute("content", content)
        return "content"
    }
}
