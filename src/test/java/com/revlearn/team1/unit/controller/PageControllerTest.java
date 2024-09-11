package com.revlearn.team1.unit.controller;

import com.revlearn.team1.controller.PageController;
import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.page.PageReqDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.service.page.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PageService pageService;

    @InjectMocks
    private PageController pageController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pageController).build();
    }

    @Test
    public void testGetPageById() throws Exception {
        Long pageId = 1L;

        PageResDTO pageResDTO = new PageResDTO(
                pageId,
                "Test Page",
                "Test Content",
                1L,
                "Test Notes",
                List.of("http://example.com/attachment1"),
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(pageService.getPageById(pageId)).thenReturn(pageResDTO);

        mockMvc.perform(get("/api/v1/page/{pageId}", pageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pageId))
                .andExpect(jsonPath("$.title").value("Test Page"))
                .andExpect(jsonPath("$.markdownContent").value("Test Content"));

        verify(pageService, times(1)).getPageById(pageId);
    }

    @Test
    public void testCreatePage() throws Exception {
        Long moduleId = 1L;

        PageReqDTO pageReqDTO = new PageReqDTO(
                "New Page",
                "New Content",
                "New Instructor Notes",
                List.of("http://example.com/attachment2")
        );

        PageResDTO pageResDTO = new PageResDTO(
                1L,
                "New Page",
                "New Content",
                1L,
                "New Instructor Notes",
                List.of("http://example.com/attachment2"),
                moduleId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(pageService.createPage(eq(moduleId), any(PageReqDTO.class))).thenReturn(pageResDTO);

        mockMvc.perform(post("/api/v1/page/module/{moduleId}", moduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Page\",\"markdownContent\":\"New Content\",\"instructorNotes\":\"New Instructor Notes\",\"attachmentsUrls\":[\"http://example.com/attachment2\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Page"));

        verify(pageService, times(1)).createPage(eq(moduleId), any(PageReqDTO.class));
    }

    @Test
    public void testUpdatePage() throws Exception {
        Long pageId = 1L;

        PageReqDTO pageReqDTO = new PageReqDTO(
                "Updated Page",
                "Updated Content",
                "Updated Instructor Notes",
                List.of("http://example.com/attachment3")
        );

        PageResDTO pageResDTO = new PageResDTO(
                pageId,
                "Updated Page",
                "Updated Content",
                1L,
                "Updated Instructor Notes",
                List.of("http://example.com/attachment3"),
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(pageService.updatePage(eq(pageId), any(PageReqDTO.class))).thenReturn(pageResDTO);

        mockMvc.perform(put("/api/v1/page/{pageId}", pageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Page\",\"markdownContent\":\"Updated Content\",\"instructorNotes\":\"Updated Instructor Notes\",\"attachmentsUrls\":[\"http://example.com/attachment3\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pageId))
                .andExpect(jsonPath("$.title").value("Updated Page"));

        verify(pageService, times(1)).updatePage(eq(pageId), any(PageReqDTO.class));
    }

    @Test
    public void testDeletePage() throws Exception {
        Long pageId = 1L;
        MessageDTO messageDTO = new MessageDTO("Page deleted successfully");

        when(pageService.deletePage(pageId)).thenReturn(messageDTO);

        mockMvc.perform(delete("/api/v1/page/{pageId}", pageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Page deleted successfully"));

        verify(pageService, times(1)).deletePage(pageId);
    }
}
