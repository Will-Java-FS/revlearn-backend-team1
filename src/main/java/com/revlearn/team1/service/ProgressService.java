package com.revlearn.team1.service;

import com.revlearn.team1.model.Progress;
import com.revlearn.team1.repository.ProgressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    private ProgressRepo progressRepo;

    @Autowired
    public ProgressService(ProgressRepo progressRepo){
        this.progressRepo = progressRepo;
    }

    public List<Progress> getAllProgress(){
        return progressRepo.findAll();
    }
    /*Things we want to do:
    Students:
    - Get overall progress
    - Get progress by course
    Teacher/Institutions:
    - Get progress by course overall and by student
     */

    public List<Progress> getProgressByStudent(Long student_id){
        Optional<List<Progress>> studentProgress = progressRepo.findByStudent_id(student_id);
        if(studentProgress.isPresent()){
            return studentProgress.get();
        }
        return null;
    }

    public Progress getProgressByStudentCourse(Long student_id, Long course_id){
        Optional<Progress> specificProgress = progressRepo.findByStudent_idAndCourse_id(student_id, course_id);
        if (specificProgress.isPresent()){
            return specificProgress.get();
        }
        return null;
    }
}
