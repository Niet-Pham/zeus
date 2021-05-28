package vn.tech.website.store.entity;

public enum EAction {
    // Action AuthFunctionController
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    SEARCH("SEARCH"),
    SYNC("SYNC"),
    DETAIL("DETAIL"),
    PIN("PIN"),                                             // đăng tải quy chế
    UNPIN("UNPIN"),                                         // gỡ bỏ quy chế
    ACTIVE_BLOCK("ACTIVE_BLOCK"),                           // khóa/mở khóa TK
    RESET_PASSWORD("RESET_PASSWORD"),
    RESET("RESET"),                                         // trạng thái ban đầu
    REFUSE_JOIN("REFUSE_JOIN"),                             // từ chối đăng ký
    ACCEPT_JOIN("ACCEPT_JOIN"),                             // phê duyệt đăng ký
    VIEW_REASON_JOIN_FAIL("VIEW_REASON_JOIN_FAIL"),         // xem lý do tạm dừng đăng ký
    UPDATE_REASON_REFUND("UPDATE_REASON_REFUND"),           // cập nhật lý do hoàn tiền
    REMOVE_ASSET("REMOVE_ASSET"),                           // huy bo tai san
    UPDATE_RESULT_FINAL("UPDATE_RESULT_FINAL"),             // cap nhat ket qua phien dau gia
    DEPOSITION("DEPOSITION"),                               // TRUAT QUYEN
    VIEW_FILE("VIEW_FILE"),                                 // xem file đính kèm
    CANCEL("CANCEL"),                                       // tạm dừng phiên/
    VIEW_REASON_CANCEL("VIEW_REASON_CANCEL"),               // xem lý do tạm dừng
    EXPORT("EXPORT"),                                       // xuất dữ liệu
    CHANGE_AUCTIONEER("CHANGE_AUCTIONEER"),                 // phân công ĐGV
    VIEW_JOINED_LIST("VIEW_JOINED_LIST"),                   // xem danh sách người đăng ký
    VIEW_REGULATION("VIEW_REGULATION"),                     // xem chi tiết quy chế
    VIEW_ASSET("VIEW_ASSET"),                               // xem danh sach tai san
    CREATE_REGULATION("CREATE_REGULATION"),                 // tạo mới quy chế
    CHANGE_STATUS("CHANGE_STATUS"),                         // thay đổi trạng thái (đăng ký bán tài sản, đơn đăng ký tham gia)
    SEND_RECEIPT("SEND_RECEIPT");                           // gửi biên lai

    private String action;

    EAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
