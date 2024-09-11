package com.revlearn.team1.controller;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.page.PageReqDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.service.page.PageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/page")
public class PageController {
    private final PageService pageService;

    @GetMapping("/{pageId}")
    @Operation(summary = "Get Page by Id", description = "", tags = {"page"})
    public PageResDTO getPageById(@PathVariable Long pageId) {
        return pageService.getPageById(pageId);
    }

    @PostMapping("/module/{moduleId}")
    @Operation(summary = "Create Page", description = "", tags = {"page"})
    public PageResDTO createPage(@PathVariable Long moduleId, @RequestBody PageReqDTO pageReqDTO) {
        return pageService.createPage(moduleId, pageReqDTO);
    }

    @PutMapping("/{pageId}")
    @Operation(summary = "Update Page", description = "", tags = {"page"})
    public PageResDTO updatePage(@PathVariable Long pageId, @RequestBody PageReqDTO pageReqDTO) {
        return pageService.updatePage(pageId, pageReqDTO);
    }

    @DeleteMapping("/{pageId}")
    @Operation(summary = "Delete Page", description = "", tags = {"page"})
    public MessageDTO deletePage(@PathVariable Long pageId) {
        return pageService.deletePage(pageId);
    }

}
