package pjatk.socialeventorganizer.social_event_support.common.paginator;

import org.springframework.data.domain.Sort;

public class TestSortPage {

    private int pageNumber = 0;
    private int pageSize = 10;
    private org.springframework.data.domain.Sort.Direction sortDirection = org.springframework.data.domain.Sort.Direction.ASC;
    private String sortBy = "city";

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
