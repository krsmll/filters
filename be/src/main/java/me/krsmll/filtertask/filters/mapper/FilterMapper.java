package me.krsmll.filtertask.filters.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.krsmll.filtertask.filters.dto.FilterCreationRequest;
import me.krsmll.filtertask.filters.dto.FilterCriteriaCreationRequest;
import me.krsmll.filtertask.filters.dto.FilterCriteriaDto;
import me.krsmll.filtertask.filters.dto.FilterDto;
import me.krsmll.filtertask.filters.entity.Filter;
import me.krsmll.filtertask.filters.entity.FilterCriteria;
import me.krsmll.filtertask.filters.enums.Operation;
import me.krsmll.filtertask.filters.enums.ValueType;
import org.springframework.stereotype.Component;

@Component
public class FilterMapper {
    public List<FilterDto> toDtos(List<Filter> filters) {
        return filters.stream().map(this::toDto).toList();
    }

    public FilterDto toDto(Filter filter) {
        return new FilterDto(
                filter.getId(),
                filter.getName(),
                filter.getCriteria().stream().map(this::toCriteriaDto).toList());
    }

    public FilterCriteriaDto toCriteriaDto(FilterCriteria criteria) {
        return new FilterCriteriaDto(
                criteria.getId(),
                criteria.getType().name(),
                criteria.getOperation().name(),
                criteria.getValue());
    }

    public List<Filter> toEntities(List<FilterDto> filterDtos) {
        return filterDtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public Filter toEntity(FilterDto filterDto) {
        List<FilterCriteria> criteria =
                filterDto.criteria().stream().map(this::toCriteriaEntity).toList();

        Filter filter = new Filter();
        filter.setId(filterDto.id());
        filter.setName(filterDto.name());
        filter.setCriteria(criteria);

        return filter;
    }

    public Filter toEntity(FilterCreationRequest filterDto) {
        List<FilterCriteria> criteria =
                filterDto.criteria().stream().map(this::toCriteriaEntity).toList();

        Filter filter = new Filter();
        filter.setName(filterDto.name());
        filter.setCriteria(criteria);

        return filter;
    }

    public FilterCriteria toCriteriaEntity(FilterCriteriaDto criteriaDto) {
        FilterCriteria filter = new FilterCriteria();
        filter.setId(criteriaDto.id());
        filter.setType(ValueType.valueOf(criteriaDto.type()));
        filter.setOperation(Operation.valueOf(criteriaDto.operation()));
        filter.setValue(criteriaDto.value());
        return filter;
    }

    public FilterCriteria toCriteriaEntity(FilterCriteriaCreationRequest criteriaDto) {
        FilterCriteria filter = new FilterCriteria();
        filter.setType(ValueType.valueOf(criteriaDto.type()));
        filter.setOperation(Operation.valueOf(criteriaDto.operation()));
        filter.setValue(criteriaDto.value());
        return filter;
    }

    public Filter updateEntity(Filter filter, Filter other) {
        if (other.getId() != null) {
            filter.setId(other.getId());
        }

        filter.setName(other.getName());
        filter.setCriteria(updateCriteria(filter.getCriteria(), other.getCriteria()));
        return filter;
    }

    public List<FilterCriteria> updateCriteria(
            List<FilterCriteria> originalCriteria, List<FilterCriteria> updatedCriteria) {
        List<FilterCriteria> res = new ArrayList<>();
        for (FilterCriteria newCriteria : updatedCriteria) {
            Optional<FilterCriteria> existingCriteriaOpt = originalCriteria.stream()
                    .filter(criteria -> criteria.getId().equals(newCriteria.getId()))
                    .findFirst();

            if (existingCriteriaOpt.isPresent()) {
                FilterCriteria existingCriteria = existingCriteriaOpt.get();
                existingCriteria.setType(newCriteria.getType());
                existingCriteria.setOperation(newCriteria.getOperation());
                existingCriteria.setValue(newCriteria.getValue());
                res.add(existingCriteria);
            } else {
                res.add(newCriteria);
            }
        }

        return res;
    }
}
