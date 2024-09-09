package com.revlearn.team1.service.programCourse;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.exceptions.ProgramNotFoundException;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.Program;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.ProgramRepo;
import com.revlearn.team1.service.accessControl.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramCourseServiceImp implements ProgramCourseService {
    private final CourseMapper courseMapper;
    private final ProgramRepo programRepo;
    private final CourseRepo courseRepo;
    private final AccessControlService accessControlService;

    @Override
    public List<CourseResDTO> getProgramCourses(Program program) {
        return program.getCourses().stream().map(courseMapper::toDto).toList();
    }

    @Override
    public void removeAllCoursesFromProgram(Program program) {
        List<Course> courses = new ArrayList<>(program.getCourses());

        if (!courses.isEmpty()) {
            // Remove the program from each course in bulk
            for (Course course : courses) {
                course.getPrograms().remove(program);
            }
            // Save all modified courses in a batch
            courseRepo.saveAll(courses);

            // Clear courses from the program and save the program
            program.getCourses().clear();
            programRepo.save(program);
        }
    }

    @Override
    public void removeAllProgramsFromCourse(Course course) {
        List<Program> programs = new ArrayList<>(course.getPrograms());

        if (!programs.isEmpty()) {
            // Remove the course from each program in bulk
            for (Program program : programs) {
                program.getCourses().remove(course);
            }
            // Save all modified programs in a batch
            programRepo.saveAll(programs);

            // Clear programs from the course and save the course
            course.getPrograms().clear();
            courseRepo.save(course);
        }
    }


    @Override
    public MessageDTO addCourseToProgram(Long programId, Long courseId) {

        //verify request authorization
        accessControlService.verifyInstitutionAccess();

        //verify program exists
        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        //verify course is not already in program
        if (program.getCourses().stream().anyMatch(c -> Objects.equals(c.getId(), courseId))) {
            return new MessageDTO("Course with id: " + courseId + " is already in program with id: " + programId);
        }

        //verify course exists
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new CourseNotFoundException(
                "ProgramCourseService#addCourseToProgram", courseId));

        //add course to program
        program.getCourses().add(course);
        course.getPrograms().add(program);

        //save program
        programRepo.save(program);
        courseRepo.save(course);

        return new MessageDTO("Successfully added course with id: " + courseId + " to program with id: " + programId);
    }

    @Override
    public MessageDTO removeCourseFromProgram(Long programId, Long courseId) {

        //verify request authorization
        accessControlService.verifyInstitutionAccess();

        //verify program exists
        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));


        //verify course exists in program
        Optional<Course> course = program.getCourses().stream().filter(c -> Objects.equals(c.getId(), courseId)).findFirst();
        if (course.isEmpty()) {
            return new MessageDTO("Course with id: " + courseId + " is not in program with id: " + programId);
        }
        Course foundCourse = course.get();

        //remove course from program
        program.getCourses().remove(foundCourse);
        foundCourse.getPrograms().remove(program);

        //save program
        programRepo.save(program);
        courseRepo.save(foundCourse);

        return new MessageDTO("Successfully removed course with id: " + courseId + " from program with id: " + programId);
    }
}
