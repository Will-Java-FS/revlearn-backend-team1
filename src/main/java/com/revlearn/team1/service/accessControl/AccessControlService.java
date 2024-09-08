package com.revlearn.team1.service.accessControl;

import com.revlearn.team1.model.Course;

public interface AccessControlService {
    boolean verifyStudentLevelAccess(Course course);
    boolean verifyEducatorLevelAccess(Course course);

    }
