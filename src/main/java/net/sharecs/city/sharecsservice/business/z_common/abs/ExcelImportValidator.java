package net.sharecs.city.sharecsservice.business.z_common.abs;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sharecs.city.sharecsservice.business.z_common.enumeration.ErrorCode;
import net.sharecs.city.sharecsservice.business.z_common.export_excel.ExcelCasting;
import net.sharecs.city.sharecsservice.business.z_common.model.EntityErrorDetail;
import net.sharecs.city.sharecsservice.core.exception.InfoException;
import net.sharecs.city.sharecsservice.core.util.Constants;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Slf4j
public abstract class ExcelImportValidator<S, T> {

    private final List<S> sourceObjects;

    private final List<T> destinationObjects;

    private final List<EntityErrorDetail> entityErrorsDetail; // lỗi sẽ được lưu hết ở đây

    private final MultipartFile multipartFile;

    private final int headerStart;  // Dòng bắt đầu dọc

    private final Locale locale; // ngôn ngữ

    private final int lastCellNum; // Cột cuôi cùng dọc

    public ExcelImportValidator(MultipartFile file, int headerStart, Locale locale, int lastCellNum) {
        // Convert Excel to Object
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().rawData(true).trimCellValue(true).headerStart(headerStart).dateFormatter(DateTimeFormatter.ofPattern(Constants.PRINT_DATE_FORMAT)).withCasting(new ExcelCasting()).build();

        try {
            Class<S> sourceType = (Class<S>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            this.sourceObjects = Poiji.fromExcel(file.getInputStream(), PoijiExcelType.XLSX, sourceType, options);
            // Limit records import excel 2000
            if (this.sourceObjects.size() > Constants.LIMIT_DATA_IMPORT_EXCEL) {
                // Trả log lỗi tại đây. Bạn có thể extends lại class RuntimeException. hoặc sử dụng @Slf4j
                throw new InfoException(ErrorCode.LIMIT_DATA_IMPORT_EXCEL, "Number of records must be less than: 3000");
            }
        } catch (IOException e) {
            throw new InfoException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        this.entityErrorsDetail = new ArrayList<>();
        this.destinationObjects = new ArrayList<>();
        this.multipartFile = file;
        this.headerStart = headerStart;
        this.locale = locale;
        this.lastCellNum = lastCellNum;
    }

    public abstract ExcelImportValidator<S, T> validate();

    /**
     *
     * @param response
     * @return
     */
    public ExcelImportValidator<S, T> thenReturnExcelIfError(HttpServletResponse response) {
        if (!ObjectUtils.isEmpty(entityErrorsDetail)) {

            try {
                XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
                XSSFSheet sheet = workbook.getSheetAt(0);

                // remove comment
                this.removeComment(sheet);
                // map row index and message error
                Map<Integer, String> mapEntityErrorDetail = entityErrorsDetail.stream().collect(Collectors.toMap(EntityErrorDetail::getIndex, EntityErrorDetail::getErrorMessage));
                // Set comment and style in sheet
                this.setCommentStyle(workbook, sheet, mapEntityErrorDetail);

                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(multipartFile.getOriginalFilename(), StandardCharsets.UTF_8) + "_" + System.currentTimeMillis() + ".xlsx");
                ServletOutputStream out = response.getOutputStream();

                workbook.write(out);
                out.flush();
                out.close();
            } catch (IOException e) {
                throw new InfoException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when create new file excel");
            }
            throw new InfoException(ErrorCode.REQUEST_VALIDATION_ERROR, "validate error");
        }

        return this;
    }

    /**
     *
     * @return
     */
    public ExcelImportValidator<S, T> thenThrowExceptionIfError() {
        if (!ObjectUtils.isEmpty(entityErrorsDetail)) {
            throw new InfoException(ErrorCode.REQUEST_VALIDATION_ERROR, "validate error");
        }

        return this;
    }

    /**
     *
     * @return
     */
    public abstract List<T> thenMappingAndReturnDestinationObjects();

    /**
     *
     * @return
     */
    public List<EntityErrorDetail> thenReturnErrorsDetail() {
        return this.entityErrorsDetail;
    }

    /**
     *
     * @return
     */
    public ExcelImportValidator<S, T> validatePre() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        ReloadableResourceBundleMessageSource messageSource = messageSource();

        int temp = 0;

        for (S dto: this.getSourceObjects()) {
            EntityErrorDetail errorDetail = new EntityErrorDetail();
            List<String> comments = new ArrayList<>();
            temp++;

            // Pre validation
            Set<ConstraintViolation<S>> violations = validator.validate(dto);

            if (violations.size() > 0) {

                for (ConstraintViolation<S> violation: violations) {
                    comments.add(messageSource.getMessage(violation.getMessage(), null, locale));
                }

                errorDetail.setIndex(temp);
                errorDetail.setErrorMessage(String.join(", ", comments));
                entityErrorsDetail.add(errorDetail);
            }
        }

        return this;
    }

    /**
     * @param keyComments key messenger error
     * @param temp row error
     * @return
     */
    public void errorMessengers(List<String> keyComments, int temp) {
        List<String> comments = new ArrayList<>();
        ReloadableResourceBundleMessageSource messageSource = messageSource();

        for (String str: keyComments) {
            comments.add(messageSource.getMessage(str, null, locale));
        }

        String comment = String.join(", ", comments);

        if (!ObjectUtils.isEmpty(comment)) {
            EntityErrorDetail errorDetail = new EntityErrorDetail(temp, comment);
            // Khixayryr ra lỗi thì add hết vào entityErrorsDetail
            this.getEntityErrorsDetail().add(errorDetail);
        }
    }

    // Dùng để xác định ngôn ngữ lỗi cần trả ra
    private static ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
        messageSource.setCacheSeconds(0);

        return messageSource;
    }

    /**
     * @param sheet workbook
     * @return
     */
    public void removeComment(XSSFSheet sheet) {
        // Get comment exists
        Map<CellAddress, XSSFComment> oldComments = sheet.getCellComments();
        Cell cell;

        for (Map.Entry<CellAddress, XSSFComment> oldComment: oldComments.entrySet()) {
            cell = sheet.getRow(oldComment.getKey().getRow()).getCell(oldComment.getKey().getColumn());
            cell.removeCellComment();

            cell.getCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }
    }

    public void setCommentStyle(XSSFWorkbook workbook, XSSFSheet sheet, Map<Integer, String> mapEntityErrorDetail) {
        Row row; Cell cell; Comment comment;

        CellStyle cellStyle = workbook.getCellStyleAt(0);
        cellStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();

        for (Map.Entry<Integer, String> errorDetail: mapEntityErrorDetail.entrySet()) {
            int rowNumber = errorDetail.getKey() + headerStart;
            row = sheet.getRow(rowNumber);
            int lastColumn = Math.max(row.getLastCellNum(), lastCellNum);
            RichTextString errorMessenger = factory.createRichTextString(errorDetail.getValue());

            for (int i = 0; i < lastColumn; i++) {
                cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellStyle(cellStyle);

                comment = sheet.createDrawingPatriarch().createCellComment(anchor);
                comment.setString(errorMessenger);
                cell.setCellComment(comment);
            }
        }
    }

}
