package me.krsmll.filtertask.filters.entity;

import jakarta.persistence.*;
import java.util.Objects;
import me.krsmll.filtertask.filters.enums.Operation;
import me.krsmll.filtertask.filters.enums.ValueType;

@Entity
@Table(name = "filter_criteria")
public class FilterCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type")
    private ValueType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private Operation operation;

    @Column(name = "operated_value")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterCriteria that = (FilterCriteria) o;

        if (!Objects.equals(id, that.id)) return false;
        if (type != that.type) return false;
        if (operation != that.operation) return false;
        return Objects.equals(value, that.value);
    }
}
