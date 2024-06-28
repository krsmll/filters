package me.krsmll.filtertask.filters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;

public record Target(
        @NotNull @Schema(description = "Amount of the target", example = "1000") BigInteger amount,
        @NotNull @Schema(description = "Title of the target", example = "Some Target") String title,
        @NotNull @Schema(description = "Date of the target", example = "2021-10-10") String date) {}
