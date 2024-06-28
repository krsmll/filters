package me.krsmll.filtertask.filters.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import me.krsmll.filtertask.filters.dto.FilterCreationRequest;
import me.krsmll.filtertask.filters.dto.FilterDto;
import me.krsmll.filtertask.filters.dto.FiltersResponse;
import me.krsmll.filtertask.filters.dto.Target;
import org.springframework.http.ResponseEntity;

public interface FilterControllerSpec {
    @Operation(summary = "Get all filters")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Filters were successfully retrieved",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FiltersResponse.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Invalid request"),
                @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
            })
    ResponseEntity<FiltersResponse> getFilters();

    @Operation(summary = "Get filter by ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Filter was successfully retrieved",
                        content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = FilterDto.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Invalid request"),
                @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
            })
    ResponseEntity<FilterDto> getFilter(Long id);

    @Operation(summary = "Create filter")
    @RequestBody(
            description = "Filter to create",
            required = true,
            content = @Content(schema = @Schema(implementation = FilterCreationRequest.class)))
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Filter was successfully created",
                        content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = FilterDto.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Invalid request"),
                @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
            })
    ResponseEntity<FilterDto> createFilter(FilterCreationRequest filterCreationRequest);

    @Operation(summary = "Update filter")
    @RequestBody(
            description = "Filter to update",
            required = true,
            content = @Content(schema = @Schema(implementation = FilterDto.class)))
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Filter was successfully updated",
                        content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = FilterDto.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Invalid request"),
                @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
            })
    ResponseEntity<FilterDto> updateFilter(FilterDto filterDto);

    @Operation(summary = "Delete filter")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Filter was successfully deleted"),
                @ApiResponse(responseCode = "400", description = "Invalid request"),
                @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
            })
    ResponseEntity<Void> deleteFilter(Long id);

    @Operation(summary = "Apply filter")
    @RequestBody(
            description = "Filter ID and list of targets to apply the filter to",
            required = true,
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Target.class))))
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Filter was successfully applied",
                        content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Target.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Invalid request"),
                @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
            })
    ResponseEntity<List<Target>> applyFilter(Long id, List<Target> targets);
}
