package me.krsmll.filtertask.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ValidFilterCriteriaCombinationValidator.class})
public @interface ValidFilterCriteriaCombination {
    String message() default "Invalid filter criteria combination";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
