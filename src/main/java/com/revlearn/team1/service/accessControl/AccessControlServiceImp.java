package com.revlearn.team1.service.accessControl;

import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.exceptions.UserNotAuthorizedException;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.service.securityContext.SecurityContextService;
import org.springframework.stereotype.Service;

@Service
public class AccessControlServiceImp implements AccessControlService {
    public boolean verifyStudentLevelAccess(Course course) {

        Long userId = SecurityContextService.getUserId();
        Roles userRole = SecurityContextService.getUserRole();

        if (course.getStudents().stream().noneMatch(s -> s.getId() == userId)
                && course.getEducators().stream().noneMatch(e -> e.getId() == userId)
                && !userRole.equals(Roles.INSTITUTION))
            throw new UserNotAuthorizedException(
                    "User is not authorized to access this course.  Must be enrolled student, assigned educator, or administrator.");

        return true;
    }

    public boolean verifyEducatorLevelAccess(Course course) {

        Long userId = SecurityContextService.getUserId();
        Roles userRole = SecurityContextService.getUserRole();

        if (course.getEducators().stream().noneMatch(e -> e.getId() == userId)
                && !userRole.equals(Roles.INSTITUTION))
            throw new UserNotAuthorizedException(
                    "User is not authorized to access this course.  Must be assigned educator or administrator.");

        return true;
    }

    public void verifyInstitutionAccess() {
        if (SecurityContextService.getUserRole() != Roles.INSTITUTION) {
            throw new UserNotAuthorizedException("Unauthorized to access resource.  Must be institution (admin) account.");
        }
    }
}
