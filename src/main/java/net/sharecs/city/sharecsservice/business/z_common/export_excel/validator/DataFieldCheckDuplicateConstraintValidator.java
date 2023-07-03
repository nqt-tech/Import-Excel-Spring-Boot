package net.sharecs.city.sharecsservice.business.z_common.export_excel.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

public class DataFieldCheckDuplicateConstraintValidator implements ConstraintValidator<DataFieldCheckDuplicate, String> {

    private Set<String> stringSet = new HashSet<>();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!ObjectUtils.isEmpty(value) && !stringSet.add(value)) {
            return false;
        }

        return true;
    }

}
