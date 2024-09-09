package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.page.PageReqDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.model.Page;
import org.springframework.stereotype.Component;


@Component
public class PageMapper {
    public PageResDTO toPageResDTO(Page page) {
        return new PageResDTO(
                page.getId(),
                page.getTitle(),
                page.getMarkdownContent(),
                page.getPageNumber(),
                page.getInstructorNotes(),
                page.getAttachmentsUrls(),
                page.getModule().getId(),
                page.getCreatedAt(),
                page.getUpdatedAt()
        );
    }

    //TODO: Handle page number (index) in the future
    public Page toPage(PageReqDTO pageReqDTO) {
        Page page = new Page();
        page.setTitle(pageReqDTO.title());
        page.setMarkdownContent(pageReqDTO.markdownContent());
        page.setInstructorNotes(pageReqDTO.instructorNotes());
        page.setAttachmentsUrls(pageReqDTO.attachmentsUrls());
        return page;
    }
}
