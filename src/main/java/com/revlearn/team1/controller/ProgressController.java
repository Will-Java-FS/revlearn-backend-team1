package com.revlearn.team1.controller;

import com.revlearn.team1.model.Progress;
import com.revlearn.team1.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService){
        this.progressService = progressService;
    }

    /**
     * GET All progress for every course, user etc.
     * @return List<Progress>
     */
    @GetMapping
    public ResponseEntity<List<Progress>> getAllProgress(){
        List<Progress> progress = progressService.getAllProgress();
        return ResponseEntity.status(200).body(progress);
    }

    /**
     * GET All progress for specific user.
     * @param user_id: id of user you want to obtain progress for
     * @return List<Progress> for user by user_id
     */
    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<Progress>> getProgressByUser(@PathVariable Long user_id){
        List<Progress> studentProgress = progressService.getProgressByUser(user_id);
        if(studentProgress.isEmpty()){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(200).body(studentProgress);
    }

    /**
     * GET all progress by course
     * @param course_id: id for course you want to obtain progress for
     * @return List<Progress> for course by course_id
     */
    @GetMapping("/course/{course_id}")
    public ResponseEntity<List<Progress>> getProgressByCourse(@PathVariable Long course_id){
        List<Progress> courseProgress = progressService.getProgressByCourse(course_id);
        if(courseProgress.isEmpty()){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(200).body(courseProgress);
    }

    /**
     * GET progress for specific user in a specific course
     * @param user_id: id of user you want to obtain progress for
     * @param course_id: id for course you want to obtain progress for
     * @return Progress entity
     */
    @GetMapping("/user/{user_id}/course/{course_id}")
    public ResponseEntity<Progress> getStudentCourseProgress(@PathVariable Long user_id, @PathVariable Long course_id){
        Progress progress = progressService.getProgressByUserCourse(user_id, course_id);
        return ResponseEntity.status(200).body(progress);
    }

    /**
     * Updates progress by progress_id
     * @param progress_id: id of progress entity to be updated
     * @param progress: entity that obtains the updates to be applied
     * @return Updated progress
     */
    @PutMapping("/{progress_id}")
    public ResponseEntity<Progress> updateProgress(@PathVariable Long progress_id, @RequestBody Progress progress){
        Progress updated = progressService.updateProgress(progress_id, progress);
        return ResponseEntity.status(200).body(updated);
    }
}