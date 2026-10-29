package com.example.springjte.controller

import com.example.springjte.web.WebUtils
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {

    @GetMapping("/login")
    fun login(model: Model, request: HttpServletRequest): String {
        model.addAttribute("contextPath", request.contextPath)
        model.addAttribute("csrf", WebUtils.csrfToken(request)?.token)
        model.addAttribute("csrfParam", "_csrf")
        model.addAttribute("hasError", request.getParameter("error") != null)
        model.addAttribute("hasLogout", request.getParameter("logout") != null)
        return "login"
    }
}