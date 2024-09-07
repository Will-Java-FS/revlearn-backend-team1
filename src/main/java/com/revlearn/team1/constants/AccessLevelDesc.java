package com.revlearn.team1.constants;

import lombok.Getter;

@Getter
public class AccessLevelDesc {
    public static final String AFFECTED_STUDENT = "Authorization Required: affected student, assigned educator or institution (admin) account.  ";
    public static final String ENROLLED_STUDENT = "Authorization required: enrolled student, assigned educator or institution (admin) account.  ";
    public static final String ASSIGNED_EDUCATOR = "Authorization required: assigned educator or institution (admin) account.  ";
    public static final String ANY_EDUCATOR = "Authorization required: educator or institution (admin) account.  ";
    public static final String INSTITUTION = "Authorization required: institution (admin) account.  ";
}
