package com.revlearn.team1.service.course;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.exceptions.UserNotAuthorizedException;
import com.revlearn.team1.exceptions.UserNotFoundException;
import com.revlearn.team1.exceptions.course.*;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.UserRepository;
import com.revlearn.team1.service.securityContext.SecurityContextService;
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
    private final ModuleMapper moduleMapper;

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
    public List<User> getAllStudentsOfCourseId(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("getAllStudentsByCourseId()", courseId));

        //Guard clause to verify user is authorized to access course
        verifyStudentLevelAccess(course);

        return course.getStudents();
    }


    @Override
    public List<User> getAllEducatorsOfCourseId(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("getAllEducatorsByCourseId()", courseId));

        //Guard clause to verify user is authorized to access course
        verifyStudentLevelAccess(course);

        return course.getEducators();
    }

    @Override
    public CourseDTO getById(Long courseId) {
        Course retrievedCourse = courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("CourseService.getById()", courseId));
        return courseMapper.toDto(retrievedCourse);
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {

        Course course = courseMapper.fromDto(courseDTO);

        //Guard clause to verify user is authorized to create a course
        String userRole = SecurityContextService.getUserRole();
        if (!userRole.equals("Educator") && !userRole.equals("Institution"))
            throw new UserNotAuthorizedException("User is not authorized to create a course.  Must be an educator or institution.");

        //Get user
        Long userId = SecurityContextService.getUserId();
        User educator = userRepo.findById(Math.toIntExact(userId)).orElseThrow(
                () -> new UserNotFoundException("CourseService.createCourse() failed to find user by ID."));

        //Add educator to course
        course.getEducators().add(educator);
        educator.getTaughtCourses().add(course);

        //Save updated entities
        Course savedCourse = courseRepo.save(course);
        userRepo.save(educator);

        return courseMapper.toDto(savedCourse);
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {

        //Verify course already exists in database
        Course course = courseRepo.findById(courseDTO.id()).orElseThrow(
                () -> new CourseNotFoundException("CourseService.updateCourse()", courseDTO.id()));

        //verify user is authorized to update course
        verifyEducatorLevelAccess(course);

        //Update retrieved course object with courseDTO information
        courseMapper.updateCourseFromDto(course, courseDTO);

        //Save updated course to database
        Course savedCourse = courseRepo.save(course);

        //Return evidence of successful persistence as dto
        return courseMapper.toDto(savedCourse);
    }

    @Override
    public String deleteById(Long id) {

        //verify course exists
        Course course = courseRepo.findById(id).orElseThrow(
                () -> new CourseNotFoundException("CourseServiceImp.deleteById()", id));

        //verify user is authorized to delete course
        verifyEducatorLevelAccess(course);

        //delete
        courseRepo.deleteById(id);

        return String.format("Successfully deleted course %d.", id);
    }


    @Override
    public CourseStudentResDTO enrollStudent(CourseStudentDTO courseStudentDTO) {
        //verify course and student exist
        Course course = courseRepo.findById(courseStudentDTO.courseId()).orElseThrow(
                () -> new CourseNotFoundException("CourseServiceImp.removeEducator()", courseStudentDTO.courseId()));
        //Verify student does not already exist in course
        if (course.getStudents().stream().anyMatch(s -> s.getId() == courseStudentDTO.studentId())) {
            throw new StudentAlreadyEnrolledInCourseException(
                    String.format("Student by ID %d is already enrolled in course by ID %d.", courseStudentDTO.studentId(), course.getId()));
        }
        User student = userRepo.findById(Math.toIntExact(courseStudentDTO.studentId())).orElseThrow(
                () -> new UserNotFoundException(String.format("Could not find user by ID %d", courseStudentDTO.studentId())));

        Long userId = SecurityContextService.getUserId();
        if (userId != student.getId()) {
            //Throws exception if user is not course educator or institution
            verifyEducatorLevelAccess(course);
            throw new UserNotAuthorizedException(
                    "User is not authorized to enroll this student in course.  Must be the student, course educator, or institution."
            );
        }

        // TODO: How does Stripe payment system interact with this?

        course.getStudents().add(student);
        student.getEnrolledCourses().add(course);

        Course savedCourse = courseRepo.save(course);
        User savedUser = userRepo.save(student);

        return new CourseStudentResDTO("Successfully enrolled student into course.", savedCourse.getId(), (long) savedUser.getId());
    }

    @Override
    public CourseStudentResDTO withdrawStudent(CourseStudentDTO courseStudentDTO) {

        Course course = courseRepo.findById(courseStudentDTO.courseId()).orElseThrow(
                () -> new CourseNotFoundException("CourseServiceImp.removeEducator()", courseStudentDTO.courseId()));

        User student = course.getStudents().stream().filter(s -> s.getId() == courseStudentDTO.studentId()).findFirst()
                .orElseThrow(() -> new StudentNotEnrolledInCourseException(
                        String.format("Student by id %d was not found to be enrolled in course by id %d.", course.getId(), courseStudentDTO.studentId())));

        Long userId = SecurityContextService.getUserId();
        if (userId != student.getId()) {
            //Throws exception if user is not course educator or institution
            verifyEducatorLevelAccess(course);
            throw new UserNotAuthorizedException(
                    "User is not authorized to withdraw this student from course.  Must be the student, course educator, or institution."
            );
        }
        // TODO: How does Stripe payment system interact with this?

        course.getStudents().remove(student);
        student.getEnrolledCourses().remove(course);

        Course savedCourse = courseRepo.save(course);
        User savedUser = userRepo.save(student);

        return new CourseStudentResDTO("Successfully removed student from course.", savedCourse.getId(), (long) savedUser.getId());
    }

    @Override
    public CourseEducatorResDTO addEducator(CourseEducatorDTO courseEducatorDTO) {
        //verify course and educator exist
        Course course = courseRepo.findById(courseEducatorDTO.courseId()).orElseThrow(
                () -> new CourseNotFoundException("CourseService.addEducator()", courseEducatorDTO.courseId()));
        //Verify educator does not already teach course.
        if (course.getEducators().stream().anyMatch(e -> e.getId() == courseEducatorDTO.educatorId())) {
            throw new EducatorAlreadyTeachesCourseException(
                    String.format("Educator by id %d already teaches course by id %d", courseEducatorDTO.educatorId(), course.getId()));
        }
        User educator = userRepo.findById(Math.toIntExact(courseEducatorDTO.educatorId())).orElseThrow(
                () -> new UserNotFoundException(String.format("Could not find user by ID %d", courseEducatorDTO.educatorId())));

        verifyEducatorLevelAccess(course);

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
        //Verify educator and course are connected
        User educator = course.getEducators().stream().filter(e -> e.getId() == courseEducatorDTO.educatorId()).findFirst()
                .orElseThrow(() -> new EducatorDoesNotTeachCourseException(
                        String.format("Educator by id %d does not teach course by id %d.", courseEducatorDTO.educatorId(), course.getId())));

        //TODO: Consider restriction of self-removal of educator from course
        verifyEducatorLevelAccess(course);

        course.getEducators().remove(educator);
        educator.getTaughtCourses().remove(course);

        Course savedCourse = courseRepo.save(course);
        User savedEducator = userRepo.save(educator);

        return new CourseEducatorResDTO("Successfully removed educator from course.", savedCourse.getId(), (long) savedEducator.getId());
    }

    @Override
    public List<ModuleDTO> getModulesByCourseId(Long courseId) {

        Course course = courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("Course not found", courseId));

        verifyStudentLevelAccess(course);

        return course.getCourseModules().stream().map(moduleMapper::toDto).toList();
    }

    public void verifyStudentLevelAccess(Course course) {

        Long userId = SecurityContextService.getUserId();
        String userRole = SecurityContextService.getUserRole();

        if (course.getStudents().stream().noneMatch(s -> s.getId() == userId)
                && course.getEducators().stream().noneMatch(e -> e.getId() == userId)
                && !userRole.equals("Institution"))
            throw new UserNotAuthorizedException(
                    "User is not authorized to access this course.  Must be enrolled student, assigned educator, or administrator.");
    }

    public void verifyEducatorLevelAccess(Course course) {

        Long userId = SecurityContextService.getUserId();
        String userRole = SecurityContextService.getUserRole();

        if (course.getEducators().stream().noneMatch(e -> e.getId() == userId)
                && !userRole.equals("Institution"))
            throw new UserNotAuthorizedException(
                    "User is not authorized to access this course.  Must be assigned educator or administrator.");
    }
}
