package net.sharecs.city.sharecsservice.business.z_common.export_excel.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = DataFieldCheckDuplicateConstraintValidator.class)
public @interface DataFieldCheckDuplicate {
    // hàm này dùng để Check Duplicate ừng trường
    String message() default "data_field_duplicate";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
