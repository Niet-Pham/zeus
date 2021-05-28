package vn.tech.website.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class BaseSearchDto {
    private static final long serialVersionUID = 1261520726062235615L;

    protected String sortField;
    protected String sortOrder;
    protected String groupByFields;
    protected int pageIndex;
    protected int pageSize;
    protected Date fromDate;
    protected Date toDate;
    protected String keyword;
    protected boolean hasInactived;
}
