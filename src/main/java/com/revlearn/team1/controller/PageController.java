package com.revlearn.team1.controller;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.page.PageReqDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.model.Page;
import com.revlearn.team1.service.page.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/page")
public class PageController {
    private final PageService pageService;

    @GetMapping("/{pageId}")
    public PageResDTO getPageById(@PathVariable Long pageId) {
        return pageService.getPageById(pageId);
    }

    @PostMapping("/module/{moduleId}")
    public PageResDTO createPage(@PathVariable Long moduleId, @RequestBody PageReqDTO pageReqDTO) {
        return pageService.createPage(moduleId, pageReqDTO);
    }

    @PutMapping("/{pageId}")
    public PageResDTO updatePage(@PathVariable Long pageId, @RequestBody PageReqDTO pageReqDTO) {
        return pageService.updatePage(pageId, pageReqDTO);
    }

    @DeleteMapping("/{pageId}")
    public MessageDTO deletePage(@PathVariable Long pageId) {
        return pageService.deletePage(pageId);
    }

}
