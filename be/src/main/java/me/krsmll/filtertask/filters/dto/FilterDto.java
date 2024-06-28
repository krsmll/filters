package me.krsmll.filtertask.filters.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record FilterDto(
        @NotNull @Min(1) @Schema(description = "Filter ID", example = "1") Long id,
        @NotNull @Length(min = 1, max = 255) @Schema(description = "Filter name", example = "Some Filter") String name,
        @NotNull @NotEmpty @Valid @ArraySchema(schema = @Schema(implementation = FilterCriteriaDto.class))
                List<FilterCriteriaDto> criteria) {}
