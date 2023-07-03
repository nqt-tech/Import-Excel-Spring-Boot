package net.sharecs.city.sharecsservice.business.z_common.export_excel.enumeration;

public enum LastCellNum {

    METER(6),
    MATERIAL(10),
    BLOCK_UNIT(6),
    FACILITY(13),
    HEADCOUNT_CONFIG(6),
    BANK_TRANSFER_TRANSACTION(7),
    PARKING(12),
    KEY_CARD(12),
    METER_READING(4),
    RESIDENT(18),
    PAYMENT_TRANSACTION(9),
    FEE_INFO(6),
    VIRTUAL_BANK_ACCOUNT(3);

    private LastCellNum(int value) {
        this.value = value;
    }

    final int value;

    public int value() {
        return value;
    }

}
