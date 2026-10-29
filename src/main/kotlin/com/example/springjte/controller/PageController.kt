package com.example.springjte.controller

import com.example.springjte.dto.UserRequest
import com.example.springjte.dto.UserResponse
import com.example.springjte.service.UserService
import com.example.springjte.web.WebUtils
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class PageController(private val service: UserService) {

    @GetMapping("/")
    fun index(): String = "redirect:/users"

    @GetMapping("/users")
    fun list(
        @RequestParam(required = false) search: String?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        model: Model,
        request: HttpServletRequest
    ): String {
        addCommon(model, request)
        model.addAttribute("search", search?.trim() ?: "")
        model.addAttribute("pageData", service.findPage(search, page, size))
        return "users/list"
    }

    @GetMapping("/users/{id}/edit")
    fun edit(@PathVariable id: Long, model: Model, request: HttpServletRequest): String {
        addCommon(model, request)
        model.addAttribute("user", service.findById(id))
        return "users/edit"
    }

    @PostMapping("/users/{id}/update")
    fun update(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam email: String,
        @RequestParam password: String,
        model: Model,
        request: HttpServletRequest,
        redirectAttributes: RedirectAttributes
    ): String {
        val userRequest = UserRequest(
            name = name,
            email = email,
            password = password.takeIf { it.isNotBlank() }
        )
        return try {
            service.update(id, userRequest)
            redirectAttributes.addFlashAttribute("flash", "User updated successfully.")
            "redirect:/users"
        } catch (ex: ResponseStatusException) {
            if (ex.statusCode.value() == HttpStatus.CONFLICT.value()) {
                addCommon(model, request)
                model.addAttribute("user", UserResponse(id = id, name = name.trim(), email = email.trim(), role = service.findById(id).role))
                model.addAttribute("error", "That email is already in use by another user.")
                return "users/edit"
            }
            throw ex
        }
    }

    @PostMapping("/users/{id}/delete")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        service.delete(id)
        redirectAttributes.addFlashAttribute("flash", "User deleted.")
        return "redirect:/users"
    }

    private fun addCommon(model: Model, request: HttpServletRequest) {
        model.addAttribute("contextPath", request.contextPath)
        model.addAttribute("csrf", WebUtils.csrfToken(request)?.token)
        model.addAttribute("csrfParam", "_csrf")
        model.addAttribute("currentUser", request.userPrincipal?.name ?: "")
    }
}