package com.revlearn.team1.dto.page;

import java.util.List;

public record PageReqDTO(
    String title,
    String markdownContent,
    Long pageNumber,
    String instructorNotes,
    List<String> attachmentsUrls
) {
}
