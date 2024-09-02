package com.revlearn.team1.service;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.exceptions.CourseNotFoundException;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImp implements CourseService {
    private final CourseRepo courseRepo;
    private final CourseMapper courseMapper;
    private final UserRepository userRepo;

    @Override
    public List<CourseDTO> getAll() {
        //TODO: Consider pagination instead of the return of every course
        //TODO: Add sort methods: alphabetical, by program
        try {
            return courseRepo.findAll().stream().map(courseMapper::toDto).toList();
        } catch (DataAccessException ex) {
            throw new ServiceLayerDataAccessException("CourseService.getAll() failed to retrieve all courses because of a data access exception.", ex);
        }
    }

    @Override
    public CourseDTO getById(Long courseId) {
        Course retrievedCourse = courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("CourseService.getById()", courseId));
        return courseMapper.toDto(retrievedCourse);
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        //TODO: Secure so that only instructors and institutions can create courses
        // Maybe we require user information as parameter
        Course course = courseMapper.fromDto(courseDTO);
        Course savedCourse = courseRepo.save(course);
        return courseMapper.toDto(savedCourse);
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        //TODO: Secure so that only course owners (educator(s) or institution) can modify.
        // Compare provided course information (educator(s), institution) to authenticated user properties.

        //Verify course already exists in database
        Course course = courseRepo.findById(courseDTO.id()).orElseThrow(
                () -> new CourseNotFoundException("CourseService.updateCourse()", courseDTO.id()));


        //Update retrieved course object with courseDTO information
        courseMapper.updateCourseFromDto(course, courseDTO);

        //Save updated course to database
        Course savedCourse = courseRepo.save(course);

        //Return evidence of successful persistence as dto
        return courseMapper.toDto(savedCourse);
    }

    @Override
    public String deleteById(Long id) {
        //TODO: Secure method. verify requesters' are course owner: educator or institution

        //verify course exists
        courseRepo.findById(id).orElseThrow(
                () -> new CourseNotFoundException("CourseServiceImp.deleteById()", id));
        //delete
        courseRepo.deleteById(id);
        return String.format("Successfully deleted course %d.", id);
    }


    @Override
    public CourseEducatorResDTO addEducator(CourseEducatorDTO courseEducatorDTO) {
        //verify course and educator exist
        Course course = courseRepo.findById(courseEducatorDTO.courseId()).orElseThrow(
                () -> new CourseNotFoundException("CourseService.addEducator()", courseEducatorDTO.courseId()));
        User educator = userRepo.findById(Math.toIntExact(courseEducatorDTO.educatorId())).orElseThrow(
                //TODO: replace generic runtimeexception with custom exception
                () -> new RuntimeException(String.format("Could not find user by ID %d", courseEducatorDTO.educatorId())));

        //TODO: verify authenticated user owns course
        //TODO: verify educator does not already exist in course. add custom exception.


        course.getEducators().add(educator);
        educator.getTaughtCourses().add(course);

        Course savedCourse = courseRepo.save(course);
        User savedEducator = userRepo.save(educator);

        return new CourseEducatorResDTO("Successfully added educator to course.", savedCourse.getId(), (long) savedEducator.getId());
    }

    @Override
    public CourseEducatorResDTO removeEducator(CourseEducatorDTO courseEducatorDTO) {
        //verify course and educator exist
        Course course = courseRepo.findById(courseEducatorDTO.courseId()).orElseThrow(
                () -> new CourseNotFoundException("CourseServiceImp.removeEducator()", courseEducatorDTO.courseId()));
        User educator = userRepo.findById(Math.toIntExact(courseEducatorDTO.educatorId())).orElseThrow(
                //TODO: replace generic runtime exception with custom exception
                () -> new RuntimeException(String.format("Could not find user by ID %d", courseEducatorDTO.educatorId())));

        //TODO: verify authenticated user owns course
        //TODO: verify educator and course are connected, first? add custom exception.

        course.getEducators().remove(educator);
        educator.getTaughtCourses().remove(course);

        Course savedCourse = courseRepo.save(course);
        User savedEducator = userRepo.save(educator);

        return new CourseEducatorResDTO("Successfully removed educator from course.", savedCourse.getId(), (long) savedEducator.getId());
    }

    @Override
    public CourseStudentResDTO enrollStudent(CourseStudentDTO courseStudentDTO) {
        //verify course and student exit
        Course course = courseRepo.findById(courseStudentDTO.courseId()).orElseThrow(
                () -> new CourseNotFoundException("CourseServiceImp.removeEducator()", courseStudentDTO.courseId()));
        User student = userRepo.findById(Math.toIntExact(courseStudentDTO.studentId())).orElseThrow(
                //TODO: replace generic runtime exception with custom exception
                () -> new RuntimeException(String.format("Could not find user by ID %d", courseStudentDTO.studentId())));

        //TODO: verify authenticated user is provided student or proper authority like course educator or course institution
        // (Clean this up later when security is implemented.  Use security context to get current user)
        //TODO: verify student does not already exist in course. add custom exception.

//        course.getStudents().add(student);
//        student.getEnrolledCourses().add(course);

        Course savedCourse = courseRepo.save(course);
        User savedUser = userRepo.save(student);

        return new CourseStudentResDTO("Successfully enrolled student into course.", savedCourse.getId(), (long) savedUser.getId());
    }

    @Override
    public CourseStudentResDTO withdrawStudent(CourseStudentDTO courseStudentDTO) {
        //verify course and student exit
        Course course = courseRepo.findById(courseStudentDTO.courseId()).orElseThrow(
                () -> new CourseNotFoundException("CourseServiceImp.removeEducator()", courseStudentDTO.courseId()));
        User student = userRepo.findById(Math.toIntExact(courseStudentDTO.studentId())).orElseThrow(
                //TODO: replace generic runtime exception with custom exception
                () -> new RuntimeException(String.format("Could not find user by ID %d", courseStudentDTO.studentId())));

        //TODO: verify authenticated user is provided student or proper authority like course educator or course institution
        // (Clean this up later when security is implemented.  Use security context to get current user)
        //TODO: verify student and course are connected, first? add custom exception.

//        course.getStudents().remove(student);
//        student.getEnrolledCourses().remove(course);

        Course savedCourse = courseRepo.save(course);
        User savedUser = userRepo.save(student);

        return new CourseStudentResDTO("Successfully removed student from course.", savedCourse.getId(), (long) savedUser.getId());
    }

    @Override
    public List<CourseDTO> getAllByStudentId(Long studentId) {
        // TODO: Secure so only specified student or admin-like account ("counselor"?)
        // can retrieve data.
        // Will probably use Security context to obtain student id instead of path
        // variable
        User student = userRepo.findById(Math.toIntExact(studentId)).orElseThrow(() -> new RuntimeException("Could not find user."));
        return student.getEnrolledCourses().stream().map(courseMapper::toDto).toList();
    }

    @Override
    public List<CourseDTO> getAllByEducatorId(Long educatorId) {
        // TODO: Secure so only specified educator can retrieve data.
        // Will probably use Security context to obtain educator id instead of path
        // variable
        User educator = userRepo.findById(Math.toIntExact(educatorId)).orElseThrow(() -> new RuntimeException("Could not find user."));
        return educator.getTaughtCourses().stream().map(courseMapper::toDto).toList();
    }

    @Override
    public List<CourseDTO> getAllByInstitutionId(Long institutionId) {
        User institution = userRepo.findById(Math.toIntExact(institutionId)).orElseThrow(() -> new RuntimeException("Could not find user."));
        return institution.getInstitutionCourses().stream()
                .map(courseMapper::toDto).toList();
    }
}
