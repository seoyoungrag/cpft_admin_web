package kr.co.teamfresh.cpft.admin.web.backend.util;

import java.util.List;

import lombok.Data;

@Data
public class DatatableVO<T>{
	public int draw;
	public int start;
	public int length;
    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;
}
