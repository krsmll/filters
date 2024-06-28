package me.krsmll.filtertask.common.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import me.krsmll.filtertask.filters.entity.Filter;
import me.krsmll.filtertask.filters.entity.FilterCriteria;
import me.krsmll.filtertask.filters.enums.Operation;
import me.krsmll.filtertask.filters.enums.ValueType;
import me.krsmll.filtertask.filters.repository.FilterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntegrationTest {

    @Autowired
    private FilterRepository filterRepository;

    @BeforeEach
    public void beforeEach() {
        Filter filter1 = new Filter();
        FilterCriteria criteria1 = new FilterCriteria();
        criteria1.setType(ValueType.AMOUNT);
        criteria1.setOperation(Operation.LESS_THAN);
        criteria1.setValue("1000");

        FilterCriteria criteria2 = new FilterCriteria();
        criteria2.setType(ValueType.TEXT);
        criteria2.setOperation(Operation.CONTAINS);
        criteria2.setValue("test");

        filter1.setName("Some Filter");
        filter1.setCriteria(List.of(criteria1, criteria2));

        Filter filter2 = new Filter();
        FilterCriteria criteria3 = new FilterCriteria();
        criteria3.setType(ValueType.AMOUNT);
        criteria3.setOperation(Operation.LARGER_THAN);
        criteria3.setValue("1000");

        filter2.setName("Another Filter");
        filter2.setCriteria(List.of(criteria3));

        filterRepository.saveAll(List.of(filter1, filter2));
        filterRepository.flush();
    }

    @AfterEach
    public void afterEach() {
        filterRepository.deleteAll();
        filterRepository.flush();
    }

    protected String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
