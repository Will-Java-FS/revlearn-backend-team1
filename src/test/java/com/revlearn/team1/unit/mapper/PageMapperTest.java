package com.revlearn.team1.unit.mapper;

import com.revlearn.team1.dto.page.PageReqDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.mapper.PageMapper;
import com.revlearn.team1.model.Module;
import com.revlearn.team1.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PageMapperTest {

    private PageMapper pageMapper;

    @BeforeEach
    public void setUp() {
        pageMapper = new PageMapper();
    }

    @Test
    public void testToPageResDTO() {
        // Given: A Page entity
        Module module = new Module();
        module.setId(1L);

        Page page = new Page();
        page.setId(1L);
        page.setTitle("Test Page");
        page.setMarkdownContent("Test Content");
        page.setPageNumber(1L);
        page.setInstructorNotes("Test Notes");
        page.setAttachmentsUrls(List.of("http://example.com/attachment1"));
        page.setModule(module);
        page.setCreatedAt(LocalDateTime.of(2023, 9, 10, 12, 0));
        page.setUpdatedAt(LocalDateTime.of(2023, 9, 11, 12, 0));

        // When: Converting the Page entity to PageResDTO
        PageResDTO pageResDTO = pageMapper.toPageResDTO(page);

        // Then: Assert the DTO fields match the entity fields
        assertEquals(page.getId(), pageResDTO.id());
        assertEquals(page.getTitle(), pageResDTO.title());
        assertEquals(page.getMarkdownContent(), pageResDTO.markdownContent());
        assertEquals(page.getPageNumber(), pageResDTO.pageNumber());
        assertEquals(page.getInstructorNotes(), pageResDTO.instructorNotes());
        assertEquals(page.getAttachmentsUrls(), pageResDTO.attachmentsUrls());
        assertEquals(page.getModule().getId(), pageResDTO.moduleId());
        assertEquals(page.getCreatedAt(), pageResDTO.createdAt());
        assertEquals(page.getUpdatedAt(), pageResDTO.updatedAt());
    }

    @Test
    public void testToPage() {
        // Given: A PageReqDTO object
        PageReqDTO pageReqDTO = new PageReqDTO(
                "New Page",
                "New Content",
                "New Instructor Notes",
                List.of("http://example.com/attachment2")
        );

        // When: Converting the PageReqDTO to a Page entity
        Page page = pageMapper.toPage(pageReqDTO);

        // Then: Assert the entity fields match the DTO fields
        assertNull(page.getId()); // Id is not set by the mapper
        assertEquals(pageReqDTO.title(), page.getTitle());
        assertEquals(pageReqDTO.markdownContent(), page.getMarkdownContent());
        assertEquals(pageReqDTO.instructorNotes(), page.getInstructorNotes());
        assertEquals(pageReqDTO.attachmentsUrls(), page.getAttachmentsUrls());
        assertNull(page.getModule()); // Module is not set by the mapper
    }

}
