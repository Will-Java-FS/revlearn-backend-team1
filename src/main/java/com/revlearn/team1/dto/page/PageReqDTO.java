package com.revlearn.team1.dto.page;

import java.util.List;

public record PageReqDTO(
    String title,
    String markdownContent,
    String instructorNotes,
    List<String> attachmentsUrls
) {
}
