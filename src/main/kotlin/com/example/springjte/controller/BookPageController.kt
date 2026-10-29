package com.example.springjte.controller

import com.example.springjte.dto.BookRequest
import com.example.springjte.service.BookService
import com.example.springjte.web.WebUtils
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Server-rendered book catalog — the Spring/JTE counterpart of the ASP.NET Core
 * `BookController`.
 *
 * Routes mirror the original actions:
 *
 *   Index    -> GET  /books
 *   Details  -> GET  /books/{id}
 *   Create   -> GET  /books/new      + POST /books
 *   Edit     -> GET  /books/{id}/edit + POST /books/{id}
 *   Delete   -> POST /books/{id}/delete
 *   Report   -> GET  /books/export  (CSV instead of the PDF/Excel reports)
 *
 * The literal segments (`new`, `export`) are matched ahead of `{id}` by Spring's
 * pattern comparator, so they never collide with the id route.
 */
@Controller
@RequestMapping("/books")
class BookPageController(private val service: BookService) {

    @GetMapping
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
        return "books/list"
    }

    @GetMapping("/new")
    fun createForm(model: Model, request: HttpServletRequest): String {
        addFormCommon(
            model = model,
            request = request,
            heading = "Add Book",
            actionUrl = request.contextPath + "/books"
        )
        model.addAttribute("titleValue", "")
        model.addAttribute("authorValue", "")
        model.addAttribute("priceValue", "")
        return "books/form"
    }

    @PostMapping
    fun create(
        @RequestParam title: String,
        @RequestParam author: String,
        @RequestParam price: String,
        model: Model,
        request: HttpServletRequest,
        redirectAttributes: RedirectAttributes
    ): String {
        val validationError = validate(title, author, price)
        if (validationError != null) {
            addFormCommon(
                model = model,
                request = request,
                heading = "Add Book",
                actionUrl = request.contextPath + "/books",
                error = validationError
            )
            model.addAttribute("titleValue", title)
            model.addAttribute("authorValue", author)
            model.addAttribute("priceValue", price)
            return "books/form"
        }

        val created = service.create(BookRequest(title, author, price.trim().toInt()))
        redirectAttributes.addFlashAttribute("flash", "Added \"${created.title}\".")
        return "redirect:/books"
    }

    @GetMapping("/{id}")
    fun details(
        @PathVariable id: Long,
        model: Model,
        request: HttpServletRequest
    ): String {
        addCommon(model, request)
        model.addAttribute("book", service.findById(id))
        return "books/details"
    }

    @GetMapping("/{id}/edit")
    fun editForm(
        @PathVariable id: Long,
        model: Model,
        request: HttpServletRequest
    ): String {
        val book = service.findById(id)
        addFormCommon(
            model = model,
            request = request,
            heading = "Edit Book",
            actionUrl = request.contextPath + "/books/$id"
        )
        model.addAttribute("titleValue", book.title)
        model.addAttribute("authorValue", book.author)
        model.addAttribute("priceValue", book.price.toString())
        return "books/form"
    }

    @PostMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestParam title: String,
        @RequestParam author: String,
        @RequestParam price: String,
        model: Model,
        request: HttpServletRequest,
        redirectAttributes: RedirectAttributes
    ): String {
        val validationError = validate(title, author, price)
        if (validationError != null) {
            addFormCommon(
                model = model,
                request = request,
                heading = "Edit Book",
                actionUrl = request.contextPath + "/books/$id",
                error = validationError
            )
            model.addAttribute("titleValue", title)
            model.addAttribute("authorValue", author)
            model.addAttribute("priceValue", price)
            return "books/form"
        }

        val updated = service.update(id, BookRequest(title, author, price.trim().toInt()))
        redirectAttributes.addFlashAttribute("flash", "Updated \"${updated.title}\".")
        return "redirect:/books"
    }

    @PostMapping("/{id}/delete")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        val book = service.findById(id)
        service.delete(id)
        redirectAttributes.addFlashAttribute("flash", "Deleted \"${book.title}\".")
        return "redirect:/books"
    }

    /**
     * Whole-catalog CSV download.
     *
     * A UTF-8 BOM is prepended so Excel on Windows detects the encoding instead
     * of mangling non-ASCII titles.
     */
    @GetMapping("/export")
    fun export(): ResponseEntity<ByteArray> {
        val bom = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())
        val body = bom + service.toCsv().toByteArray(Charsets.UTF_8)
        val filename = "books-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".csv"

        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment().filename(filename).build().toString()
            )
            .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
            .body(body)
    }

    /** Returns a user-facing message, or null when the input is acceptable. */
    private fun validate(title: String, author: String, price: String): String? {
        val parsed = price.trim().toIntOrNull()
        return when {
            title.isBlank() -> "Title is required."
            title.trim().length > 200 -> "Title must be at most 200 characters."
            author.isBlank() -> "Author is required."
            author.trim().length > 120 -> "Author must be at most 120 characters."
            parsed == null -> "Price must be a whole number."
            parsed < 0 -> "Price cannot be negative."
            else -> null
        }
    }

    private fun addCommon(model: Model, request: HttpServletRequest) {
        model.addAttribute("contextPath", request.contextPath)
        model.addAttribute("csrf", WebUtils.csrfToken(request)?.token)
        model.addAttribute("csrfParam", "_csrf")
        model.addAttribute("currentUser", request.userPrincipal?.name ?: "")
    }

    private fun addFormCommon(
        model: Model,
        request: HttpServletRequest,
        heading: String,
        actionUrl: String,
        error: String? = null
    ) {
        addCommon(model, request)
        model.addAttribute("heading", heading)
        model.addAttribute("actionUrl", actionUrl)
        model.addAttribute("error", error)
    }
}
