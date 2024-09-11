package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.exam.ExamReqDTO;
import com.revlearn.team1.dto.exam.ExamResDTO;
import com.revlearn.team1.model.Exam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExamMapper {
    private final ExamQuestionMapper examQuestionMapper;
    public ExamResDTO toExamResDTO(Exam exam) {
        return new ExamResDTO(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getInstructions(),
                exam.getDuration(),
                exam.getType(),
                exam.getModule().getId(),
                Optional.ofNullable(exam.getQuestions()).orElse(Collections.emptyList())
                        .stream().map(examQuestionMapper::toExamQuestionResDTO).toList(),
                exam.getCreatedAt(),
                exam.getUpdatedAt()
        );
    }

    public Exam toExam(ExamReqDTO examReqDTO) {
        Exam exam = new Exam();

        exam.setTitle(examReqDTO.title());
        exam.setDescription(examReqDTO.description());
        exam.setInstructions(examReqDTO.instructions());
        exam.setDuration(examReqDTO.duration());
        exam.setType(examReqDTO.type());

        return exam;
    }
}
