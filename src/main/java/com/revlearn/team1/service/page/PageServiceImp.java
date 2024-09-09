package com.revlearn.team1.service.page;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.page.PageReqDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.PageNotFoundException;
import com.revlearn.team1.exceptions.UserNotAuthorizedException;
import com.revlearn.team1.mapper.PageMapper;
import com.revlearn.team1.model.Module;
import com.revlearn.team1.model.Page;
import com.revlearn.team1.repository.ModuleRepo;
import com.revlearn.team1.repository.PageRepo;
import com.revlearn.team1.service.securityContext.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageServiceImp implements PageService {
    private final PageRepo pageRepo;
    private final ModuleRepo moduleRepo;
    private final PageMapper pageMapper;

    @Override
    public PageResDTO getPageById(Long pageId) {
        //TODO: Verify requester is authorized to view page: enrolled student, assigned educator, or owner institution
        return pageRepo.findById(pageId).map(pageMapper::toPageResDTO)
                .orElseThrow(() -> new PageNotFoundException("Page with id: " + pageId + " not found"));
    }

    @Override
    public PageResDTO createPage(Long moduleId, PageReqDTO pageReqDTO) {

        //verify module exists
        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        //verify module is owned by requester or requester is an institution (admin) account
        Long requesterId = SecurityContextService.getUserId();
        Roles userRole = SecurityContextService.getUserRole();
        if (userRole != Roles.INSTITUTION && module.getCourse().getEducators().stream().anyMatch(e -> e.getId() == (requesterId))) {
            throw new UserNotAuthorizedException("User not authorized to create page for module.  Must be assigned educator, or institution (admin) account.");
        }

        Page page = pageMapper.toPage(pageReqDTO);
        page.setPageNumber((long) module.getPages().size());

        //Attach page to module
        module.getPages().add(page);
        page.setModule(module);


        //Save page
        return pageMapper.toPageResDTO(pageRepo.save(page));
    }

    @Override
    public PageResDTO updatePage(Long pageId, PageReqDTO pageReqDTO) {

        //verify page exists
        Page existingPage = pageRepo.findById(pageId)
                .orElseThrow(() -> new PageNotFoundException("Page with id: " + pageId + " not found"));

        //verify module exists
        Module module = existingPage.getModule();

        //verify module is owned by requester or requester is an institution (admin) account
        Long requesterId = SecurityContextService.getUserId();
        Roles userRole = SecurityContextService.getUserRole();
        if (userRole != Roles.INSTITUTION && module.getCourse().getEducators().stream().anyMatch(e -> e.getId() == (requesterId))) {
            throw new UserNotAuthorizedException("User not authorized to update page for module.  Must be assigned educator, or institution (admin) account.");
        }

        //update page
        existingPage.setTitle(pageReqDTO.title());
        existingPage.setMarkdownContent(pageReqDTO.markdownContent());
        //TODO: Handle page number (index) updates separately
//        existingPage.setPageNumber(pageReqDTO.pageNumber());
        existingPage.setInstructorNotes(pageReqDTO.instructorNotes());

        //Save page
        return pageMapper.toPageResDTO(pageRepo.save(existingPage));
    }

    @Override
    public MessageDTO deletePage(Long pageId) {
        //verify page exists
        Page existingPage = pageRepo.findById(pageId)
                .orElseThrow(() -> new PageNotFoundException("Page with id: " + pageId + " not found"));

        //verify module exists
        Module module = existingPage.getModule();

        //verify module is owned by requester or requester is an institution (admin) account
        Long requesterId = SecurityContextService.getUserId();
        Roles userRole = SecurityContextService.getUserRole();
        if (userRole != Roles.INSTITUTION && module.getCourse().getEducators().stream().anyMatch(e -> e.getId() == (requesterId))) {
            throw new UserNotAuthorizedException("User not authorized to delete page for module.  Must be assigned educator, or institution (admin) account.");
        }

        //delete page
        pageRepo.delete(existingPage);

        return new MessageDTO("Page with id: " + pageId + " deleted.");
    }
}
