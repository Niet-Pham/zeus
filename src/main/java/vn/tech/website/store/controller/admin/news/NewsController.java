package vn.tech.website.store.controller.admin.news;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.controller.admin.common.UploadSingleImageController;
import vn.tech.website.store.dto.CategoryDto;
import vn.tech.website.store.dto.CategorySearchDto;
import vn.tech.website.store.dto.NewsDto;
import vn.tech.website.store.dto.NewsSearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.*;
import vn.tech.website.store.repository.CategoryRepository;
import vn.tech.website.store.repository.NewsRepository;
import vn.tech.website.store.repository.NotificationRepository;
import vn.tech.website.store.repository.SendNotificationRepository;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@Scope(value = "session")
@Getter
@Setter
public class NewsController extends BaseController {
    @Inject
    private AuthorizationController authorizationController;
    @Inject
    private UploadSingleImageController uploadSingleImageController;

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SendNotificationRepository sendNotificationRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    private LazyDataModel<NewsDto> lazyDataModel;
    private NewsDto newsDto;
    private NewsSearchDto searchDto;
    private List<SelectItem> categoryList;

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        newsDto = new NewsDto();
        searchDto = new NewsSearchDto();
        categoryList = new ArrayList<>();
        uploadSingleImageController.resetAll(null);
        List<Category> categories = categoryRepository.findAllCategoryNews();
        for (Category obj : categories) {
            categoryList.add(new SelectItem(obj.getCategoryId(), obj.getCategoryName()));
        }
        onSearch();
    }

    public void onSearch() {
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<NewsDto>() {
            @Override
            public List<NewsDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
                return newsRepository.search(searchDto);
            }

            @Override
            public NewsDto getRowData(String rowKey) {
                List<NewsDto> dtoList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (NewsDto obj : dtoList) {
                    if (obj.getNewsId().equals(Long.valueOf(value))) {
                        return obj;
                    }
                }
                return null;
            }
        };

        int count = newsRepository.countSearch(searchDto).intValue();
        lazyDataModel.setRowCount(count);
    }

    public boolean validateData() {
        if (newsDto.getCategoryId() == null) {
            FacesUtil.addErrorMessage("Bạn vui lòng chọn loại tin tức");
            return false;
        }
        newsDto.setImgPath(uploadSingleImageController.getImagePath());
        if (newsDto.getImgPath() == null) {
            FacesUtil.addErrorMessage("Bạn vui lòng tải ảnh cho tin tức");
            return false;
        }
        if (StringUtils.isBlank(newsDto.getTitle())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập tiêu đề");
            return false;
        }
        if (StringUtils.isBlank(newsDto.getShortContent())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập nội dung tóm tắt");
            return false;
        }
        if (StringUtils.isBlank(newsDto.getContent())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập nội dung");
            return false;
        }
        return true;
    }

    public void onSave() {
        if (!validateData()) {
            return;
        }
        News news = new News();
        BeanUtils.copyProperties(newsDto, news);
        news.setStatus(DbConstant.NEWS_STATUS_ACTIVE);
        news.setCreateDate(new Date());
        news.setCreateBy(authorizationController.getAccountDto().getAccountId());
        news.setUpdateDate(new Date());
        news.setUpdateBy(authorizationController.getAccountDto().getAccountId());
        newsRepository.save(news);

        if (newsDto.getNewsId() == null) {
            //send notification
            SendNotification sendNotification = new SendNotification();
            sendNotification.setAccountId(authorizationController.getAccountDto().getAccountId());
            sendNotification.setContent("Có tin tức mới: <b><i>" + news.getTitle() + "</i></b>");
            sendNotification.setStatus(DbConstant.SNOTIFICATION_STATUS_ACTIVE);
            sendNotification.setObjectId(news.getNewsId());
            sendNotification.setType(DbConstant.NOTIFICATION_TYPE_NEWS);
            sendNotification.setCreateDate(new Date());
            sendNotification.setUpdateDate(new Date());
            sendNotificationRepository.save(sendNotification);
            //receive notification
            List<Account> customerList = getAccountRepository().findAccountByRoleId(DbConstant.ROLE_ID_USER);
            for (Account customer : customerList) {
                ReceiveNotification receiveNotification = new ReceiveNotification();
                receiveNotification.setAccountId(customer.getAccountId());
                receiveNotification.setSendNotificationId(sendNotification.getSendNotificationId());
                receiveNotification.setStatus(DbConstant.NOTIFICATION_STATUS_NOT_SEEN);
                receiveNotification.setStatusBell(DbConstant.NOTIFICATION_STATUS_BELL_NOT_SEEN);
                receiveNotification.setCreateDate(new Date());
                receiveNotification.setUpdateDate(new Date());
                notificationRepository.save(receiveNotification);
            }
        }

        FacesUtil.addSuccessMessage("Lưu thành công.");
        FacesUtil.closeDialog("dialogInsertUpdate");
        onSearch();
    }

    public void showUpdatePopup(NewsDto resultDto) {
        uploadSingleImageController.resetAll(resultDto.getImgPath());
        BeanUtils.copyProperties(resultDto, newsDto);
    }

    public void onDelete(NewsDto resultDto) {
        resultDto.setStatus(DbConstant.NEWS_STATUS_INACTIVE);
        News news = new News();
        BeanUtils.copyProperties(resultDto, news);
        newsRepository.save(news);
        FacesUtil.addSuccessMessage("Xóa thành công.");
        onSearch();
    }

    public void resetDialog() {
        newsDto = new NewsDto();
        uploadSingleImageController.resetAll(null);
    }

    public String removeSpaceOfString(String str) {
        return str.trim().replaceAll("[\\s|\\u00A0]+", " ");
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
