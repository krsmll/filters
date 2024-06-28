package me.krsmll.filtertask.filters.service;

import java.util.List;
import me.krsmll.filtertask.filters.dto.FilterCreationRequest;
import me.krsmll.filtertask.filters.dto.FilterDto;
import me.krsmll.filtertask.filters.dto.FiltersResponse;
import me.krsmll.filtertask.filters.dto.Target;

public interface FilterService {
    FiltersResponse getFilters();

    FilterDto getFilter(Long id);

    FilterDto createFilter(FilterCreationRequest filterSaveRequest);

    FilterDto updateFilter(FilterDto filterDto);

    void deleteFilter(Long id);

    List<Target> applyFilter(Long id, List<Target> targets);
}
