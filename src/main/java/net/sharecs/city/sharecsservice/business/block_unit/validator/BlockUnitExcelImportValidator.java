package net.sharecs.city.sharecsservice.business.block_unit.validator;

import net.sharecs.city.sharecsservice.business.block.Block;
import net.sharecs.city.sharecsservice.business.block.BlockService;
import net.sharecs.city.sharecsservice.business.block_unit.BlockUnit;
import net.sharecs.city.sharecsservice.business.block_unit.BlockUnitRepository;
import net.sharecs.city.sharecsservice.business.block_unit.dto.BlockUnitExcelDTO;
import net.sharecs.city.sharecsservice.business.z_common.export_excel.enumeration.LastCellNum;
import org.modelmapper.ModelMapper;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import net.sharecs.city.sharecsservice.business.z_common.abs.ExcelImportValidator;
import net.sharecs.city.sharecsservice.business.z_common.model.map_composite_key.BlockUnitBlockKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlockUnitExcelImportValidator extends ExcelImportValidator<BlockUnitExcelDTO, BlockUnit> {

    private final ModelMapper mapper;

    private final BlockUnitRepository blockUnitRepository;

    private final BlockService blockService;

    private Map<String, String> mapBlockIdsByName;

    private Map<BlockUnitBlockKey, BlockUnit> mapBlockIdAndBlockUnit;

    private List<BlockUnit> blockUnits;

    public BlockUnitExcelImportValidator(MultipartFile file, int headerStart, Locale locale, ModelMapper mapper,
                                         BlockUnitRepository blockUnitRepository, BlockService blockService) {
        super(file, headerStart, locale, LastCellNum.BLOCK_UNIT.value());
        this.mapper = mapper;
        this.blockUnitRepository = blockUnitRepository;
        this.blockService = blockService;
    }

    @Override
    public ExcelImportValidator<BlockUnitExcelDTO, BlockUnit> validate() {
        // Convert data thành list
        List<BlockUnitExcelDTO> blockUnitExcelDTOS = this.getSourceObjects();

        // Lấy dữ liệu ra để check
        Set<String> blockNames = blockUnitExcelDTOS.stream().map(BlockUnitExcelDTO::getBlockName).collect(Collectors.toSet());
        Set<String> blockUnitNames = blockUnitExcelDTOS.stream().map(BlockUnitExcelDTO::getName).collect(Collectors.toSet());

        // Thực hiên query 1 loạt, sắm trước để tý chỉ việc dùng để check trong for. Tuyệt đối không được viêt query trong for
        mapBlockIdsByName = blockService.findBlockIdsByNames(blockNames);
        blockUnits = blockUnitRepository.findBlockUnitByNameIn(blockUnitNames);
        mapBlockIdAndBlockUnit = blockUnits.stream().collect(Collectors.toMap(b -> new BlockUnitBlockKey(b.getBlock().getName(), b.getName()), Function.identity()));
        int temp = 0;

        // thực hiện for validate lỗi ở đây, validate ở đây thường là validate logic giữa nhiều bảng
        for (BlockUnitExcelDTO dto: this.getSourceObjects()) {
            List<String> keyComments = new ArrayList<>();

            String blockId = mapBlockIdsByName.get(dto.getBlockName());
            BlockUnitBlockKey blockUnitBlockKey = new BlockUnitBlockKey(dto.getBlockName(), dto.getName());
            BlockUnit blockUnit = mapBlockIdAndBlockUnit.get(blockUnitBlockKey);

            if (ObjectUtils.isEmpty(blockId)) {
                keyComments.add("block_no_exist");
            } else {

                if (!ObjectUtils.isEmpty(blockUnit)) {
                    keyComments.add("block_unit_exist");
                }

            }

            // xac dinh từng dòng có lỗi vaf vị trí dòng lỗi
            temp++;
            this.errorMessengers(keyComments, temp);
        }

        return this;
    }

    @Override
    public List<BlockUnit> thenMappingAndReturnDestinationObjects() {
        // mapping sourceObjects to destinationObjects;
        List<BlockUnit> blockUnits = new ArrayList<>();

        for (BlockUnitExcelDTO dto: this.getSourceObjects()) {
            BlockUnit blockUnit = new BlockUnit();
            mapper.map(dto, blockUnit);
            blockUnit.setBlock(new Block(mapBlockIdsByName.get(dto.getBlockName())));

            blockUnits.add(blockUnit);
        }

        return blockUnits;
    }

}
