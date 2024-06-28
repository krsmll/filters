package me.krsmll.filtertask.filters.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import me.krsmll.filtertask.filters.dto.FilterCreationRequest;
import me.krsmll.filtertask.filters.dto.FilterDto;
import me.krsmll.filtertask.filters.dto.FiltersResponse;
import me.krsmll.filtertask.filters.dto.Target;
import me.krsmll.filtertask.filters.entity.Filter;
import me.krsmll.filtertask.filters.entity.FilterCriteria;
import me.krsmll.filtertask.filters.mapper.FilterMapper;
import me.krsmll.filtertask.filters.repository.FilterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FilterServiceImpl implements FilterService {
    private final FilterRepository filterRepository;
    private final FilterMapper filterMapper;

    public FilterServiceImpl(FilterRepository filterRepository, FilterMapper filterMapper) {
        this.filterRepository = filterRepository;
        this.filterMapper = filterMapper;
    }

    @Override
    public FiltersResponse getFilters() {
        List<Filter> filters = filterRepository.findAll();
        List<FilterDto> filterDtos = filterMapper.toDtos(filters);
        return new FiltersResponse(filterDtos);
    }

    @Override
    public FilterDto getFilter(Long id) {
        Filter filter = filterRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filter not found"));
        return filterMapper.toDto(filter);
    }

    @Override
    public FilterDto createFilter(FilterCreationRequest filterSaveRequest) {
        Filter filter = filterMapper.toEntity(filterSaveRequest);
        Filter created = filterRepository.save(filter);

        return filterMapper.toDto(created);
    }

    @Override
    public FilterDto updateFilter(FilterDto filterDto) {
        Filter filter = filterRepository
                .findById(filterDto.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filter not found"));
        Filter toUpdate = filterMapper.updateEntity(filter, filterMapper.toEntity(filterDto));
        Filter updated = filterRepository.save(toUpdate);

        return filterMapper.toDto(updated);
    }

    @Override
    public void deleteFilter(Long id) {
        Optional<Filter> f = this.filterRepository.findById(id);
        if (f.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filter not found");
        }

        this.filterRepository.deleteById(id);
    }

    @Override
    public List<Target> applyFilter(Long id, List<Target> targets) {
        Filter filter = filterRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filter not found"));

        return targets.stream().filter(target -> applyFilter(filter, target)).toList();
    }

    private boolean applyFilter(Filter filter, Target target) {
        for (FilterCriteria criteria : filter.getCriteria()) {
            switch (criteria.getType()) {
                case AMOUNT -> {
                    if (!applyAmountCriteria(criteria, target)) {
                        return false;
                    }
                }
                case TEXT -> {
                    if (!applyTextCriteria(criteria, target)) {
                        return false;
                    }
                }
                case DATE -> {
                    if (!applyDateCriteria(criteria, target)) {
                        return false;
                    }
                }
                default -> throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid criteria type: " + criteria.getType());
            }
        }

        return true;
    }

    private boolean applyAmountCriteria(FilterCriteria criteria, Target target) {
        BigInteger criteriaValue = parseAmount(criteria.getValue())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid criteria amount value: " + criteria.getValue()));
        switch (criteria.getOperation()) {
            case EQUALS -> {
                return target.amount().compareTo(criteriaValue) == 0;
            }
            case LARGER_THAN -> {
                return target.amount().compareTo(criteriaValue) > 0;
            }
            case LESS_THAN -> {
                return target.amount().compareTo(criteriaValue) < 0;
            }
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid criteria operation: " + criteria.getOperation());
        }
    }

    private boolean applyTextCriteria(FilterCriteria criteria, Target target) {
        String value = target.title().toLowerCase();
        String other = criteria.getValue().toLowerCase();
        switch (criteria.getOperation()) {
            case EQUALS -> {
                return value.equals(other);
            }
            case CONTAINS -> {
                return value.contains(other);
            }
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid criteria operation: " + criteria.getOperation());
        }
    }

    private boolean applyDateCriteria(FilterCriteria criteria, Target target) {
        LocalDate targetDate = parseDate(target.date())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid target date value: " + target.date()));
        LocalDate criteriaDate = parseDate(criteria.getValue())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid criteria date value: " + criteria.getValue()));

        switch (criteria.getOperation()) {
            case FROM -> {
                return targetDate.isEqual(criteriaDate) || targetDate.isAfter(criteriaDate);
            }
            case TO -> {
                return targetDate.isEqual(criteriaDate) || targetDate.isBefore(criteriaDate);
            }
            case ON -> {
                return targetDate.isEqual(criteriaDate);
            }
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid criteria operation: " + criteria.getOperation());
        }
    }

    private Optional<BigInteger> parseAmount(String value) {
        try {
            return Optional.of(new BigInteger(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<LocalDate> parseDate(String value) {
        try {
            return Optional.of(LocalDate.parse(value));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
