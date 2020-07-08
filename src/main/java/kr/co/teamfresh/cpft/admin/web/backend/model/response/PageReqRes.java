package kr.co.teamfresh.cpft.admin.web.backend.model.response;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.teamfresh.cpft.admin.web.backend.util.DatatableOrderingVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class PageReqRes<T,D> extends CommonResult implements Pageable {

	public int draw;
    @ApiModelProperty(value = "start", example = "0", position = 1)
	public int start;
    @ApiModelProperty(value = "length", example = "10", position = 2)
	public int length;
    private long recordsTotal;
    private long recordsFiltered;
    private List<D> data;
    
    private List<DatatableOrderingVO> order;
	
    private List<Map> columns;

	@Override
	public int getPageNumber() {
		int page = this.start/this.length; 
		if (page < 0) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}
	    return page;
	}
	@Override
	public int getPageSize() {
		if (this.length < 1) {
			throw new IllegalArgumentException("Page size must not be less than one!");
		}
		return this.length;
	}
	
	@Override
	public long getOffset() {
		return this.start;
	}
	@Override
	public Sort getSort() {
		Sort sort = Sort.unsorted();
		if(this.order!=null && this.order.size()>0) {
			for(DatatableOrderingVO order : this.order) {
				Direction dir = Direction.DESC;
				if(order.getDir().equals("asc")) {
					dir = Direction.ASC;
				}
				sort = sort.and(Sort.by(dir, String.valueOf(this.columns.get(order.getColumn()).get("data"))));	
			}
		}
		return sort;
	}
	@Override
	public Pageable next() {
		return null;
	}
	@Override
	public Pageable previousOrFirst() {
		return this;
	}
	@Override
	public Pageable first() {
	    return this;
	}
	@Override
	public boolean hasPrevious() {
		return this.getPageNumber() > 0;
	}
}