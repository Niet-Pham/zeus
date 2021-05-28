package vn.tech.website.store.controller.admin.orders;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.CollectionUtils;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.dto.*;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.*;
import vn.tech.website.store.repository.*;
import vn.tech.website.store.util.*;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Named
@Scope(value = "session")
@Getter
@Setter
public class OrdersController extends BaseController {
    @Inject
    private AuthorizationController authorizationController;
    @Inject
    private HttpServletRequest request;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private PaymentsRepository paymentsRepository;
    @Autowired
    private SendNotificationRepository sendNotificationRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    private ProductHighLightRepository productHighLightRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductOptionDetailRepository productOptionDetailRepository;
    @Autowired
    private ProductOptionRepository productOptionRepository;

    private LazyDataModel<OrdersDto> lazyDataModel;
    private OrdersDto ordersDto;
    private OrdersSearchDto searchDto;
    private int orderType;
    private List<OrdersDetailDto> ordersDetailDtoList;
    private List<SelectItem> productList;
    private List<SelectItem> accountList;
    private int paymentMethod;
    private String pathShipping;
    private String infoShipping;
    private Long totalQty = 0L;
    private Double totalMoney = 0D;
    private List<SelectItem> shippingItem;
    private Map<Long, Shipping> shippingMap;
    private boolean isReadonly;
    private Long lastProductId;
    private Long orderIdParams;

    public void initDataOrder() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            orderType = DbConstant.ORDER_TYPE_ORDER;
            String orderId = request.getParameter("orderid");
            orderIdParams = orderId == null ? null : Long.parseLong(orderId);
            resetAll(orderType);
        }
    }

    public void resetAll(Integer orderType) {
        isReadonly = false;
        ordersDto = new OrdersDto();
        searchDto = new OrdersSearchDto();
        productList = new ArrayList<>();
        ordersDetailDtoList = new ArrayList<>();
        accountList = new ArrayList<>();

        searchDto.setOrdersId(orderIdParams);
        onSearch(orderType);

        List<SelectItem> products = convertProduct(0L, 10);
        productList.addAll(products);
        lastProductId = Long.parseLong(products.get(products.size() - 1).getValue().toString());

        List<Account> accounts = accountRepository.findAccountByRoleId(DbConstant.ROLE_ID_USER);
        for (Account obj : accounts) {
            accountList.add(new SelectItem(obj.getAccountId(), obj.getFullName()));
        }

        List<Shipping> shippingList = shippingRepository.findAllShipping();
        shippingItem = new ArrayList<>();
        shippingMap = new HashMap<>();
        for (Shipping s : shippingList) {
            shippingItem.add(new SelectItem(s.getShippingId(), s.getName()));
            shippingMap.put(s.getShippingId(), s);
        }
    }

    public void onSearch(Integer orderType) {
        searchDto.setStatusInit(orderType);
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<OrdersDto>() {
            @Override
            public List<OrdersDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                searchDto.setPageIndex(first);
                searchDto.setPageSize(pageSize);
                searchDto.setSortField(sortField);
                String sort;
                if (sortOrder.equals(sortOrder.ASCENDING)) {
                    sort = "ASC";
                } else {
                    sort = "DESC";
                }
                searchDto.setSortOrder(sort);
                return orderRepository.search(searchDto);
            }

            @Override
            public OrdersDto getRowData(String rowKey) {
                List<OrdersDto> dtoList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (OrdersDto obj : dtoList) {
                    if (obj.getOrdersId().equals(Long.valueOf(value))) {
                        return obj;
                    }
                }
                return null;
            }
        };

        int count = orderRepository.countSearch(searchDto).intValue();
        lazyDataModel.setRowCount(count);
    }

    public void resetDialog() {
        isReadonly = false;
        ordersDto = new OrdersDto();
        ordersDetailDtoList = new ArrayList<>();
    }

    public void loadData() {
        List<SelectItem> loadMoreProduct = convertProduct(lastProductId, 20);
        productList.addAll(loadMoreProduct);
        lastProductId = Long.parseLong(productList.get(productList.size() - 1).getValue().toString());
        FacesUtil.updateView("dlForm");
    }

    public List<SelectItem> convertProduct(Long id, Integer limit) {
        List<Product> products = productRepository.getAllExpertType(id, DbConstant.PRODUCT_TYPE_PARENT, limit);
        List<SelectItem> productListClone = new ArrayList<>();
        for (Product obj : products) {
            List<ProductOptionDetail> productOptionDetails = productOptionDetailRepository.findAllByProductId(obj.getProductId());
            String option = "";
            for (ProductOptionDetail productOptionDetail : productOptionDetails) {
                ProductOption productOption = productOptionRepository.getByProductOptionId(productOptionDetail.getProductOptionId());
                if (option == "") {
                    option += productOption.getOptionName();
                } else {
                    option += " - " + productOption.getOptionName();
                }
            }
            productListClone.add(new SelectItem(obj.getProductId(), obj.getProductName() + "(" + option + ")"));
        }
        return productListClone;
    }

    public void updatePanel() {
        if (ordersDto.getAccountId() != null) {
            Account account = accountRepository.findByAccountId(ordersDto.getAccountId());
            ordersDto.setCustomerName(account.getFullName());
            ordersDto.setPhone(account.getPhone());
            ordersDto.setAddress(account.getAddress());
        }
        FacesUtil.updateView("dlForm");
    }

    public void updatePrice(Integer index) {
        Double unitPrice = 0D;
        Float discount = 0F;
        if (ordersDetailDtoList.get(index).getProductId() != null) {
            Product product = productRepository.getByProductId(ordersDetailDtoList.get(index).getProductId());
            unitPrice = product.getPrice();
            ordersDetailDtoList.get(index).setUnitPrice(unitPrice);
            discount = product.getDiscount() != null ? product.getDiscount() : 0;
            ordersDetailDtoList.get(index).setDiscount(discount);
            Long maxQty = product.getQuantity();
            ordersDetailDtoList.get(index).setMaxQuantity(maxQty);

            ordersDetailDtoList.get(index).getProductDto().setProductImages(new LinkedHashSet<>());
            ordersDetailDtoList.get(index).getProductDto().setProductImages(productImageRepository.getImagePathByProductId(ordersDetailDtoList.get(index).getProductId()));
            if (ordersDetailDtoList.get(index).getProductDto().getProductImages().size() != 0) {
                ordersDetailDtoList.get(index).getProductDto().setImageToShow(ordersDetailDtoList.get(index).getProductDto().getProductImages().iterator().next());
            }

            FacesUtil.updateView("dlForm");
        }
        if (ordersDetailDtoList.get(index).getQuantity() != null) {
            Double amount = (unitPrice - unitPrice * discount / 100) * ordersDetailDtoList.get(index).getQuantity();
            ordersDetailDtoList.get(index).setAmount(amount);
            if (index == 0) {
                ordersDto.setTotalAmount(amount);
            } else {
                ordersDto.setTotalAmount(ordersDto.getTotalAmount() + amount);
            }
            FacesUtil.updateView("dlForm");
        }
    }

    public boolean validateData() {
        if (StringUtils.isBlank(ordersDto.getCustomerName())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập tên khách hàng hoặc chọn khách hàng");
            return false;
        }
        if (StringUtils.isBlank(ordersDto.getPhone())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập số điện thoại");
            return false;
        }
        if (StringUtils.isBlank(ordersDto.getPhone())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập email");
            return false;
        }
        if (StringUtils.isBlank(ordersDto.getAddress())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập địa chỉ");
            return false;
        }
        if (CollectionUtils.isEmpty(ordersDetailDtoList)) {
            FacesUtil.addErrorMessage("Bạn vui lòng thêm loại sản phẩm");
            return false;
        }
        for (OrdersDetailDto obj : ordersDetailDtoList) {
            if (obj.getProductId() == null) {
                FacesUtil.addErrorMessage("Bạn vui lòng chọn sản phẩm");
                return false;
            }
            if (obj.getQuantity() == null) {
                FacesUtil.addErrorMessage("Bạn vui lòng nhập số lượng sản phẩm");
                return false;
            }
        }
        if (ordersDto.getShippingId() == null || ordersDto.getShippingId() == -1) {
            FacesUtil.addErrorMessage("Bạn vui lòng chọn đơn vị vẫn chuyển");
            return false;
        }
        return true;
    }

    public void onSave() {
        if (!validateData()) {
            return;
        }
        Long countCode = orderRepository.findMaxCountCode() != null ? orderRepository.findMaxCountCode() : 0;
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersDto, orders);
        if (orders.getOrdersId() == null) {
            orders.setCode(StringUtil.createCode(null, Constant.ACRONYM_ORDER, countCode));
            orders.setCountCode(StringUtil.createCountCode(orders.getCode(), Constant.ACRONYM_ORDER));
            orders.setStatus(DbConstant.ORDER_STATUS_NOT_APPROVED);
            orders.setCreateBy(authorizationController.getAccountDto().getAccountId());
            orders.setCreateDate(new Date());
        }
        orders.setShippingId(ordersDto.getShippingId());
        orders.setShipping(ordersDto.getShipping());
        orders.setUpdateBy(authorizationController.getAccountDto().getAccountId());
        orders.setUpdateDate(new Date());
        orderRepository.save(orders);

        //update Qty product when edit
        if (ordersDto.getOrdersId() != null) {
            List<OrdersDetail> listToDelete = orderDetailRepository.getAllByOrdersId(ordersDto.getOrdersId());
            for (OrdersDetail ordersDetail : listToDelete) {
                Product product = productRepository.getByProductId(ordersDetail.getProductId());
                product.setQuantity(product.getQuantity() + ordersDetail.getQuantity());
                productRepository.save(product);
                orderDetailRepository.delete(ordersDetail);
            }
        }
        //add orderDetail
        for (OrdersDetailDto dto : ordersDetailDtoList) {
            OrdersDetail ordersDetail = new OrdersDetail();
            ordersDetail.setOrdersId(orders.getOrdersId());
            ordersDetail.setProductId(dto.getProductId());
            ordersDetail.setQuantity(dto.getQuantity());
            ordersDetail.setAmount(dto.getAmount());
            orderDetailRepository.save(ordersDetail);

            // change quality product in stock
            Product product = productRepository.getByProductId(dto.getProductId());
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

        FacesUtil.addSuccessMessage("Lưu thành công.");
        FacesUtil.closeDialog("dialogInsertUpdate");
        onSearch(orderType);
    }

    public void onDeleteProduct(OrdersDetailDto ordersDetailDto) {
        if (ordersDetailDto != null && ordersDto.getTotalAmount() != null) {
            ordersDto.setTotalAmount(ordersDto.getTotalAmount() - ordersDetailDto.getAmount());
        }
        ordersDetailDtoList.remove(ordersDetailDto);
        FacesUtil.updateView("dlForm");
    }

    public void onAddNew() {
        ordersDetailDtoList.add(new OrdersDetailDto());
    }

    public void onUpdate(OrdersDto resultDto) {
        if (resultDto.getStatus() == DbConstant.ORDER_STATUS_NOT_APPROVED) {
            isReadonly = false;
        } else {
            isReadonly = true;
        }
        ordersDetailDtoList = new ArrayList<>();
        ordersDto = new OrdersDto();
        List<OrdersDetail> listOrdersDetails = orderDetailRepository.getAllByOrdersId(resultDto.getOrdersId());
        convertEntityToDto(listOrdersDetails);
        BeanUtils.copyProperties(resultDto, ordersDto);
    }

    private void convertEntityToDto(List<OrdersDetail> ordersDetails) {
        ordersDetailDtoList = new ArrayList<>();
        for (OrdersDetail obj : ordersDetails) {
            OrdersDetailDto dto = new OrdersDetailDto();
            BeanUtils.copyProperties(obj, dto);
            Product product = productRepository.getByProductId(obj.getProductId());
            dto.setUnitPrice(product.getPrice());
            dto.setDiscount(product.getDiscount());
            dto.getProductDto().setProductImages(new LinkedHashSet<>());
            dto.getProductDto().setProductImages(productImageRepository.getImagePathByProductId(dto.getProductId()));
            if (dto.getProductDto().getProductImages().size() != 0) {
                dto.getProductDto().setImageToShow(dto.getProductDto().getProductImages().iterator().next());
            }
            ordersDetailDtoList.add(dto);
        }
    }

    public void onChangeStatusApproved(OrdersDto resultDto) {
        if (resultDto.getStatus() == DbConstant.ORDER_STATUS_NOT_APPROVED) {
            Orders orders = orderRepository.getByOrdersId(resultDto.getOrdersId());
            orders.setStatus(DbConstant.ORDER_STATUS_APPROVED);
            orders.setUpdateBy(authorizationController.getAccountDto().getAccountId());
            orders.setUpdateDate(new Date());
            orderRepository.save(orders);

            if (orders.getAccountId() != null) {
                SendNotification sendNotification = new SendNotification();
                sendNotification.setAccountId(authorizationController.getAccountDto().getAccountId());
                sendNotification.setContent("Đơn hàng có mã: <b><i>" + orders.getCode() + "</i></b> của bạn đã được chuẩn bị xong, vui lòng chờ đến khi đơn hàng được vận chuyển đến bạn.");
                sendNotification.setStatus(DbConstant.SNOTIFICATION_STATUS_ACTIVE);
                sendNotification.setObjectId(orders.getOrdersId());
                sendNotification.setType(DbConstant.NOTIFICATION_TYPE_ORDER);
                sendNotification.setCreateDate(new Date());
                sendNotification.setUpdateDate(new Date());
                sendNotificationRepository.save(sendNotification);

                //receive notification
                ReceiveNotification receiveNotification = new ReceiveNotification();
                receiveNotification.setAccountId(orders.getAccountId());
                receiveNotification.setSendNotificationId(sendNotification.getSendNotificationId());
                receiveNotification.setStatus(DbConstant.NOTIFICATION_STATUS_NOT_SEEN);
                receiveNotification.setStatusBell(DbConstant.NOTIFICATION_STATUS_BELL_NOT_SEEN);
                receiveNotification.setCreateDate(new Date());
                receiveNotification.setUpdateDate(new Date());
                notificationRepository.save(receiveNotification);
            } else {
                EmailUtil.getInstance().sendNotificationApprovedOrder(orders.getEmail(), orders.getCustomerName(), orders.getCode(), orders.getCreateDate(), Constant.ZEUS_SHOP, Constant.ZEUS_FOUNDER, Constant.URL_LOGO_ZEUS);
            }

        }
        if (resultDto.getStatus() == DbConstant.ORDER_STATUS_APPROVED) {
            Orders orders = orderRepository.getByOrdersId(resultDto.getOrdersId());
            orders.setStatus(DbConstant.ORDER_STATUS_NOT_APPROVED);
            orders.setUpdateBy(authorizationController.getAccountDto().getAccountId());
            orders.setUpdateDate(new Date());
            orderRepository.save(orders);
        }
        FacesUtil.addSuccessMessage("Đổi trạng thái thành công.");
        onSearch(orderType);
    }

    public void onCancelOrder(OrdersDto resultDto) {
        Orders orders = orderRepository.getByOrdersId(resultDto.getOrdersId());
        orders.setStatus(DbConstant.ORDER_STATUS_CANCEL);
        orders.setUpdateBy(authorizationController.getAccountDto().getAccountId());
        orders.setUpdateDate(new Date());
        orderRepository.save(orders);
        FacesUtil.addSuccessMessage("Đã hủy đơn hàng.");
        onSearch(orderType);
    }

    public void onChangeStatusToPaid(OrdersDto resultDto) {
        Orders orders = orderRepository.getByOrdersId(resultDto.getOrdersId());
        orders.setStatus(DbConstant.ORDER_STATUS_PAID);
        String code = orders.getCode().replace(Constant.ACRONYM_ORDER, Constant.ACRONYM_BILL);
        orders.setCode(code);
        orders.setUpdateBy(authorizationController.getAccountDto().getAccountId());
        orders.setUpdateDate(new Date());
        orderRepository.save(orders);
        Payments payments = new Payments();
        BeanUtils.copyProperties(orders, payments);
        payments.setTotalAmount(resultDto.getAllTotalAmount());
        paymentsRepository.save(payments);
        FacesUtil.addSuccessMessage("Thanh toán thành công.");
        FacesUtil.closeDialog("dialogPaymentMethod");
        onSearch(orderType);
    }

    public void onChangeShipping() {
        if (ordersDto.getShippingId() == -1) {
            ordersDto.setShipping(null);
            ordersDto.setAllTotalAmount(ordersDto.getTotalAmount());
        } else {
            Shipping shipping = shippingMap.get(ordersDto.getShippingId());
            pathShipping = shipping.getPath();
            infoShipping = shipping.getDetail();
            ordersDto.setShipping(Double.parseDouble(shipping.getPrice().toString()));
            ordersDto.setAllTotalAmount(ordersDto.getTotalAmount() + ordersDto.getShipping());
        }
        FacesUtil.updateView("dlForm");
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
