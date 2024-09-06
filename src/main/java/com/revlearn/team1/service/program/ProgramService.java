package com.revlearn.team1.service.program;

import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.dto.user.UserDTO;
import com.revlearn.team1.dto.user.UserResDTO;

import java.util.List;

public interface ProgramService {
    List<ProgramResDTO> getAllPrograms();

    ProgramResDTO getProgramById(Long programId);

    List<CourseResDTO> getProgramCourses(Long programId);

    List<UserResDTO> getProgramStudents(Long programId);

    ProgramResDTO createProgram(ProgramReqDTO programReqDTO);

    ProgramResDTO updateProgram(Long programId, ProgramReqDTO programReqDTO);

    void deleteProgram(Long programId);
}
