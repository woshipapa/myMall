package com.papa.design;

public enum OmsOrderReturnApplyStatus {

    PENDING(0, "待处理"),
    RETURNING(1, "退货中"),
    COMPLETED(2, "已完成"),
    REJECTED(3, "已拒绝");

    private final int value;
    private final String description;

    OmsOrderReturnApplyStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据value获取枚举类型
     * @param value 数值
     * @return 枚举类型
     */
    public static OmsOrderReturnApplyStatus valueOf(int value) {
        for (OmsOrderReturnApplyStatus status : OmsOrderReturnApplyStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的ApplicationStatus值: " + value);
    }
}
