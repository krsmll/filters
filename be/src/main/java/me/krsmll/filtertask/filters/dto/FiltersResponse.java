package me.krsmll.filtertask.filters.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record FiltersResponse(
        @ArraySchema(schema = @Schema(implementation = FilterDto.class)) List<FilterDto> filters) {}
