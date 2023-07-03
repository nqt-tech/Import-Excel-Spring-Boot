package net.sharecs.city.sharecsservice.business.block_unit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.sharecs.city.sharecsservice.business.block_unit.dto.BlockUnitExcelDTO;
import org.springframework.util.ObjectUtils;

public class FromDateToDateBlockUnitConstraintValidator implements ConstraintValidator<FromDateToDateBlockUnitValidator, BlockUnitExcelDTO> {

    @Override
    public boolean isValid(BlockUnitExcelDTO excelDTO, ConstraintValidatorContext constraintValidatorContext) {

        if (!ObjectUtils.isEmpty(excelDTO.getFreeFromDate()) && !ObjectUtils.isEmpty(excelDTO.getFreeToDate()) && excelDTO.getFreeFromDate().isAfter(excelDTO.getFreeToDate())) {
            return false;
        }

        return true;
    }

}
