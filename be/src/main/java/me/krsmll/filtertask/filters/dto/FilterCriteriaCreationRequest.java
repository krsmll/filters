package me.krsmll.filtertask.filters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import me.krsmll.filtertask.common.validation.ValidFilterCriteriaCombination;
import me.krsmll.filtertask.common.validation.ValueOfEnum;
import me.krsmll.filtertask.filters.enums.Operation;
import me.krsmll.filtertask.filters.enums.ValueType;
import org.hibernate.validator.constraints.Length;

// spotless:off
@ValidFilterCriteriaCombination
public record FilterCriteriaCreationRequest(
        @NotNull
        @ValueOfEnum(enumClass = ValueType.class)
        @Schema(description = "Type of the filter criteria", example = "AMOUNT")
        String type,

        @NotNull
        @ValueOfEnum(enumClass = Operation.class)
        @Schema(description = "Operation of the filter criteria", example = "LESS_THAN")
        String operation,

        @NotNull
        @Length(min = 1, max = 255)
        @Schema(description = "Value of the filter criteria", example = "1000")
        String value
) {}
// spotless:on
