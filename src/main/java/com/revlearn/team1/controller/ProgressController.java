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
public class ProgressController {

    private ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService){
        this.progressService = progressService;
    }
    /*
    Endpoints Needed:
    - GET progress
    - Update progress
     */
    //Return user's progress of all enrolled courses
    //Want this to be returning percentages
    @GetMapping("/progress")
    public ResponseEntity<List<Progress>> getAllProgress(){
        List<Progress> progress = progressService.getAllProgress();
        return ResponseEntity.status(200).body(progress);
    }

    @GetMapping("/progress/user/{user_id}")
    public ResponseEntity<List<Progress>> getProgressByUser(@PathVariable Long user_id){
        List<Progress> studentProgress = progressService.getProgressByUser(user_id);
        if(studentProgress.isEmpty()){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(200).body(studentProgress);
    }

    @GetMapping("/progress/course/{course_id}")
    public ResponseEntity<List<Progress>> getProgressByCourse(@PathVariable Long course_id){
        List<Progress> courseProgress = progressService.getProgressByCourse(course_id);
        if(courseProgress.isEmpty()){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(200).body(courseProgress);
    }

    //Returns progress for specific user in specific course
    @GetMapping("/progress/user/{user_id}/course/{course_id}")
    public ResponseEntity<Progress> getStudentCourseProgress(@PathVariable Long user_id, @PathVariable Long course_id){
        Progress progress = progressService.getProgressByUserCourse(user_id, course_id);
        return ResponseEntity.status(200).body(progress);
    }

    @PutMapping("/progress/{progress_id}")
    public ResponseEntity<Progress> updateProgress(@PathVariable Long progress_id, @RequestBody Progress progress){
        Progress updated = progressService.updateProgress(progress_id, progress);
        return ResponseEntity.status(200).body(updated);
    }
}