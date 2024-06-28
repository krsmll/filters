package me.krsmll.filtertask.filters.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.Length;

// spotless:off
public record FilterCreationRequest(

        @NotNull @Length(min = 1, max = 255)
        @Schema(description = "Filter name", example = "Some Filter")
        String name,

        @NotNull
        @NotEmpty
        @Valid
        @ArraySchema(schema = @Schema(implementation = FilterCriteriaCreationRequest.class))
        List<FilterCriteriaCreationRequest> criteria
) {}
// spotless:on
