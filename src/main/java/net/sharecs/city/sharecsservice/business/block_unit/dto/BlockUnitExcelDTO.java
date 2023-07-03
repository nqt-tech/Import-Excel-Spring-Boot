package net.sharecs.city.sharecsservice.business.block_unit.dto;

import com.poiji.annotation.ExcelCell;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sharecs.city.sharecsservice.business.block_unit.validator.CheckFromDateToDateEmpty;
import net.sharecs.city.sharecsservice.business.block_unit.validator.DataBlockUnitCheckDuplication;
import net.sharecs.city.sharecsservice.business.block_unit.validator.FromDateToDateBlockUnitValidator;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DataBlockUnitCheckDuplication
@CheckFromDateToDateEmpty
@FromDateToDateBlockUnitValidator
public class BlockUnitExcelDTO {

    public BlockUnitExcelDTO (String blockName, String name) {
        this.blockName = blockName;
        this.name = name;
    }

    @ExcelCell(0)
    @NotBlank(message = "block_name_empty")
    @Size(max = 255, message = "block_name_validation_size")
    public String blockName;

    @ExcelCell(1)
    @NotBlank(message = "block_unit_name_empty")
    @Size(max = 255, message = "block_unit_name_validation_size")
    public String name;

    @ExcelCell(2)
    @NotNull(message = "block_unit_group_empty")
    @Size(max = 200, message = "block_unit_group_validation_size")
    public String blockUnitGroup;

    @ExcelCell(3)
    public Float square = 0f;

    @ExcelCell(4)
    @NotNull(message = "block_unit_floor_empty")
    @Size(max = 20, message = "block_unit_floor_validation_size")
    public String floor;

    @ExcelCell(5)
    public Integer numberOfRoom;

    @ExcelCell(6)
    private LocalDate freeFromDate;

    @ExcelCell(7)
    private LocalDate freeToDate;

}
