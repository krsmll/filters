package me.krsmll.filtertask.filters.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.List;
import me.krsmll.filtertask.common.controller.AbstractIntegrationTest;
import me.krsmll.filtertask.filters.dto.*;
import me.krsmll.filtertask.filters.enums.Operation;
import me.krsmll.filtertask.filters.enums.ValueType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class FilterControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getFilters() throws Exception {
        mockMvc.perform(get("/filters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filters", hasSize(2)))
                .andExpect(jsonPath("$.filters[0].name", is("Some Filter")))
                .andExpect(jsonPath("$.filters[0].criteria", hasSize(2)))
                .andExpect(jsonPath("$.filters[0].criteria[0].type", is("AMOUNT")))
                .andExpect(jsonPath("$.filters[0].criteria[0].operation", is("LESS_THAN")))
                .andExpect(jsonPath("$.filters[0].criteria[0].value", is("1000")))
                .andExpect(jsonPath("$.filters[0].criteria[1].type", is("TEXT")))
                .andExpect(jsonPath("$.filters[0].criteria[1].operation", is("CONTAINS")))
                .andExpect(jsonPath("$.filters[0].criteria[1].value", is("test")))
                .andExpect(jsonPath("$.filters[1].name", is("Another Filter")))
                .andExpect(jsonPath("$.filters[1].criteria", hasSize(1)))
                .andExpect(jsonPath("$.filters[1].criteria[0].type", is("AMOUNT")))
                .andExpect(jsonPath("$.filters[1].criteria[0].operation", is("LARGER_THAN")))
                .andExpect(jsonPath("$.filters[1].criteria[0].value", is("1000")));
    }

    @Test
    void getFilter_exists() throws Exception {
        mockMvc.perform(get("/filters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Some Filter")))
                .andExpect(jsonPath("$.criteria", hasSize(2)))
                .andExpect(jsonPath("$.criteria[0].type", is("AMOUNT")))
                .andExpect(jsonPath("$.criteria[0].operation", is("LESS_THAN")))
                .andExpect(jsonPath("$.criteria[0].value", is("1000")))
                .andExpect(jsonPath("$.criteria[1].type", is("TEXT")))
                .andExpect(jsonPath("$.criteria[1].operation", is("CONTAINS")))
                .andExpect(jsonPath("$.criteria[1].value", is("test")));
    }

    @Test
    void getFilter_notExists() throws Exception {
        mockMvc.perform(get("/filters/3"))
                .andExpect(status().isNotFound())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    void createFilter_success() throws Exception {
        FilterCriteriaCreationRequest criteria =
                new FilterCriteriaCreationRequest(ValueType.AMOUNT.name(), Operation.EQUALS.name(), "727");
        FilterCreationRequest filter = new FilterCreationRequest("New Filter", List.of(criteria));

        mockMvc.perform(post("/filters")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(asJsonString(filter)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Filter")))
                .andExpect(jsonPath("$.criteria", hasSize(1)))
                .andExpect(jsonPath("$.criteria[0].type", is("AMOUNT")))
                .andExpect(jsonPath("$.criteria[0].operation", is("EQUALS")))
                .andExpect(jsonPath("$.criteria[0].value", is("727")));

        mockMvc.perform(get("/filters")).andExpect(status().isOk()).andExpect(jsonPath("$.filters", hasSize(3)));
    }

    @Test
    void createFilter_emptyCriteria_badRequest() throws Exception {
        FilterCreationRequest filter = new FilterCreationRequest("New Filter", List.of());

        mockMvc.perform(post("/filters")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(asJsonString(filter)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilter_emptyName_badRequest() throws Exception {
        FilterCriteriaCreationRequest criteria =
                new FilterCriteriaCreationRequest(ValueType.AMOUNT.name(), Operation.EQUALS.name(), "727");
        FilterCreationRequest filter = new FilterCreationRequest("", List.of(criteria));

        mockMvc.perform(post("/filters")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(asJsonString(filter)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilter_wrongEnumType_badRequest() throws Exception {
        FilterCriteriaCreationRequest criteria =
                new FilterCriteriaCreationRequest("WRONG_ENUM_TYPE", Operation.EQUALS.name(), "727");
        FilterCreationRequest filter = new FilterCreationRequest("heh", List.of(criteria));
        mockMvc.perform(post("/filters")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(asJsonString(filter)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilter_badCriteriaCombo_badRequest() throws Exception {
        FilterCriteriaCreationRequest criteria =
                new FilterCriteriaCreationRequest(ValueType.AMOUNT.name(), Operation.CONTAINS.name(), "727");
        FilterCreationRequest filter = new FilterCreationRequest("heh", List.of(criteria));
        mockMvc.perform(post("/filters")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(asJsonString(filter)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFilter_success() throws Exception {
        FilterCriteriaDto criteria = new FilterCriteriaDto(3L, ValueType.AMOUNT.name(), Operation.EQUALS.name(), "727");

        FilterDto filter = new FilterDto(2L, "Updated Filter", List.of(criteria));
        mockMvc.perform(put("/filters")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(asJsonString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Filter")))
                .andExpect(jsonPath("$.criteria", hasSize(1)))
                .andExpect(jsonPath("$.criteria[0].type", is("AMOUNT")))
                .andExpect(jsonPath("$.criteria[0].operation", is("EQUALS")))
                .andExpect(jsonPath("$.criteria[0].value", is("727")));
    }

    @Test
    void updateFilter_notExists() throws Exception {
        FilterCriteriaDto criteria =
                new FilterCriteriaDto(null, ValueType.AMOUNT.name(), Operation.EQUALS.name(), "727");

        FilterDto filter = new FilterDto(6L, "Updated Filter", List.of(criteria));
        mockMvc.perform(put("/filters")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(asJsonString(filter)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFilter_success() throws Exception {
        mockMvc.perform(delete("/filters/2")).andExpect(status().isNoContent());

        mockMvc.perform(get("/filters")).andExpect(status().isOk()).andExpect(jsonPath("$.filters", hasSize(1)));
    }

    @Test
    void deleteFilter_notFound() throws Exception {
        mockMvc.perform(delete("/filters/6")).andExpect(status().isNotFound());
    }

    @Test
    void applyFilter_success() throws Exception {
        List<Target> targets = List.of(
                new Target(BigInteger.valueOf(789L), "Title test", "2021-06-26"),
                new Target(BigInteger.valueOf(1001L), "Hehe", "2021-06-25"),
                new Target(BigInteger.valueOf(10), "ABCDEFGtest", "2024-06-21"));

        mockMvc.perform(post("/filters/1/apply")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(asJsonString(targets)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount", is(789)))
                .andExpect(jsonPath("$[0].title", is("Title test")))
                .andExpect(jsonPath("$[0].date", is("2021-06-26")))
                .andExpect(jsonPath("$[1].amount", is(10)))
                .andExpect(jsonPath("$[1].title", is("ABCDEFGtest")))
                .andExpect(jsonPath("$[1].date", is("2024-06-21")));
    }
}
