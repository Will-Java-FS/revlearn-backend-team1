package com.revlearn.team1.controller;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.model.ModulePage;
import com.revlearn.team1.service.page.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/page")
public class PageController {
    private final PageService pageService;

    @GetMapping("/{pageId}")
    public ModulePage getPageById(@PathVariable Long pageId) {
        return pageService.getPageById(pageId);
    }

    @PostMapping("/module/{moduleId}")
    public ModulePage createPage(@PathVariable Long moduleId, @RequestBody ModulePage page) {
        return pageService.createPage(moduleId, page);
    }

    @PutMapping("/{pageId}")
    public ModulePage updatePage(@PathVariable Long pageId, @RequestBody ModulePage page) {
        return pageService.updatePage(pageId, page);
    }

    @DeleteMapping("/{pageId}")
    public MessageDTO deletePage(@PathVariable Long pageId) {
        return pageService.deletePage(pageId);
    }

}
