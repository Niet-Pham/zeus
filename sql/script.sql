use k2_tech_store;
# missing notification table
### table province/district/commune ###
drop table if exists province;
create table if not exists province
(
    province_id int auto_increment primary key,
    name        nvarchar(200) null comment 'Tên tỉnh'
);

drop table if exists district;
create table if not exists district
(
    district_id int auto_increment primary key,
    province_id int           null comment 'Id tỉnh',
    name        nvarchar(200) null comment 'Tên quận/huyện',
    constraint fk_district_province
        foreign key (province_id) references province (province_id)
);

drop table if exists commune;
create table if not exists commune
(
    commune_id  int auto_increment primary key,
    province_id int           null comment 'Id tỉnh',
    district_id int           null comment 'Id quận/huyện',
    name        nvarchar(200) null comment 'Tên phường/xã',
    constraint fk_commune_district
        foreign key (district_id) references district (district_id),
    constraint fk_commune_province
        foreign key (province_id) references province (province_id)
);
### close province/district/commune ###

### table account ###
drop table if exists role;
create table if not exists role
(
    role_id    int primary key comment '0: admin / 1: quản trị - nhân viên / 2: quản trị - quản lý / 3: khách hàng',
    name       nvarchar(200) null comment 'admin/employee/manager/customer',
    status     int(1)        null comment '0: active / 1: disable',
    can_modify int(1) default 0 comment 'F(full)/A(all)/C(create)/U(update)/D(delete)/W(watch) - Cấu hình quyền thêm sửa xoá cho từng quyền trong constant'
);

drop table if exists account;
create table if not exists account
(
    account_id    int auto_increment primary key,
    user_name     nvarchar(100) null comment 'Tên đăng nhập',
    full_name     nvarchar(100) null comment 'Họ và tên',
    date_of_birth datetime      null comment 'Ngày sinh',
    gender        int(1) default 0 comment '0: Không xác định / 1: Nam / 2: Nữ',
    role_id       int           null comment 'Quyền truy cập',
    password      nvarchar(200) null comment 'Mật khẩu - mã hoá',
    salt          nvarchar(100) null comment 'Mã sha5 để mã hoá mật khẩu',
    email         nvarchar(50)  null comment 'Địa chỉ email',
    phone         char(11)      null comment 'Số điện thoại',
    address       text          null comment 'Địa chi',
    avatar_path   nvarchar(200) null comment 'Ảnh đại diện',
    status        int(1)        null comment 'Trạng thái kích hoạt tài khoản: 0: active / 1: disable',
    province_id   int           null comment 'Tỉnh id',
    district_id   int           null comment 'Quận/Huyện id',
    commune_id    int           null comment 'Phường/Xã id',
    create_date   datetime      null comment 'Ngày tạo',
    create_by     int           null comment 'Nười tạo',
    update_date   datetime      null comment 'Ngày cập nhật',
    update_by     int           null comment 'Người cập nhật',
    constraint fk_account_role
        foreign key (role_id) references role (role_id),
    constraint fk_province
        foreign key (province_id) references province (province_id),
    constraint fk_district
        foreign key (district_id) references district (district_id),
    constraint fk_commune
        foreign key (commune_id) references commune (commune_id)
);
### close account ###

### table brand ###
drop table if exists brand;
create table if not exists brand
(
    brand_id    int auto_increment primary key,
    code        nvarchar(100) null comment 'Mã thương hiệu',
    name        nvarchar(100) null comment 'Tên thương hiệu',
    status      int(1)        null comment 'Trạng thái hoạt động của thương hiệu. 0: active / 1: disable',
    create_date datetime      null comment 'Ngày tạo',
    create_by   int           null comment 'Nười tạo',
    update_date datetime      null comment 'Ngày cập nhật',
    update_by   int           null comment 'Người cập nhật'
);
### close brand ###

### table category ###
drop table if exists category;
create table if not exists category
(
    category_id int auto_increment primary key,
    code        nvarchar(100) null comment 'Mã nhãn hiệu',
    name        nvarchar(100) null comment 'Tên nhãn hiệu',
    type        nvarchar(10)  null comment 'Kiểu cate: 0 - news / 1 - products',
    status      int(1)        null comment 'Trạng thái hoạt động nhãn hiệu. 0: active / 1: disable',
    create_date datetime      null comment 'Ngày tạo',
    create_by   int           null comment 'Nười tạo',
    update_date datetime      null comment 'Ngày cập nhật',
    update_by   int           null comment 'Người cập nhật'
);
### close category ###

### table news ###
drop table if exists news;
create table if not exists news
(
    news_id       int auto_increment primary key,
    title         nvarchar(200) null comment 'Tiêu đề tin tức',
    short_content nvarchar(200) null comment 'Tóm tắt tin tức',
    content       text          null comment 'Chi tiết bài viết',
    category_id   int           null comment 'cate',
    status        int(1)        null comment 'Trạng thái hiển thị tin tức: 0: active / 1: disable',
    img_path      varchar(200),
    create_date   datetime      null comment 'Ngày tạo',
    create_by     int           null comment 'Nười tạo',
    update_date   datetime      null comment 'Ngày cập nhật',
    update_by     int           null comment 'Người cập nhật',
    constraint fk_category foreign key (category_id) references category (category_id)
);
### close news ###

### table product ###
drop table if exists product;
create table if not exists product
(
    product_id  int auto_increment primary key,
    brand_id    int           null comment 'Thương hiệu id',
    category_id int           null comment 'Nhãn hiệu id',
    name        nvarchar(200) null comment 'Tên sản phẩm',
    code        nvarchar(100) null comment 'Mã sản phẩm',
    quantity    bigint        null comment 'Số lượng sản phẩm',
    type        int(1)        null comment 'Loại sản phẩm: 0 - parent / 1 - child / 2 - none',
    description text          null comment 'Tóm tắt thông tin sản phẩm',
    price       double        null comment 'Giá sản phẩm theo từng lựa chọn',
    discount    float         null comment 'Mức giảm giá cho từng sản phẩm nhỏ',
    status      int(1)        null comment 'Trạng thái hiển thị cho sản phẩm. 0: active / 1: disable',
    create_date datetime      null comment 'Ngày tạo',
    create_by   int           null comment 'Nười tạo',
    update_date datetime      null comment 'Ngày cập nhật',
    update_by   int           null comment 'Người cập nhật',
    constraint fk_product_brand
        foreign key (brand_id) references brand (brand_id),
    constraint fk_product_category
        foreign key (category_id) references category (category_id)
);

drop table if exists product_link;
create table if not exists product_link
(
    product_link_id int auto_increment primary key,
    parent_id       int null comment 'Id sản phẩm cha',
    child_id        int null comment 'Id sản phẩm con'
);

drop table if exists product_image;
create table if not exists product_image
(
    product_image_id int auto_increment primary key,
    product_id       int           null comment 'Id sản phẩm',
    image_name       nvarchar(200) null comment 'Tên ảnh',
    image_path       nvarchar(200) null comment 'Địa chỉ ảnh',
    is_main          int(1)        null comment '0 - ảnh phụ / 1 - ảnh chính',
    create_date      datetime      null comment 'Ngày tạo',
    create_by        int           null comment 'Nười tạo',
    update_date      datetime      null comment 'Ngày cập nhật',
    update_by        int           null comment 'Người cập nhật',
    constraint fk_product_detail
        foreign key (product_id) references product (product_id)
);

drop table if exists product_option;
create table if not exists product_option
(
    product_option_id int auto_increment primary key,
    name              nvarchar(100) null comment 'Tên lựa chọn: Size, Color, Version',
    value             nvarchar(200) null comment 'Tóm tắt thông tin lựa chọn',
    type              nvarchar(100) null comment 'Loại lựa chọn size / color / version(release year)',
    status            int(1)        null comment 'Trạng thái hiển thị của lựa chọn. 0: active / 1: disable',
    create_date       datetime      null comment 'Ngày tạo',
    create_by         int           null comment 'Nười tạo',
    update_date       datetime      null comment 'Ngày cập nhật',
    update_by         int           null comment 'Người cập nhật'
);

drop table if exists product_option_detail;
create table if not exists product_option_detail
(
    product_option_detail_id int auto_increment primary key,
    product_id               int null comment 'Id sản phẩm - tất cả sản phẩm dùng chung tên và code, khác nhau ở những chi tiết trong bảng này',
    product_option_id        int null comment 'Id lựa chọn - mỗi sản phẩm có lựa chọn size, color khác nhau',
    constraint fk_product_option
        foreign key (product_option_id) references product_option (product_option_id),
    constraint fk_product
        foreign key (product_id) references product (product_id)
);
### close product ###

### table order ###
drop table if exists orders;
create table if not exists orders
(
    orders_id    int auto_increment primary key,
    account_id   int           null comment 'Id khách đặt hoá đơn',
    code         nvarchar(100) null comment 'Mã hoá đơn',
    address      text          null comment 'Địa chỉ giao hàng',
    phone        nvarchar(11)  null comment 'Số điện thoại nhận hàng',
    note         text          null comment 'Ghi chú của khách hàng',
    total_amount double(22, 0) null comment 'Tổng tiền đơn hàng',
    shipping     double(22, 0) null comment 'Phí ship cho đơn hàng',
    shipping_id  int           null,
    create_date  datetime      null comment 'Ngày tạo',
    create_by    int           null comment 'Nười tạo',
    update_date  datetime      null comment 'Ngày cập nhật',
    update_by    int           null comment 'Người cập nhật',
    constraint fk_order_account
        foreign key (account_id) references account (account_id)
);

drop table if exists order_detail;
create table if not exists order_detail
(
    order_detail_id int auto_increment primary key,
    orders_id       int      null comment 'Id hoá đơn',
    product_id      int      null comment 'Id sản phẩm - 1 hoá đơn có nhiều sản phẩm',
    quantity        bigint   null comment 'Số lượng của từng sản phẩm',
    amount          double   null comment 'Giá tiền của từng sản phẩm',
    create_date     datetime null comment 'Ngày tạo',
    create_by       int      null comment 'Nười tạo',
    update_date     datetime null comment 'Ngày cập nhật',
    update_by       int      null comment 'Người cập nhật',
    constraint fk_orders_id foreign key (orders_id) references orders (orders_id),
    constraint fk_product_id foreign key (product_id) references product (product_id)
);

drop table if exists payments;
create table if not exists payments
(
    payment_id   int auto_increment primary key,
    orders_id    int           null comment 'Id đơn đặt hàng',
    code         nvarchar(100) null comment 'Mã thanh toán',
    total_amount double(22, 0) null comment 'Tổng tiền của hoá đơn bao gồm tổng tiền sản phẩm + phí ship',
    type         int(1)        null comment 'Loại thanh toán. 0: offline / 1: online',
    status       int(1)        null comment 'Trạng thái thanh toán. 0: unpaid / 1: paid',
    create_date  datetime      null comment 'Ngày tạo',
    create_by    int           null comment 'Nười tạo',
    update_date  datetime      null comment 'Ngày cập nhật',
    update_by    int           null comment 'Người cập nhật',
    constraint fk_orders foreign key (orders_id) references orders (orders_id)
);
### close orders ###

-- product high light
create table product_highlight
(
    product_highlight_id int primary key auto_increment,
    product_id           int      null,
    date_add             datetime null,
    point                int      null
);
# insert into product_highlight(product_id, date_add, point)
# values (2, now(), 0),
#        (5, now(), 0),
#        (8, now(), 0),
#        (11, now(), 0),
#        (13, now(), 0),
#        (14, now(), 0),
#        (18, now(), 0),
#        (19, now(), 0),
#        (20, now(), 0),
#        (22, now(), 0),
#        (23, now(), 0),
#        (26, now(), 0),
#        (28, now(), 0),
#        (29, now(), 0),
#        (30, now(), 0),
#        (31, now(), 0),
#        (32, now(), 0),
#        (35, now(), 0),
#        (37, now(), 0),
#        (38, now(), 0),
#        (39, now(), 0),
#        (40, now(), 0),
#        (42, now(), 0),
#        (43, now(), 0),
#        (44, now(), 0),
#        (46, now(), 0),
#        (47, now(), 0),
#        (49, now(), 0),
#        (51, now(), 0),
#        (52, now(), 0),
#        (53, now(), 0),
#        (55, now(), 0),
#        (56, now(), 0),
#        (57, now(), 0),
#        (59, now(), 0),
#        (60, now(), 0),
#        (61, now(), 0),
#        (63, now(), 0),
#        (64, now(), 0),
#        (65, now(), 0),
#        (67, now(), 0),
#        (68, now(), 0),
#        (69, now(), 0),
#        (71, now(), 0),
#        (72, now(), 0),
#        (73, now(), 0),
#        (74, now(), 0),
#        (75, now(), 0),
#        (76, now(), 0),
#        (77, now(), 0),
#        (78, now(), 0),
#        (79, now(), 0),
#        (80, now(), 0),
#        (81, now(), 0),
#        (82, now(), 0),
#        (83, now(), 0),
#        (84, now(), 0),
#        (85, now(), 0),
#        (86, now(), 0),
#        (87, now(), 0),
#        (88, now(), 0),
#        (89, now(), 0),
#        (90, now(), 0),
#        (91, now(), 0),
#        (92, now(), 0),
#        (93, now(), 0),
#        (94, now(), 0),
#        (95, now(), 0),
#        (96, now(), 0),
#        (97, now(), 0),
#        (98, now(), 0),
#        (99, now(), 0),
#        (100, now(), 0),
#        (101, now(), 0),
#        (102, now(), 0),
#        (103, now(), 0),
#        (104, now(), 0),
#        (105, now(), 0),
#        (106, now(), 0),
#        (107, now(), 0),
#        (108, now(), 0),
#        (109, now(), 0),
#        (110, now(), 0),
#        (111, now(), 0),
#        (113, now(), 0),
#        (114, now(), 0),
#        (115, now(), 0),
#        (116, now(), 0),
#        (117, now(), 0),
#        (118, now(), 0),
#        (119, now(), 0),
#        (120, now(), 0);

-- auto-generated definition
create table receive_notification
(
    receive_notification_id int auto_increment
        primary key,
    account_id              int      null,
    send_notification_id    int      null,
    status                  int(1)   null,
    status_bell             int(1)   null,
    update_by               int      null,
    update_date             datetime null,
    create_by               int      null,
    create_date             datetime null
);
create table send_notification
(
    send_notification_id int auto_increment
        primary key,
    account_id           int          null,
    content              varchar(200) null,
    status               int(1)       null,
    update_by            int          null,
    update_date          datetime     null,
    create_by            int          null,
    create_date          datetime     null
);
create table shipping
(
    shipping_id int auto_increment,
    name        nvarchar(200) null,
    code        varchar(100)  null,
    price       int           null,
    detail      text          null,
    path        varchar(200)  null,
    status      int(1)        null,
    constraint shipping_pk
        primary key (shipping_id)
);
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Alo alo admin on the mic. Mic check mic check</i>' where send_notification_id = 1;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Parker dep chai</i>' where send_notification_id = 1;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Tien oc cho</i>' where send_notification_id = 2;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Testing ...</i>' where send_notification_id = 3;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Dit cu thang tien ngu</i>' where send_notification_id = 4;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Lai la Parker dep chai</i>' where send_notification_id = 5;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Check it check it</i>' where send_notification_id = 6;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Abc ...</i>' where send_notification_id = 7;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Xin vai lon .</i>' where send_notification_id = 8;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Dinh thuc su !!!</i>' where send_notification_id = 9;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Alo alo admin on the mic. Mic check mic check</i>' where send_notification_id = 10;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Parker dep chai</i>' where send_notification_id = 11;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Tien oc cho</i>' where send_notification_id = 12;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Testing ...</i>' where send_notification_id = 13;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Dit cu thang tien ngu</i>' where send_notification_id = 14;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Lai la Parker dep chai</i>' where send_notification_id = 15;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Check it check it</i>' where send_notification_id = 16;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Abc ...</i>' where send_notification_id = 17;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Xin vai lon.</i>' where send_notification_id = 18;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Dinh thuc su !!!</i>' where send_notification_id = 19;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Lai la Parker dep chai</i>' where send_notification_id = 20;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Check it check it</i>' where send_notification_id = 21;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Abc ...</i>' where send_notification_id = 22;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Xin vai lon.</i>' where send_notification_id = 23;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Dinh thuc su !!!</i>' where send_notification_id = 24;
# update send_notification set content = 'Bạn có thông báo mới từ <b>Admin</b> với nội dung: <i>Dinh thuc su !!!</i>' where send_notification_id = 25;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 26;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 27;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 28;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 29;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 30;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 31;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 32;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 33;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 34;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 35;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 36;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 37;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 38;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 39;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 40;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 41;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 42;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 43;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 44;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 45;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 46;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 47;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 48;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 49;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000018</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 50;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000019</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 51;
# update send_notification set content = 'Bạn nhận được một đơn hàng mới từ khách hàng <b>Vũ Hùng</b> với mã đơn hàng <b>DH000020</b>. <i>Kiểm tra ngay !!!</i>' where send_notification_id = 52;

ALTER TABLE k2_tech_store.account MODIFY COLUMN address text
    CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
ALTER TABLE k2_tech_store.news MODIFY COLUMN content text
    CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
ALTER TABLE k2_tech_store.product MODIFY COLUMN description text
    CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
ALTER TABLE k2_tech_store.orders MODIFY COLUMN address text
    CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
ALTER TABLE k2_tech_store.orders MODIFY COLUMN note text
    CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
ALTER TABLE k2_tech_store.shipping MODIFY COLUMN detail text
    CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
ALTER TABLE k2_tech_store.send_notification MODIFY COLUMN content text
    CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;


select distinct
    (select sum(p_1.total_amount) as total from payments p_1 where month(p_1.create_date) = 1 and year(p_1.create_date) = year(p_df.create_date)) as January,
    (select sum(p_2.total_amount) as total from payments p_2 where month(p_2.create_date) = 2 and year(p_2.create_date) = year(p_df.create_date)) as February,
    (select sum(p_3.total_amount) as total from payments p_3 where month(p_3.create_date) = 3 and year(p_3.create_date) = year(p_df.create_date)) as March,
    (select sum(p_4.total_amount) as total from payments p_4 where month(p_4.create_date) = 4 and year(p_4.create_date) = year(p_df.create_date)) as April,
    (select sum(p_5.total_amount) as total from payments p_5 where month(p_5.create_date) = 5 and year(p_5.create_date) = year(p_df.create_date)) as May,
    (select sum(p_6.total_amount) as total from payments p_6 where month(p_6.create_date) = 6 and year(p_6.create_date) = year(p_df.create_date)) as June,
    (select sum(p_7.total_amount) as total from payments p_7 where month(p_7.create_date) = 7 and year(p_7.create_date) = year(p_df.create_date)) as July,
    (select sum(p_8.total_amount) as total from payments p_8 where month(p_8.create_date) = 8 and year(p_8.create_date) = year(p_df.create_date)) as August,
    (select sum(p_9.total_amount) as total from payments p_9 where month(p_9.create_date) = 9 and year(p_9.create_date) = year(p_df.create_date)) as September,
    (select sum(p_10.total_amount) as total from payments p_10 where month(p_10.create_date) = 10 and year(p_10.create_date) = year(p_df.create_date)) as October,
    (select sum(p_11.total_amount) as total from payments p_11 where month(p_11.create_date) = 11 and year(p_11.create_date) = year(p_df.create_date)) as November,
    (select sum(p_12.total_amount) as total from payments p_12 where month(p_12.create_date) = 12 and year(p_12.create_date) = year(p_df.create_date)) as December
from payments p_df
where year(p_df.create_date) = year(now())

insert into k2_tech_store.account (user_name, full_name, date_of_birth, gender, role_id, password, salt, email, phone,
                                   address, avatar_path, status, province_id, district_id, commune_id, verify_code,
                                   create_date, create_by, update_date, update_by)
select concat(lower(c._address_firstname), '_', lower(c._address_lastname)) as username,
       concat(c._address_firstname, ' ', c._address_lastname)               as fullname,
       now()                                                                as date_of_birth,
       1                                                                    as gender,
       3                                                                    as role,
       '29ca7b2b1990d7029dfbb504494a8380839d5d4e4d68eb1b7b8b2e19bdccfd81'   as password,
       'ZK9lZFhfpa'                                                         as salt,
       concat('parker', '.', c.email)                                       as email,
       substr(replace(c._address_telephone, '-', ''), 1, 11)                as phone,
       c._address_street                                                    as address,
       '/upload/20210424/24042021012454776.jpg'                             as avapath,
       1                                                                    as status,
       1                                                                    as province,
       1                                                                    as district,
       1                                                                    as commune,
       '123456a@'                                                           as verify,
       now()                                                                as create_date,
       3                                                                    as create_by,
       now()                                                                as update_date,
       3                                                                    as update_by
from csv_to_sql_parker.catalog_customer c
where c._address_telephone <> ''