package net.sharecs.city.sharecsservice.business.z_common.enumeration;

public enum ErrorCode {

    UNKNOWN(-1),
    FILE_CONTENT_TYPE_NOT_SUPPORTED(1),
    REQUEST_VALIDATION_ERROR(2),
    EXISTED_FEE_INFO_AFTER_MONTH(3),
    LIMIT_DATA_IMPORT_EXCEL(4),
    DATE_FORMAT_INCORRECT(5),
    ;

    private ErrorCode(int value) {
        this.value = value;
    }

    int value;

    public int value() {
        return value;
    }

}
