package com.revlearn.team1.dto.page;

import java.time.LocalDateTime;
import java.util.List;

public record PageResDTO(
    Long id,
    String title,
    String markdownContent,
    Long pageNumber,
    String instructorNotes,
    List<String> attachmentsUrls,
    Long moduleId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){
}
