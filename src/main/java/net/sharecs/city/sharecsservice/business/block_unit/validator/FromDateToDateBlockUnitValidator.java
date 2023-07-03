package net.sharecs.city.sharecsservice.business.block_unit.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FromDateToDateBlockUnitConstraintValidator.class)
public @interface FromDateToDateBlockUnitValidator {

    String message() default "block_unit_free_from_date_compare_free_to_date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
