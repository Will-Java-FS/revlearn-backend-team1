package com.revlearn.team1.service;

import com.revlearn.team1.model.ModulePage;
import com.revlearn.team1.model.Progress;
import com.revlearn.team1.repository.ProgressRepo;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    //@Autowired
    private ProgressRepo progressRepo;

    @Autowired
    public ProgressService(ProgressRepo progressRepo){
        this.progressRepo = progressRepo;
    }

    public List<Progress> getAllProgress(){
        return progressRepo.findAll();
    }

    public List<Progress> getProgressByUser(Long user_id){
        Optional<List<Progress>> userProgress = progressRepo.findByUser_id(user_id);
        return userProgress.orElse(null);
    }

    public List<Progress> getProgressByCourse(Long course_id){
        Optional<List<Progress>> courseProgress = progressRepo.findByCourse_id(course_id);
        return courseProgress.orElse(null);
    }

    public Progress getProgressByUserCourse(Long user_id, Long course_id){
        Optional<Progress> specificProgress = progressRepo.findByUser_idAndCourse_id(user_id, course_id);
        return specificProgress.orElse(null);
    }

    public Progress updateProgress(Long progress_Id, Progress progress) {
        Progress existingProgress = progressRepo.findById(progress_Id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Progress not found"));
        existingProgress.setCompleted(progress.getCompleted());
        existingProgress.setCompletedProgress(progress.getCompletedProgress());
        existingProgress.setCourse(progress.getCourse());
        existingProgress.setCourseModule(progress.getCourseModule());
        existingProgress.setModulePage(progress.getModulePage());
        existingProgress.setUser(progress.getUser());
        return progressRepo.save(existingProgress);
    }

    //want to implement some progress calculation to then .setProgressPercentage()
    public double getProgressForCourse(List<ModulePage> modulePages) {
        return calculateProgress(modulePages);
    }

    public static double calculateProgress(List<ModulePage> modulePages) {
        int completedModules = 0;

        for (ModulePage modulePage : modulePages) {
            //Not sure how to check this
            if (modulePage!=null) {
                completedModules++;
            }
        }

        return ((double) completedModules / modulePages.size()) * 100;
    }
}