package net.sharecs.city.sharecsservice.business.z_common.export_excel;

import com.poiji.config.DefaultCasting;
import com.poiji.option.PoijiOptions;
import net.sharecs.city.sharecsservice.business.z_common.enumeration.ErrorCode;
import org.springframework.util.ObjectUtils;
import net.sharecs.city.sharecsservice.core.exception.InfoException;
import net.sharecs.city.sharecsservice.core.util.Constants;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExcelCasting extends DefaultCasting {

    @Override
    public Object castValue(Field field, String rawValue, int row, int col, PoijiOptions options) {
        Class<?> fieldType = field.getType();

        if (fieldType == LocalDate.class) {

            if (ObjectUtils.isEmpty(rawValue.trim())) {
                return null;
            }

            try {
                DateTimeFormatter df = DateTimeFormatter.ofPattern(Constants.PRINT_DATE_FORMAT);
                LocalDate.parse(rawValue, df);
            } catch (DateTimeParseException e) {
                int rowError = row + 1;
                int colError = col + 1;
                throw new InfoException(ErrorCode.DATE_FORMAT_INCORRECT, "Date format incorrect row: " + rowError + ", column: " + colError );
            }

        }

        return getValueObject(field, row, col, options, rawValue, fieldType);
    }

}
