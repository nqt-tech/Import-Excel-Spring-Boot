package net.sharecs.city.sharecsservice.business.block_unit;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sharecs.city.sharecsservice.business.z_common.enumeration.ErrorCode;
import net.sharecs.city.sharecsservice.core.exception.InfoException;
import net.sharecs.city.sharecsservice.core.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@RestController
@RequestMapping("/blockUnit")
@RequiredArgsConstructor
@Validated
public class BlockUnitController {

    private final BlockUnitService blockUnitService;

    @PreAuthorize("hasAuthority('CREATE_BLOCK_UNIT')")
    @PostMapping(value = "/createFromExcel", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createFromExcel(@RequestPart("file") MultipartFile file, HttpServletResponse response, Locale locale) {

        if (!Constants.FILE_CONTENT_TYPE_ALLOWED.contains(file.getContentType())) {
            throw new InfoException(ErrorCode.FILE_CONTENT_TYPE_NOT_SUPPORTED, "File content type not supported");
        }

        blockUnitService.createFromExcel(file, response, locale);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
