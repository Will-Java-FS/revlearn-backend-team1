package com.revlearn.team1.service.page;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.PageNotFoundException;
import com.revlearn.team1.exceptions.UserNotAuthorizedException;
import com.revlearn.team1.model.CourseModule;
import com.revlearn.team1.model.ModulePage;
import com.revlearn.team1.repository.ModuleRepo;
import com.revlearn.team1.repository.PageRepo;
import com.revlearn.team1.service.securityContext.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageServiceImp implements PageService{
    private final PageRepo pageRepo;
    private final ModuleRepo moduleRepo;
    @Override
    public ModulePage getPageById(Long pageId) {
        //TODO: Verify requester is authorized to view page: enrolled student, assigned educator, or owner institution
        return pageRepo.findById(pageId)
                .orElseThrow(()->new PageNotFoundException("Page with id: "+pageId+" not found"));
    }

    @Override
    public ModulePage createPage(Long moduleId, ModulePage page) {

        //verify module exists
        CourseModule module = moduleRepo.findById(moduleId)
                .orElseThrow(()->new ModuleNotFoundException(moduleId));

        //verify module is owned by requester or requester is an institution (admin) account
        Long requesterId = SecurityContextService.getUserId();
        Roles userRole = SecurityContextService.getUserRole();
        if(userRole!=Roles.INSTITUTION && module.getCourse().getEducators().stream().anyMatch(e->e.getId()==(requesterId))){
            throw new UserNotAuthorizedException("User not authorized to create page for module.  Must be assigned educator, or institution (admin) account.");
        }

        //Attach page to module
        module.getModulePages().add(page);
        page.setCourseModule(module);


        //Save page
        moduleRepo.save(module);
        ModulePage savedPage = pageRepo.save(page);

        //TODO: Use DTO to return page
        return savedPage;
    }

    @Override
    public ModulePage updatePage(Long pageId, ModulePage page) {

        //verify page exists
        ModulePage existingPage = pageRepo.findById(pageId)
                .orElseThrow(()->new PageNotFoundException("Page with id: "+pageId+" not found"));

        //verify module exists
        CourseModule module = existingPage.getCourseModule();

        //verify module is owned by requester or requester is an institution (admin) account
        Long requesterId = SecurityContextService.getUserId();
        Roles userRole = SecurityContextService.getUserRole();
        if(userRole!=Roles.INSTITUTION && module.getCourse().getEducators().stream().anyMatch(e->e.getId()==(requesterId))){
            throw new UserNotAuthorizedException("User not authorized to update page for module.  Must be assigned educator, or institution (admin) account.");
        }

        //update page
        existingPage.setTitle(page.getTitle());
        existingPage.setMarkdownContent(page.getMarkdownContent());
        existingPage.setPageNumber(page.getPageNumber());
        existingPage.setInstructorNotes(page.getInstructorNotes());

        //Save page
        return pageRepo.save(existingPage);
    }

    @Override
    public MessageDTO deletePage(Long pageId) {
        //verify page exists
        ModulePage existingPage = pageRepo.findById(pageId)
                .orElseThrow(()->new PageNotFoundException("Page with id: "+pageId+" not found"));

        //verify module exists
        CourseModule module = existingPage.getCourseModule();

        //verify module is owned by requester or requester is an institution (admin) account
        Long requesterId = SecurityContextService.getUserId();
        Roles userRole = SecurityContextService.getUserRole();
        if(userRole!=Roles.INSTITUTION && module.getCourse().getEducators().stream().anyMatch(e->e.getId()==(requesterId))){
            throw new UserNotAuthorizedException("User not authorized to delete page for module.  Must be assigned educator, or institution (admin) account.");
        }

        //delete page
        pageRepo.delete(existingPage);

        return new MessageDTO("Page with id: "+pageId+" deleted.");
    }
}
