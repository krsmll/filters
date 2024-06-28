package me.krsmll.filtertask.filters.controller;

import jakarta.validation.Valid;
import java.util.List;
import me.krsmll.filtertask.filters.controller.spec.FilterControllerSpec;
import me.krsmll.filtertask.filters.dto.FilterCreationRequest;
import me.krsmll.filtertask.filters.dto.FilterDto;
import me.krsmll.filtertask.filters.dto.FiltersResponse;
import me.krsmll.filtertask.filters.dto.Target;
import me.krsmll.filtertask.filters.service.FilterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filters")
public class FilterController implements FilterControllerSpec {
    private final FilterService filterService;

    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<FiltersResponse> getFilters() {
        return ResponseEntity.ok(filterService.getFilters());
    }

    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<FilterDto> getFilter(@PathVariable Long id) {
        return ResponseEntity.ok(filterService.getFilter(id));
    }

    @PostMapping
    @CrossOrigin
    public ResponseEntity<FilterDto> createFilter(@RequestBody @Valid FilterCreationRequest filterCreationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filterService.createFilter(filterCreationRequest));
    }

    @PutMapping
    @CrossOrigin
    public ResponseEntity<FilterDto> updateFilter(@RequestBody @Valid FilterDto filterDto) {
        return ResponseEntity.ok(filterService.updateFilter(filterDto));
    }

    @DeleteMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<Void> deleteFilter(@PathVariable Long id) {
        filterService.deleteFilter(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/apply")
    @CrossOrigin
    public ResponseEntity<List<Target>> applyFilter(@PathVariable Long id, @RequestBody List<Target> targets) {
        return ResponseEntity.ok(filterService.applyFilter(id, targets));
    }
}
