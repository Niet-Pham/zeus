package vn.tech.website.store.controller.frontend.orders;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.simpleframework.xml.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.controller.frontend.common.PaginationController;
import vn.tech.website.store.controller.frontend.product.ProductDetailFEController;
import vn.tech.website.store.dto.OrdersDetailDto;
import vn.tech.website.store.dto.OrdersDto;
import vn.tech.website.store.dto.OrdersSearchDto;
import vn.tech.website.store.dto.ProductDto;
import vn.tech.website.store.dto.payment.PaymentDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.model.OrdersDetail;
import vn.tech.website.store.model.Product;
import vn.tech.website.store.model.Shipping;
import vn.tech.website.store.repository.*;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;

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
public class BillFEController extends BaseFEController {
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
    @Autowired
    private ProductImageRepository productImageRepository;

    private PaginationController<OrdersDto> pagination;
    private Map<String, OrdersDto> ordersDtoMap;
    private OrdersDto ordersDto;
    private PaymentDto paymentDto;
    private LazyDataModel<OrdersDto> lazyDataModel;
    private OrdersSearchDto searchDto;
    private List<OrdersDetailDto> ordersDetailDtoList;

    public BillFEController() {
        pagination = new PaginationController<>();
    }

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        ordersDto = new OrdersDto();
        ordersDto.setShippingId(-1L);
        ordersDtoMap = new LinkedHashMap<>();
        searchDto = new OrdersSearchDto();
        searchDto.setSortOrder("DECS");
        onSearch();
        ordersDetailDtoList = new ArrayList<>();
    }

    public void onSearch() {
        searchDto.setSortOrder("DECS");
        searchDto.setAccountId(getAuthorizationFEController().getAccountDto().getAccountId());
        List<OrdersDto> ordersDtoList = orderRepository.search(searchDto);
        for (OrdersDto dto : ordersDtoList) {
            dto.setAllTotalAmount(dto.getTotalAmount() + dto.getShipping());
            switch (dto.getStatus()) {
                case DbConstant.ORDER_STATUS_NOT_APPROVED:
                    dto.setStatusString(DbConstant.ORDER_STATUS_NOT_APPROVED_STRING);
                    break;
                case DbConstant.ORDER_STATUS_APPROVED:
                    dto.setStatusString(DbConstant.ORDER_STATUS_APPROVED_STRING);
                    break;
                case DbConstant.ORDER_STATUS_CANCEL:
                    dto.setStatusString(DbConstant.ORDER_STATUS_CANCEL_STRING);
                    break;
                case DbConstant.ORDER_STATUS_PAID:
                    dto.setStatusString(DbConstant.ORDER_STATUS_PAID_STRING);
                    break;
            }
            dto.setShowDetail(false);
            List<OrdersDetail> ordersDetailList = orderDetailRepository.getAllByOrdersId(dto.getOrdersId());
            for (OrdersDetail ordersDetail : ordersDetailList) {
                OrdersDetailDto ordersDetailDto = new OrdersDetailDto();
                BeanUtils.copyProperties(ordersDetail, ordersDetailDto);
                Product product = productRepository.getByProductId(ordersDetail.getProductId());
                BeanUtils.copyProperties(product,ordersDetailDto.getProductDto());
                ordersDetailDto.getProductDto().setProductImages(new LinkedHashSet<>());
                ordersDetailDto.getProductDto().setProductImages(productImageRepository.getImagePathByProductId(ordersDetailDto.getProductId()));
                if (ordersDetailDto.getProductDto().getProductImages().size() != 0) {
                    ordersDetailDto.getProductDto().setImageToShow(ordersDetailDto.getProductDto().getProductImages().iterator().next());
                }
                dto.getOrdersDetailDtoList().add(ordersDetailDto);
            }
            ordersDtoMap.put(dto.getCode(), dto);
        }
    }

    public void onShowDetail(OrdersDto resultDto) {
        ordersDetailDtoList = new ArrayList<>();
        ordersDetailDtoList = resultDto.getOrdersDetailDtoList();
    }

    @Override
    protected String getMenuId() {
        return null;
    }
}
