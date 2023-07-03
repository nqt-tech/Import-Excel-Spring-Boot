package net.sharecs.city.sharecsservice.core.util;

import java.util.Arrays;
import java.util.List;

/*
 * @Author: Nguyen Tuan - Sharecs.net 
 */

public class Constants {

    private Constants() {
        
    }

    public static final List<String> FILE_CONTENT_TYPE_ALLOWED = Arrays.asList("image/png", "image/jpeg", "image/gif", "image/jpg", "image/webp",
                        "application/vnd.ms-excel", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/pdf", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "text/plain");

    // Config header start excel
    public static final int BLOCK_UNIT_HEADER_START = 9;

    public static final String PRINT_DATE_FORMAT = "dd.MM.yyyy";
    public static final String PRINT_MONTH_YEAR_FORMAT = "MM.yyyy";

    public static final Integer LIMIT_DATA_IMPORT_EXCEL = 3000;

}
