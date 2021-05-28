create table brand
(
    brand_id    int auto_increment
        primary key,
    code        varchar(100) null comment 'Mã thương hiệu',
    name        varchar(100) null comment 'Tên thương hiệu',
    status      int(1)       null comment 'Trạng thái hoạt động của thương hiệu. 1: active / 0: disable',
    create_date datetime     null comment 'Ngày tạo',
    create_by   int          null comment 'Nười tạo',
    update_date datetime     null comment 'Ngày cập nhật',
    update_by   int          null comment 'Người cập nhật'
);

create table category
(
    category_id int auto_increment
        primary key,
    code        varchar(100) null comment 'Mã nhãn hiệu',
    name        varchar(100) null comment 'Tên nhãn hiệu',
    status      int(1)       null comment 'Trạng thái hoạt động nhãn hiệu. 1: active / 0: disable',
    create_date datetime     null comment 'Ngày tạo',
    create_by   int          null comment 'Nười tạo',
    update_date datetime     null comment 'Ngày cập nhật',
    update_by   int          null comment 'Người cập nhật'
);

create table news
(
    news_id       int auto_increment
        primary key,
    title         varchar(200) null comment 'Tiêu đề tin tức',
    short_content varchar(200) null comment 'Tóm tắt tin tức',
    content       text         null comment 'Chi tiết bài viết',
    type          int(1)       null comment 'Loại tin tức: 0: category / 1: product / 3: technology',
    status        int(1)       null comment 'Trạng thái hiển thị tin tức: 0: active / 1: disable',
    create_date   datetime     null comment 'Ngày tạo',
    create_by     int          null comment 'Nười tạo',
    update_date   datetime     null comment 'Ngày cập nhật',
    update_by     int          null comment 'Người cập nhật'
);

create table product
(
    product_id  int auto_increment
        primary key,
    brand_id    int          null comment 'Thương hiệu id',
    category_id int          null comment 'Nhãn hiệu id',
    type        int(1)       null comment '0:sp cha
1:sp con
3:none',
    name        varchar(200) null comment 'Tên sản phẩm',
    code        varchar(100) null comment 'Mã sản phẩm',
    quantity    int          null comment 'số lượng',
    status      int(1)       null comment 'Trạng thái hiển thị cho sản phẩm. 1: active / 0: disable',
    create_date datetime     null comment 'Ngày tạo',
    create_by   int          null comment 'Nười tạo',
    update_date datetime     null comment 'Ngày cập nhật',
    update_by   int          null comment 'Người cập nhật',
    constraint fk_product_brand
        foreign key (brand_id) references brand (brand_id),
    constraint fk_product_category
        foreign key (category_id) references category (category_id)
);

create table product_detail
(
    product_detail_id int auto_increment
        primary key,
    product_id        int      null comment 'Id sản phẩm - tất cả sản phẩm dùng chung tên và code, khác nhau ở những chi tiết trong bảng này',
    description       text     null comment 'Mô tả chi tiết cho sản phẩm với lựa chọn riêng',
    price             double   null comment 'Giá sản phẩm theo từng lựac chọn',
    discount          float    null comment 'Mức giảm giá cho từng sản phẩm nhỏ',
    create_date       datetime null comment 'Ngày tạo',
    create_by         int      null comment 'Nười tạo',
    update_date       datetime null comment 'Ngày cập nhật',
    update_by         int      null comment 'Người cập nhật',
    constraint fk_product
        foreign key (product_id) references product (product_id)
);

create table product_detail_image
(
    product_detail_image_id int auto_increment
        primary key,
    product_detail_id       int          null comment 'Id sản phẩm chi tiết -Mỗi sản phẩm nhỏ có nhiều ảnh khách nhau',
    image_name              varchar(200) null comment 'Tên ảnh',
    image_path              varchar(200) null comment 'Địa chỉ ảnh',
    create_date             datetime     null comment 'Ngày tạo',
    create_by               int          null comment 'Nười tạo',
    update_date             datetime     null comment 'Ngày cập nhật',
    update_by               int          null comment 'Người cập nhật',
    constraint fk_product_detail
        foreign key (product_detail_id) references product_detail (product_detail_id)
);

create table product_image
(
    product_image_id int auto_increment
        primary key,
    product_id       int          null,
    image_path       varchar(200) null,
    type             int(1)       null comment '0:
1: ảnh chính',
    create_date      datetime     null,
    create_by        int          null,
    update_date      datetime     null,
    update_by        int          null
)
    comment 'ảnh sản phẩm';

create table product_link
(
    product_link_id int auto_increment
        primary key,
    parent_id       int null,
    child_id        int null
);

create table product_option
(
    product_option_id int auto_increment
        primary key,
    type              int(1)           null comment 'Loại lựa chọn 0: size / 1: color / 2: version(release year)',
    name              varchar(100)     null comment 'Tên lựa chọn: Size, Color, Version',
    value             varchar(100)     null comment 'Tóm tắt thông tin lựa chọn',
    status            int(1) default 1 null comment 'Trạng thái hiển thị của lựa chọn. 1: active / 0: disable',
    create_date       datetime         null comment 'Ngày tạo',
    create_by         int              null comment 'Nười tạo',
    update_date       datetime         null comment 'Ngày cập nhật',
    update_by         int              null comment 'Người cập nhật'
);

create table product_option_detail
(
    product_option_detail_id int auto_increment
        primary key,
    product_id               int null,
    product_option_id        int null
);

create table province
(
    province_id int auto_increment
        primary key,
    name        varchar(200) null comment 'Tên tỉnh'
);

create table district
(
    district_id int auto_increment
        primary key,
    province_id int          null comment 'Id tỉnh',
    name        varchar(200) null comment 'Tên quận/huyện',
    constraint fk_district_province
        foreign key (province_id) references province (province_id)
);

create table commune
(
    commune_id  int auto_increment
        primary key,
    province_id int          null comment 'Id tỉnh',
    district_id int          null comment 'Id quận/huyện',
    name        varchar(200) null comment 'Tên phường/xã',
    constraint fk_commune_district
        foreign key (district_id) references district (district_id),
    constraint fk_commune_province
        foreign key (province_id) references province (province_id)
);

create table role
(
    role_id    int              not null comment '0: admin / 1: quản trị - nhân viên / 2: quản trị - quản lý / 3: khách hàng'
        primary key,
    name       varchar(200)     null comment 'admin/employee/manager/customer',
    status     int(1)           null comment '0: active / 1: disable',
    can_modify int(1) default 0 null comment 'A(all)/C(create)/U(update)/D(delete)/W(watch) - Cấu hình quyền thêm sửa xoá cho từng quyền trong constant'
);

create table account
(
    account_id    int auto_increment
        primary key,
    user_name     varchar(100)     null comment 'Tên đăng nhập',
    full_name     varchar(100)     null comment 'Họ và tên',
    date_of_birth date             null comment 'Ngày sinh',
    gender        int(1) default 2 null comment '0: Nam / 1: Nữ / 2: Không xác định',
    role_id       int              null comment 'Quyền truy cập',
    password      varchar(200)     null comment 'Mật khẩu - mã hoá',
    salt          varchar(100)     null comment 'Mã sha5 để mã hoá mật khẩu',
    email         varchar(50)      null comment 'Địa chỉ email',
    phone         char(11)         null comment 'Số điện thoại',
    address       text             null comment 'Địa chi',
    avatar_path   varchar(200)     null comment 'Ảnh đại diện',
    verify_code   varchar(20)      null,
    status        int(1)           null comment 'Trạng thái kích hoạt tài khoản: 1: active / 0: disable',
    province_id   int              null comment 'Tỉnh id',
    district_id   int              null comment 'Quận/Huyện id',
    commune_id    int              null comment 'Phường/Xã id',
    create_date   datetime         null comment 'Ngày tạo',
    create_by     int              null comment 'Nười tạo',
    update_date   datetime         null comment 'Ngày cập nhật',
    update_by     int              null comment 'Người cập nhật',
    constraint fk_account_role
        foreign key (role_id) references role (role_id),
    constraint fk_commune
        foreign key (commune_id) references commune (commune_id),
    constraint fk_district
        foreign key (district_id) references district (district_id),
    constraint fk_province
        foreign key (province_id) references province (province_id)
);

create table orders
(
    orders_id    int auto_increment
        primary key,
    account_id   int           null comment 'Id khách đặt hoá đơn',
    code         varchar(100)  null comment 'Mã hoá đơn',
    address      text          null comment 'Địa chỉ giao hàng',
    phone        varchar(11)   null comment 'Số điện thoại nhận hàng',
    note         varchar(200)  null comment 'Ghi chú của khách hàng',
    total_amount double(22, 0) null comment 'Tổng tiền đơn hàng',
    shipping     double(22, 0) null comment 'Phí ship cho đơn hàng',
    create_date  datetime      null comment 'Ngày tạo',
    create_by    int           null comment 'Nười tạo',
    update_date  datetime      null comment 'Ngày cập nhật',
    update_by    int           null comment 'Người cập nhật',
    constraint fk_order_account
        foreign key (account_id) references account (account_id)
);

create table order_detail
(
    order_detail_id int auto_increment
        primary key,
    order_id        int      null comment 'Id hoá đơn',
    product_id      int      null comment 'Id sản phẩm - 1 hoá đơn có nhiều sản phẩm',
    quantity        int(2)   null comment 'Số lượng của từng sản phẩm',
    amount          double   null comment 'Giá tiền của từng sản phẩm',
    create_date     datetime null comment 'Ngày tạo',
    create_by       int      null comment 'Nười tạo',
    update_date     datetime null comment 'Ngày cập nhật',
    update_by       int      null comment 'Người cập nhật',
    constraint fk_orders_id
        foreign key (order_id) references orders (orders_id),
    constraint fk_product_id
        foreign key (product_id) references product (product_id)
);

create table payments
(
    payment_id   int auto_increment
        primary key,
    orders_id    int           null comment 'Id đơn đặt hàng',
    code         varchar(100)  null comment 'Mã thanh toán',
    total_amount double(22, 0) null comment 'Tổng tiền của hoá đơn bao gồm tổng tiền sản phẩm + phí ship',
    type         int(1)        null comment 'Loại thanh toán. 0: offline / 1: online',
    status       int(1)        null comment 'Trạng thái thanh toán. 0: unpaid / 1: paid',
    create_date  datetime      null comment 'Ngày tạo',
    create_by    int           null comment 'Nười tạo',
    update_date  datetime      null comment 'Ngày cập nhật',
    update_by    int           null comment 'Người cập nhật',
    constraint fk_orders
        foreign key (orders_id) references orders (orders_id)
);


