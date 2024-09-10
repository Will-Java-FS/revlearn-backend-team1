package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.page.PageReqDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.PageNotFoundException;
import com.revlearn.team1.mapper.PageMapper;
import com.revlearn.team1.model.Module;
import com.revlearn.team1.model.Page;
import com.revlearn.team1.repository.ModuleRepo;
import com.revlearn.team1.repository.PageRepo;
import com.revlearn.team1.service.accessControl.AccessControlService;
import com.revlearn.team1.service.page.PageServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PageServiceImpTest {

    @Mock
    private PageRepo pageRepo;

    @Mock
    private ModuleRepo moduleRepo;

    @Mock
    private PageMapper pageMapper;

    @Mock
    private AccessControlService accessControlService;

    @InjectMocks
    private PageServiceImp pageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPageById_Success() {
        Long pageId = 1L;

        // Create mock Page and PageResDTO objects
        Page page = new Page();
        page.setId(pageId);
        PageResDTO pageResDTO = new PageResDTO(
                pageId, "Test Page", "Test Content", 1L, "Test Notes", new ArrayList<>(), 1L, null, null
        );

        // Mock the repository and mapper behavior
        when(pageRepo.findById(pageId)).thenReturn(Optional.of(page));
        when(pageMapper.toPageResDTO(page)).thenReturn(pageResDTO);

        // Test the service method
        PageResDTO result = pageService.getPageById(pageId);

        // Verify behavior
        assertEquals(pageResDTO, result);
        verify(pageRepo, times(1)).findById(pageId);
        verify(pageMapper, times(1)).toPageResDTO(page);
    }

    @Test
    void testGetPageById_PageNotFound() {
        Long pageId = 1L;

        // Mock the repository to return an empty Optional
        when(pageRepo.findById(pageId)).thenReturn(Optional.empty());

        // Test and assert exception is thrown
        assertThrows(PageNotFoundException.class, () -> pageService.getPageById(pageId));
        verify(pageRepo, times(1)).findById(pageId);
        verify(pageMapper, never()).toPageResDTO(any());
    }

    @Test
    void testCreatePage_Success() {
        Long moduleId = 1L;

        // Create mock Module, PageReqDTO, and Page objects
        Module module = new Module();
        module.setId(moduleId);
        module.setPages(new ArrayList<>());

        PageReqDTO pageReqDTO = new PageReqDTO("New Page", "New Content", "New Notes", new ArrayList<>());
        Page page = new Page();
        PageResDTO pageResDTO = new PageResDTO(1L, "New Page", "New Content", 1L, "New Notes", new ArrayList<>(), moduleId, null, null);

        // Mock repository, access control, and mapper behavior
        when(moduleRepo.findById(moduleId)).thenReturn(Optional.of(module));
        when(pageMapper.toPage(any(PageReqDTO.class))).thenReturn(page);
        when(pageRepo.save(page)).thenReturn(page);
        when(pageMapper.toPageResDTO(page)).thenReturn(pageResDTO);

        // Test the service method
        PageResDTO result = pageService.createPage(moduleId, pageReqDTO);

        // Verify behavior
        assertEquals(pageResDTO, result);
        verify(moduleRepo, times(1)).findById(moduleId);
        verify(accessControlService, times(1)).verifyEducatorLevelAccess(module.getCourse());
        verify(pageMapper, times(1)).toPage(pageReqDTO);
        verify(pageRepo, times(1)).save(page);
    }

    @Test
    void testCreatePage_ModuleNotFound() {
        Long moduleId = 1L;
        PageReqDTO pageReqDTO = new PageReqDTO("New Page", "New Content", "New Notes", new ArrayList<>());

        // Mock repository to return an empty Optional for the module
        when(moduleRepo.findById(moduleId)).thenReturn(Optional.empty());

        // Test and assert exception is thrown
        assertThrows(ModuleNotFoundException.class, () -> pageService.createPage(moduleId, pageReqDTO));
        verify(moduleRepo, times(1)).findById(moduleId);
        verify(pageRepo, never()).save(any(Page.class));
    }

    @Test
    void testUpdatePage_Success() {
        Long pageId = 1L;

        // Create mock Page, Module, and PageReqDTO objects
        Module module = new Module();
        Page page = new Page();
        page.setId(pageId);
        page.setModule(module);

        PageReqDTO pageReqDTO = new PageReqDTO("Updated Page", "Updated Content", "Updated Notes", new ArrayList<>());
        PageResDTO pageResDTO = new PageResDTO(pageId, "Updated Page", "Updated Content", 1L, "Updated Notes", new ArrayList<>(), 1L, null, null);

        // Mock repository, access control, and mapper behavior
        when(pageRepo.findById(pageId)).thenReturn(Optional.of(page));
        when(pageRepo.save(page)).thenReturn(page);
        when(pageMapper.toPageResDTO(page)).thenReturn(pageResDTO);

        // Test the service method
        PageResDTO result = pageService.updatePage(pageId, pageReqDTO);

        // Verify behavior
        assertEquals(pageResDTO, result);
        verify(pageRepo, times(1)).findById(pageId);
        verify(accessControlService, times(1)).verifyEducatorLevelAccess(module.getCourse());
        verify(pageRepo, times(1)).save(page);
    }

    @Test
    void testUpdatePage_PageNotFound() {
        Long pageId = 1L;
        PageReqDTO pageReqDTO = new PageReqDTO("Updated Page", "Updated Content", "Updated Notes", new ArrayList<>());

        // Mock repository to return an empty Optional for the page
        when(pageRepo.findById(pageId)).thenReturn(Optional.empty());

        // Test and assert exception is thrown
        assertThrows(PageNotFoundException.class, () -> pageService.updatePage(pageId, pageReqDTO));
        verify(pageRepo, times(1)).findById(pageId);
        verify(pageRepo, never()).save(any(Page.class));
    }

    @Test
    void testDeletePage_Success() {
        Long pageId = 1L;

        // Create mock Page and Module objects
        Page page = new Page();
        page.setId(pageId);
        Module module = new Module();
        page.setModule(module);

        // Mock repository and access control behavior
        when(pageRepo.findById(pageId)).thenReturn(Optional.of(page));

        // Test the service method
        MessageDTO result = pageService.deletePage(pageId);

        // Verify behavior
        assertEquals("Page with id: " + pageId + " deleted.", result.message());
        verify(pageRepo, times(1)).findById(pageId);
        verify(accessControlService, times(1)).verifyEducatorLevelAccess(module.getCourse());
        verify(pageRepo, times(1)).delete(page);
    }

    @Test
    void testDeletePage_PageNotFound() {
        Long pageId = 1L;

        // Mock repository to return an empty Optional for the page
        when(pageRepo.findById(pageId)).thenReturn(Optional.empty());

        // Test and assert exception is thrown
        assertThrows(PageNotFoundException.class, () -> pageService.deletePage(pageId));
        verify(pageRepo, times(1)).findById(pageId);
        verify(pageRepo, never()).delete(any(Page.class));
    }
}
