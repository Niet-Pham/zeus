package vn.tech.website.store.controller.frontend.news;


import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.AuthorizationFEController;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.controller.frontend.common.PaginationController;
import vn.tech.website.store.dto.*;
import vn.tech.website.store.model.News;
import vn.tech.website.store.model.Product;
import vn.tech.website.store.repository.*;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Named
@Scope(value = "session")
@Getter
@Setter
public class NewsFEController extends BaseFEController {
    @Inject
    private AuthorizationFEController authorizationFEController;
    @Inject
    private HttpServletRequest request;

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductImageRepository productImageRepository;

    private List<NewsDto> newsDtoList;
    private String cateId;
    private Long newsIdParams;
    private List<OrdersDetailDto> listAddToCart;
    private NewsSearchDto searchDto;
    private PaginationController<NewsDto> pagination;
    private NewsDto newsDto;

    public NewsFEController(){
        pagination = new PaginationController<>();
    }

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            cateId = request.getParameter("catid");
            String newsId = request.getParameter("newsid");
            newsIdParams = newsId == null ? null : Long.parseLong(newsId);
            resetAll(cateId == null ? null : Long.parseLong(cateId));
        }
    }

    public void resetAll(Long categoryId) {
        newsDtoList = new ArrayList<>();
        searchDto = new NewsSearchDto();
        pagination.setRequest(request);
        newsDto = new NewsDto();
        if (newsIdParams != null){
            News news = newsRepository.getByNewsId(newsIdParams);
            BeanUtils.copyProperties(news,newsDto);
            viewNewsDetail(newsDto);
            return;
        }
        onSearch(categoryId);
    }

    public void onSearch(Long categoryId) {
        searchDto.setCategoryId(categoryId);
        pagination.setLazyDataModel(new LazyDataModel<NewsDto>() {
            @Override
            public List<NewsDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                searchDto.setPageIndex(first);
                searchDto.setPageSize(pageSize);
                searchDto.setSortField(sortField);
                String sort = "";
                if (sortOrder == null || sortOrder.equals(SortOrder.DESCENDING)) {
                    sort = "DESC";
                } else {
                    sort = "ASC";
                }
                searchDto.setSortOrder(sort);
                return newsRepository.search(searchDto);
            }

            @Override
            public int getRowCount() {
                return newsRepository.countSearch(searchDto).intValue();
            }
        });
        pagination.loadData();
    }

    public void viewNewsDetail(NewsDto resultDto) {
        newsDto = new NewsDto();
        BeanUtils.copyProperties(resultDto, newsDto);
        FacesUtil.redirect("/frontend/news/news.xhtml");
    }

    @Override
    protected String getMenuId() {
        return null;
    }
}
