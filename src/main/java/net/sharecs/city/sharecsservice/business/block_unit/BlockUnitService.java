package net.sharecs.city.sharecsservice.business.block_unit;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sharecs.city.sharecsservice.business.block.Block;
import net.sharecs.city.sharecsservice.business.block.BlockRepository;
import net.sharecs.city.sharecsservice.business.block.BlockService;
import net.sharecs.city.sharecsservice.business.block_unit.dto.BlockUnitExcelDTO;
import net.sharecs.city.sharecsservice.business.block_unit.validator.BlockUnitExcelImportValidator;
import net.sharecs.city.sharecsservice.core.util.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import net.sharecs.city.sharecsservice.business.z_common.abs.ExcelImportValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class BlockUnitService {

    private final ModelMapper mapper;

    private final BlockUnitRepository blockUnitRepository;

    private final BlockRepository blockRepository;

    private final BlockService blockService;


    /**
     * @param file
     * @param response
     * @param locale
     */
    public void createFromExcel(MultipartFile file, HttpServletResponse response, Locale locale) {
        ExcelImportValidator<BlockUnitExcelDTO, BlockUnit> blockUnitValidator = new BlockUnitExcelImportValidator(file, Constants.BLOCK_UNIT_HEADER_START, locale, mapper, blockUnitRepository, blockService);
        blockUnitValidator.validatePre().thenReturnExcelIfError(response);
        List<BlockUnit> blockUnits = blockUnitValidator.validate().thenReturnExcelIfError(response).thenMappingAndReturnDestinationObjects();

        // Save data
        blockUnits = blockUnitRepository.saveAll(blockUnits);
        Set<String> blockIds = new HashSet<>();

        for (BlockUnit blockUnit : blockUnits) {
            blockIds.add(blockUnit.getBlock().getId());
        }

        List<Block> blocks = blockRepository.findAllById(blockIds);

        for (Block block : blocks) {
            Long countTotalApartment = blockUnits.stream().filter(c -> c.getBlock().getId().equals(block.getId())).count();
            block.setTotalApartment((int) (block.getTotalApartment() + countTotalApartment));
        }

        blockRepository.saveAll(blocks);
    }


}
