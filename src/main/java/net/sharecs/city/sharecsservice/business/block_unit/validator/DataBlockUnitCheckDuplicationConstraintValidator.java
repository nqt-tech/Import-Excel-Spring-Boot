package net.sharecs.city.sharecsservice.business.block_unit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.sharecs.city.sharecsservice.business.block_unit.dto.BlockUnitExcelDTO;

import java.util.HashSet;
import java.util.Set;

public class DataBlockUnitCheckDuplicationConstraintValidator implements ConstraintValidator<DataBlockUnitCheckDuplication, BlockUnitExcelDTO> {

    Set<BlockUnitExcelDTO> checkDuplicates = new HashSet<>();

    @Override
    public boolean isValid(BlockUnitExcelDTO blockUnitExcelDTO, ConstraintValidatorContext constraintValidatorContext) {
        // Check duplication in file
        BlockUnitExcelDTO excelDTO = new BlockUnitExcelDTO(blockUnitExcelDTO.getBlockName(), blockUnitExcelDTO.getName());

        if (!checkDuplicates.add(excelDTO)) {
            return false;
        }

        return true;
    }

}
