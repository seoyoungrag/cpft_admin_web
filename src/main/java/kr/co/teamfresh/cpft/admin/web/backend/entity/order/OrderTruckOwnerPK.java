package kr.co.teamfresh.cpft.admin.web.backend.entity.order;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import kr.co.teamfresh.cpft.admin.web.backend.entity.TruckOwner;

import lombok.Data;

@Data
@Embeddable
public class OrderTruckOwnerPK implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ORDER_SEQ", nullable = false)
	@JsonProperty(access = Access.READ_ONLY)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USER_SEQ", nullable = false)
	@JsonProperty(access = Access.READ_ONLY)
	private TruckOwner truckOnwer;
}
