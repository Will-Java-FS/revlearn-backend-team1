package com.revlearn.team1.controller;

import com.revlearn.team1.model.Progress;
import com.revlearn.team1.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    //Return student's progress of all enrolled courses
    //Want this to be returning percentages
    @GetMapping("/progress")
    public ResponseEntity<List<Progress>> getAllProgress(){
        List<Progress> progress = progressService.getAllProgress();
        return ResponseEntity.status(200).body(progress);
    }

    @GetMapping("/progress/{student_id}")
    public ResponseEntity<List<Progress>> getStudentProgress(@PathVariable Long student_id){
        List<Progress> studentProgress = progressService.getProgressByStudent(student_id);
        if(studentProgress.isEmpty()){
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(200).body(studentProgress);
    }

    //Returns progress for specific student in specific course
    @GetMapping("/progress/{student_id}/{course_id}")
    public ResponseEntity<Progress> getStudentCourseProgress(@PathVariable Long student_id, @PathVariable Long course_id){
        Progress progress = progressService.getProgressByStudentCourse(student_id, course_id);
        return ResponseEntity.status(200).body(progress);
    }
}
