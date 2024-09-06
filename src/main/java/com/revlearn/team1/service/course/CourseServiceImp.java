package com.revlearn.team1.service.course;

import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.course.request.CourseReqDTO;
import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.exceptions.UserNotFoundException;
import com.revlearn.team1.exceptions.course.*;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.mapper.ModuleMapper;
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
    private final ModuleMapper moduleMapper;

    @Override
    public List<CourseResDTO> getAll() {
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
        //TODO: Implement security.  Only course owners (educators and institution) should be able to access this route.
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("getAllStudentsByCourseId()", courseId));
        return course.getStudents();
    }

    @Override
    public List<User> getAllEducatorsOfCourseId(Long courseId) {
        //TODO: Implement security.  Only course owners (educators and institution) should be able to access this route.
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("getAllEducatorsByCourseId()", courseId));
        return course.getEducators();
    }

    @Override
    public CourseResDTO getById(Long courseId) {
        Course retrievedCourse = courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("CourseService.getById()", courseId));
        return courseMapper.toDto(retrievedCourse);
    }

    @Override
    public CourseResDTO createCourse(CourseReqDTO courseReqDTO) {
        //TODO: Secure so that only instructors and institutions can create courses
        // Maybe require user information as parameter
        Course course = courseMapper.fromReqDto(courseReqDTO);
        Course savedCourse = courseRepo.save(course);
        return courseMapper.toDto(savedCourse);
    }

    @Override
    public CourseResDTO updateCourse(Long courseId, CourseReqDTO courseReqDTO) {
        //TODO: Secure so that only course owners (educator(s) or institution) can modify.
        // Compare provided course information (educator(s) id(s), or institutionId) to authenticated user properties.

        //Verify course already exists in database
        Course course = courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("CourseService.updateCourse()", courseId));


        //Update retrieved course object with courseDTO information
        courseMapper.updateCourseFromReqDto(course, courseReqDTO);

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

        //TODO: verify authenticated user is provided student or proper authority like course educator or course institution
        // (Clean this up later when security is implemented.  Use security context to get current user)
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

        //TODO: verify authenticated user is provided student or proper authority like course educator or course institution
        // (Clean this up later when security is implemented.  Use security context to get current user)
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

        //TODO: verify authenticated user owns course (educator or institution)

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

        //TODO: verify authenticated user owns course (educator or institution)

        course.getEducators().remove(educator);
        educator.getTaughtCourses().remove(course);

        Course savedCourse = courseRepo.save(course);
        User savedEducator = userRepo.save(educator);

        return new CourseEducatorResDTO("Successfully removed educator from course.", savedCourse.getId(), (long) savedEducator.getId());
    }

    @Override
    public List<ModuleDTO> getModulesByCourseId(Long courseId) {
        //TODO: Secure so only course affiliated users can access (enrolled students, assigned educators, & institution)
        //TODO: Consider relocation to CourseService class
        Course course = courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("Course not found", courseId));
        return course.getCourseModules().stream().map(moduleMapper::toDto).toList();
    }
}
