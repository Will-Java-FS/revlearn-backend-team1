package com.revlearn.team1.dto.program;

public record ProgramResDTO(Long id, String title, String description, String department, String degree,
                            String duration, String location, String format, float cost) {
}
