package kr.co.teamfresh.cpft.admin.web.backend.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class OrderWorkDayPK implements Serializable {
    @Column(name = "ORDER_SEQ")
	private String orderSeq;
    @Column(name = "WORK_DAY")
	private String workDay;
}
