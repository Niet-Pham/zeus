package vn.tech.website.store.controller.frontend.orders;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.controller.frontend.common.PaginationController;
import vn.tech.website.store.controller.frontend.product.ProductDetailFEController;
import vn.tech.website.store.dto.OrdersDetailDto;
import vn.tech.website.store.dto.OrdersDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.model.*;
import vn.tech.website.store.repository.*;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;
import vn.tech.website.store.util.StringUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Named
@Scope(value = "session")
@Getter
@Setter
public class OrderFEController extends BaseFEController {
    @Inject
    private HttpServletRequest request;
    @Inject
    private ProductDetailFEController productDetailFEController;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SendNotificationRepository sendNotificationRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductHighLightRepository productHighLightRepository;
    @Autowired
    private ShippingRepository shippingRepository;

    private PaginationController<OrdersDto> pagination;
    private Map<String, OrdersDetailDto> productCartMap;
    private List<Shipping> shippingList;
    private List<SelectItem> shippingItem;
    private Map<Long, Shipping> shippingMap;
    private AccountDto accountDto;
    private OrdersDto ordersDto;

    private boolean info;
    private boolean checkShipping;
    private String pathShipping;
    private String infoShipping;
    private Long totalQty = 0L;
    private Double totalMoney = 0D;
    private HttpSession session;

    public OrderFEController() {
        pagination = new PaginationController<>();
    }

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        info = false;
        totalQty = 0L;
        totalMoney = 0D;
        accountDto = new AccountDto();
        ordersDto = new OrdersDto();
        ordersDto.setShippingId(-1L);

        checkShipping = false;
        shippingList = shippingRepository.findAllShipping();
        shippingItem = new ArrayList<>();
        shippingMap = new HashMap<>();
        for (Shipping s : shippingList) {
            shippingItem.add(new SelectItem(s.getShippingId(), s.getName()));
            shippingMap.put(s.getShippingId(), s);
        }

        session = request.getSession(false);
        if (session.getAttribute("cartList") == null) {
            productCartMap = new LinkedHashMap<>();
        } else {
            productCartMap = new LinkedHashMap<>(productDetailFEController.getMapAddToCart());
            for (OrdersDetailDto dto : productCartMap.values()) {
                totalQty += dto.getQuantity();
                totalMoney += dto.getAmount() * dto.getQuantity();
            }
        }
    }

    public void resetAllOrderInfo() {
        info = true;

        AccountDto accDto = getAuthorizationFEController().getAccountDto();
        // process account info
        accountDto = accDto == null ? new AccountDto() : accDto;

        // set value for order
        ordersDto = new OrdersDto();

        Long countCode = orderRepository.findMaxCountCode() != null ? orderRepository.findMaxCountCode() : 0;
        ordersDto.setCode(StringUtil.createCode(null, Constant.ACRONYM_ORDER, countCode));
        ordersDto.setCountCode(StringUtil.createCountCode(ordersDto.getCode(), Constant.ACRONYM_ORDER));
        ordersDto.setTotalAmount(totalMoney);
        ordersDto.setStatus(DbConstant.ORDER_STATUS_NOT_APPROVED);

        if (accountDto != null) {
            ordersDto.setAccountId(accountDto.getAccountId());
            String address = (accountDto.getAddress() == null ? "" : accountDto.getAddress() + ", ")
                    + (accountDto.getCommuneName() == null ? "" : accountDto.getCommuneName() + ", ")
                    + (accountDto.getDistrictName() == null ? "" : accountDto.getDistrictName() + ", ")
                    + (accountDto.getProvinceName() == null ? "" : accountDto.getProvinceName());
            ordersDto.setAddress(address);
            ordersDto.setPhone(accountDto.getPhone());
            ordersDto.setEmail(accountDto.getEmail());
            ordersDto.setCustomerName(accountDto.getFullName());
        }
    }

    public void removeProduct(OrdersDetailDto dto) {
        totalQty -= dto.getQuantity();
        totalMoney -= dto.getAmount() * dto.getQuantity();
        ordersDto.setTotalAmount(totalMoney);

        if (productCartMap.size() == 1) {
            session.removeAttribute("cartList");
            resetAll();
            facesNotice().reload();
            return;
        }

        productCartMap.remove(dto.getCodeProduct());
        productDetailFEController.getMapAddToCart().remove(dto.getCodeProduct());
        session.setAttribute("cartList", productDetailFEController.getMapAddToCart());
        setSuccessForm("Xoá sản phẩm \"" + dto.getProductDto().getProductName() + "\" khỏi giỏ hàng thành công.");
        FacesUtil.updateView("orderList");
    }

    public void confirmInfo() {
        setSuccessForm("Mua hàng thành công, vui lòng xác thực thông tin của bạn.");
        resetAllOrderInfo();
        FacesUtil.updateView("orderList");
    }

    public boolean validateDataFE() {
        if (StringUtils.isBlank(ordersDto.getCustomerName())) {
            setErrorForm("Bạn vui lòng nhập tên họ tên");
            return false;
        }
        if (StringUtils.isBlank(ordersDto.getPhone())) {
            setErrorForm("Bạn vui lòng nhập số điện thoại");
            return false;
        }
        if (!ordersDto.getPhone().matches("^0[1-9]{1}[0-9]{8,9}$|")) {
            FacesUtil.addErrorMessage("Số điện thoại không đúng định dạng");
            return false;
        }
        if (StringUtils.isBlank(ordersDto.getEmail())) {
            setErrorForm("Bạn vui lòng nhập email");
            return false;
        }
        if (!ordersDto.getEmail().matches("^\\s*[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\\s*$")) {
            FacesUtil.addErrorMessage("Email không đúng định dạng");
            return false;
        }
        if (StringUtils.isBlank(ordersDto.getAddress())) {
            setErrorForm("Bạn vui lòng nhập địa chỉ nhận hàng");
            return false;
        }
        if (ordersDto.getShipping() == null) {
            setErrorForm("Bạn vui chọn đơn vị giao hàng");
            return false;
        }
        return true;
    }

    public void onSaveOrderFE() {
        if (!validateDataFE()) {
            return;
        }
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersDto, orders);
        orders.setStatus(DbConstant.ORDER_STATUS_NOT_APPROVED);
        orders.setCreateDate(new Date());
        orders.setUpdateDate(new Date());
        if (accountDto != null) {
            orders.setCreateBy(accountDto.getAccountId());
            orders.setUpdateBy(accountDto.getAccountId());
        }
        Orders ordersSave = orderRepository.save(orders);
        for (OrdersDetailDto dto : productCartMap.values()) {
            OrdersDetail ordersDetail = new OrdersDetail();
            BeanUtils.copyProperties(dto, ordersDetail);
            ordersDetail.setOrdersId(ordersSave.getOrdersId());
            orderDetailRepository.save(ordersDetail);

            // change quality product in stock
            Product product = new Product();
            BeanUtils.copyProperties(dto.getProductDto(), product);
            product.setQuantity(product.getQuantity() - dto.getQuantity());
            productRepository.save(product);

            // update point for product
            ProductHighLight productHighLight = productHighLightRepository.findLastRecord(dto.getProductId());
            Integer monthDf = productHighLight.getDateAdd().getMonth();
            Integer yearDf = productHighLight.getDateAdd().getYear();

            Date now = new Date();
            Integer monthNow = now.getMonth();
            Integer yearNow = now.getYear();

            if (yearNow > yearDf) {
                ProductHighLight productHighLightNew = new ProductHighLight();
                productHighLightNew.setProductId(dto.getProductId());
                productHighLightNew.setDateAdd(now);
                productHighLightNew.setPoint(1);
                productHighLightRepository.save(productHighLightNew);
            } else if (yearNow.equals(yearDf)) {
                if (monthNow > monthDf) {
                    ProductHighLight productHighLightNew = new ProductHighLight();
                    productHighLightNew.setProductId(dto.getProductId());
                    productHighLightNew.setDateAdd(now);
                    productHighLightNew.setPoint(1);
                    productHighLightRepository.save(productHighLightNew);
                } else if (monthNow.equals(monthDf)) {
                    productHighLight.setPoint(productHighLight.getPoint() + 1);
                    productHighLightRepository.save(productHighLight);
                }
            }
        }
        session.removeAttribute("cartList");

        //send notification
        SendNotification sendNotification = new SendNotification();
        sendNotification.setAccountId(orders.getAccountId());
        String customerName = getAuthorizationFEController().hasLogged() ? getAuthorizationFEController().getAccountDto().getFullName() : ordersDto.getCustomerName();
        sendNotification.setContent("Bạn nhận được một đơn hàng mới từ khách hàng <b>" + customerName + "</b> với mã đơn hàng: <i>" + ordersSave.getCode() + "</i>. Kiểm tra ngay");
        sendNotification.setStatus(DbConstant.SNOTIFICATION_STATUS_ACTIVE);
        sendNotification.setObjectId(ordersSave.getOrdersId());
        sendNotification.setType(DbConstant.NOTIFICATION_TYPE_ORDER);
        sendNotification.setCreateDate(new Date());
        sendNotification.setUpdateDate(new Date());
        if (getAuthorizationFEController().hasLogged()) {
            sendNotification.setCreateBy(getAuthorizationFEController().getAccountDto().getAccountId());
            sendNotification.setUpdateBy(getAuthorizationFEController().getAccountDto().getAccountId());
        }
        sendNotificationRepository.save(sendNotification);
        //receive notification
        Account accountAdmin = accountRepository.findAccountByUserNameAndRoleId("admin", DbConstant.ROLE_ID_ADMIN);
        ReceiveNotification receiveNotification = new ReceiveNotification();
        receiveNotification.setAccountId(accountAdmin.getAccountId());
        receiveNotification.setSendNotificationId(sendNotification.getSendNotificationId());
        receiveNotification.setStatus(DbConstant.NOTIFICATION_STATUS_NOT_SEEN);
        receiveNotification.setStatusBell(DbConstant.NOTIFICATION_STATUS_BELL_NOT_SEEN);
        receiveNotification.setCreateDate(new Date());
        receiveNotification.setUpdateDate(new Date());
        if (getAuthorizationFEController().hasLogged()) {
            receiveNotification.setCreateBy(getAuthorizationFEController().getAccountDto().getAccountId());
            receiveNotification.setUpdateBy(getAuthorizationFEController().getAccountDto().getAccountId());
        }
        notificationRepository.save(receiveNotification);

        setSuccessForm("Đặt hàng thành công, vui lòng chờ thông báo từ cửa hàng.");
        FacesUtil.redirect("/frontend/index2.xhtml");
    }

    public void onChangeShipping() {
        if (ordersDto.getShippingId() == -1) {
            checkShipping = false;
            ordersDto.setShipping(null);
            totalMoney = ordersDto.getTotalAmount();
        } else {
            checkShipping = true;
            Shipping shipping = shippingMap.get(ordersDto.getShippingId());
            pathShipping = shipping.getPath();
            infoShipping = shipping.getDetail();
            ordersDto.setShipping(Double.parseDouble(shipping.getPrice().toString()));
            totalMoney = ordersDto.getTotalAmount();
            totalMoney += ordersDto.getShipping();
        }
        FacesUtil.updateView("orderList");
    }

    @Override
    protected String getMenuId() {
        return null;
    }
}
