package vn.tech.website.store.dto.payment;

import lombok.Getter;
import lombok.Setter;
import vn.tech.website.store.model.payment.ReceiptManagement;

@Getter
@Setter

public class ReceiptManagementDto extends ReceiptManagement {
    private boolean sendBillStatus;
    private String mail;
    private String time;
    private String moneyVietWords;
    private String paymentCode;
    private String provinceName;
    private String districtName;
    private String communeName;
    private Integer paymentStatus;
    private String titleBill;
    private String assetName;
    private String regulationCode;
    private String username;

    public ReceiptManagementDto() {
    }
}
