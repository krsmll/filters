package me.krsmll.filtertask.filters.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import me.krsmll.filtertask.filters.dto.*;
import me.krsmll.filtertask.filters.entity.Filter;
import me.krsmll.filtertask.filters.entity.FilterCriteria;
import me.krsmll.filtertask.filters.enums.Operation;
import me.krsmll.filtertask.filters.enums.ValueType;
import me.krsmll.filtertask.filters.mapper.FilterMapper;
import me.krsmll.filtertask.filters.repository.FilterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class FilterServiceImplTest {
    @Mock
    private FilterRepository filterRepository;

    @Spy
    private FilterMapper filterMapper;

    @InjectMocks
    private FilterServiceImpl filterService;

    @Test
    public void getFilters_success() {
        when(filterRepository.findAll()).thenReturn(getFiltersTestData());

        var result = filterService.getFilters();
        assertThat(result).isNotNull();
        assertThat(result.filters()).hasSize(3);
    }

    @Test
    public void getFilter_success() {
        when(filterRepository.findById(1L))
                .thenReturn(getFiltersTestData().stream().findFirst());

        var result = filterService.getFilter(1L);
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("filter1");
        assertThat(result.criteria()).hasSize(2);
        assertThat(result.criteria().getFirst().id()).isEqualTo(1L);
        assertThat(result.criteria().getFirst().type()).isEqualTo(ValueType.AMOUNT.name());
        assertThat(result.criteria().getFirst().operation()).isEqualTo(Operation.LESS_THAN.name());
        assertThat(result.criteria().getFirst().value()).isEqualTo("1000");
        assertThat(result.criteria().get(1).id()).isEqualTo(2L);
        assertThat(result.criteria().get(1).type()).isEqualTo(ValueType.TEXT.name());
        assertThat(result.criteria().get(1).operation()).isEqualTo(Operation.CONTAINS.name());
        assertThat(result.criteria().get(1).value()).isEqualTo("test");
    }

    @Test
    public void getFilter_notFound_throws() {
        when(filterRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filterService.getFilter(5L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Filter not found\"");
    }

    @Test
    public void createFilter_success() {
        FilterCriteriaCreationRequest criteriaCreationRequest =
                new FilterCriteriaCreationRequest(ValueType.TEXT.name(), Operation.EQUALS.name(), "THIS EXACT STRING");
        FilterCreationRequest creationRequest =
                new FilterCreationRequest("newFilter", List.of(criteriaCreationRequest));

        Filter newFilter = getNewFilterWithNoCriteria();
        FilterCriteria newCriteria = getNewCriteria();
        newFilter.setCriteria(List.of(newCriteria));

        Filter expectedFilter = getNewFilterWithNoCriteria();
        FilterCriteria expectedCriteria = getNewCriteria();
        expectedFilter.setId(4L);
        expectedCriteria.setId(5L);
        expectedFilter.setCriteria(List.of(expectedCriteria));

        when(filterRepository.save(eq(newFilter))).thenReturn(expectedFilter);

        var result = filterService.createFilter(creationRequest);
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(4L);
        assertThat(result.name()).isEqualTo(creationRequest.name());
        assertThat(result.criteria()).hasSize(1);
        assertThat(result.criteria().getFirst().id()).isEqualTo(5L);
        assertThat(result.criteria().getFirst().type()).isEqualTo(criteriaCreationRequest.type());
        assertThat(result.criteria().getFirst().operation()).isEqualTo(criteriaCreationRequest.operation());
        assertThat(result.criteria().getFirst().value()).isEqualTo(criteriaCreationRequest.value());
    }

    @Test
    public void updateFilter_success() {
        FilterCriteriaDto filterCriteriaDto =
                new FilterCriteriaDto(3L, ValueType.TEXT.name(), Operation.EQUALS.name(), "THIS EXACT STRING");
        FilterDto filterDto = new FilterDto(2L, "filter2 but different", List.of(filterCriteriaDto));

        Filter old = getFiltersTestData().get(1);
        Filter newFilter = getNewFilterWithNoCriteria();
        FilterCriteria newCriteria = getNewCriteria();
        newFilter.setCriteria(List.of(newCriteria));
        newFilter.setName(filterDto.name());
        newFilter.setId(filterDto.id());
        newCriteria.setId(filterCriteriaDto.id());

        when(filterRepository.findById(2L)).thenReturn(Optional.of(old));
        when(filterRepository.save(eq(newFilter))).thenReturn(newFilter);

        var result = filterService.updateFilter(filterDto);
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo(filterDto.name());
        assertThat(result.criteria()).hasSize(1);
        assertThat(result.criteria().getFirst().id()).isEqualTo(3L);
        assertThat(result.criteria().getFirst().type()).isEqualTo(filterCriteriaDto.type());
        assertThat(result.criteria().getFirst().operation()).isEqualTo(filterCriteriaDto.operation());
        assertThat(result.criteria().getFirst().value()).isEqualTo(filterCriteriaDto.value());
    }

    @Test
    public void updateFilter_notFound() {
        FilterCriteriaDto filterCriteriaDto =
                new FilterCriteriaDto(null, ValueType.TEXT.name(), Operation.EQUALS.name(), "THIS EXACT STRING");
        FilterDto filterDto = new FilterDto(5L, "filter2 but different", List.of(filterCriteriaDto));

        when(filterRepository.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> filterService.updateFilter(filterDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Filter not found\"");
    }

    @Test
    public void deleteFilter_success() {
        when(filterRepository.findById(1L))
                .thenReturn(getFiltersTestData().stream().findFirst());

        filterService.deleteFilter(1L);
        verify(filterRepository).deleteById(1L);
    }

    @Test
    public void applyFilter_success() {
        when(filterRepository.findById(1L))
                .thenReturn(getFiltersTestData().stream().findFirst());

        var result = filterService.applyFilter(1L, getTargetsTestData());
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().amount()).isEqualTo(BigInteger.valueOf(727));
        assertThat(result.getFirst().title()).isEqualTo("hehetest");
        assertThat(result.getFirst().date()).isEqualTo("1970-01-01");
    }

    private List<Filter> getFiltersTestData() {
        Filter f1 = new Filter();
        f1.setId(1L);
        f1.setName("filter1");

        FilterCriteria c1 = new FilterCriteria();
        c1.setId(1L);
        c1.setType(ValueType.AMOUNT);
        c1.setOperation(Operation.LESS_THAN);
        c1.setValue("1000");

        FilterCriteria c2 = new FilterCriteria();
        c2.setId(2L);
        c2.setType(ValueType.TEXT);
        c2.setOperation(Operation.CONTAINS);
        c2.setValue("test");
        f1.setCriteria(List.of(c1, c2));

        Filter f2 = new Filter();
        f2.setId(2L);
        f2.setName("filter2");

        FilterCriteria c3 = new FilterCriteria();
        c3.setId(3L);
        c3.setType(ValueType.AMOUNT);
        c3.setOperation(Operation.LESS_THAN);
        c3.setValue("700");
        f2.setCriteria(List.of(c3));

        Filter f3 = new Filter();
        f3.setId(3L);
        f3.setName("filter3");

        FilterCriteria c4 = new FilterCriteria();
        c4.setId(4L);
        c4.setType(ValueType.DATE);
        c4.setOperation(Operation.EQUALS);
        c4.setValue("2021-01-01");
        f3.setCriteria(List.of(c4));

        return List.of(f1, f2, f3);
    }

    private Filter getNewFilterWithNoCriteria() {
        Filter filter = new Filter();
        filter.setName("newFilter");

        return filter;
    }

    private FilterCriteria getNewCriteria() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setType(ValueType.TEXT);
        criteria.setOperation(Operation.EQUALS);
        criteria.setValue("THIS EXACT STRING");
        return criteria;
    }

    private List<Target> getTargetsTestData() {
        Target t1 = new Target(BigInteger.valueOf(1000), "test", "1970-01-01");

        Target t2 = new Target(BigInteger.valueOf(727), "hehetest", "1970-01-01");

        return List.of(t1, t2);
    }
}
