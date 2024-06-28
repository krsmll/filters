package me.krsmll.filtertask.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.krsmll.filtertask.filters.dto.FilterCriteriaCreationRequest;
import me.krsmll.filtertask.filters.dto.FilterCriteriaDto;
import me.krsmll.filtertask.filters.enums.Operation;
import me.krsmll.filtertask.filters.enums.ValueType;

public class ValidFilterCriteriaCombinationValidator
        implements ConstraintValidator<ValidFilterCriteriaCombination, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return switch (o) {
            case null -> true;
            case FilterCriteriaDto dto -> {
                if (!isValidValueType(dto.type()) || !isValidOperationValue(dto.operation())) {
                    yield false;
                }
                yield isValid(ValueType.valueOf(dto.type()), Operation.valueOf(dto.operation()));
            }
            case FilterCriteriaCreationRequest creationReq -> {
                if (!isValidValueType(creationReq.type()) || !isValidOperationValue(creationReq.operation())) {
                    yield false;
                }
                yield isValid(ValueType.valueOf(creationReq.type()), Operation.valueOf(creationReq.operation()));
            }
            default -> false;
        };
    }

    private boolean isValidOperationValue(String operation) {
        for (Operation op : Operation.values()) {
            if (op.name().equals(operation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidValueType(String type) {
        for (ValueType valueType : ValueType.values()) {
            if (valueType.name().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValid(ValueType type, Operation operation) {
        return switch (type) {
            case AMOUNT -> switch (operation) {
                case EQUALS, LARGER_THAN, LESS_THAN -> true;
                default -> false;
            };
            case TEXT -> switch (operation) {
                case EQUALS, CONTAINS -> true;
                default -> false;
            };
            case DATE -> switch (operation) {
                case ON, TO, FROM -> true;
                default -> false;
            };
        };
    }
}
