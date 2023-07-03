package net.sharecs.city.sharecsservice.business.block_unit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.sharecs.city.sharecsservice.business.block_unit.dto.BlockUnitExcelDTO;

public class CheckFromDateToDateEmptyConstraintValidator implements ConstraintValidator<CheckFromDateToDateEmpty, BlockUnitExcelDTO> {

    @Override
    public boolean isValid(BlockUnitExcelDTO excelDTO, ConstraintValidatorContext constraintValidatorContext) {

        if (excelDTO.getFreeFromDate() != null && excelDTO.getFreeToDate() == null || excelDTO.getFreeFromDate() == null && excelDTO.getFreeToDate() != null) {
            return false;
        }

        return true;
    }

}
