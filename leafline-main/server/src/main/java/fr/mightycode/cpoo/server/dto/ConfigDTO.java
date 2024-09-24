package fr.mightycode.cpoo.server.dto;


import jakarta.validation.constraints.NotEmpty;

public record ConfigDTO(@NotEmpty String domain) {
}
