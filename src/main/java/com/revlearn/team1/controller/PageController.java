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
    public ModulePage getPageById(Long pageId) {
        return pageService.getPageById(pageId);
    }

    @PostMapping("/module/{moduleId}")
    public ModulePage createPage(Long moduleId, ModulePage page) {
        return pageService.createPage(moduleId, page);
    }

    @PutMapping("/{pageId}")
    public ModulePage updatePage(Long pageId, ModulePage page) {
        return pageService.updatePage(pageId, page);
    }

    @DeleteMapping("/{pageId}")
    public MessageDTO deletePage(Long pageId) {
        return pageService.deletePage(pageId);
    }

}
