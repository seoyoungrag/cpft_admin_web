package kr.co.teamfresh.cpft.admin.web.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@Embeddable
public class TruckPK implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_SEQ", nullable = false)
	@JsonProperty(access = Access.READ_ONLY)
	private TruckOwner truckOnwer;
	
	@Column(name="CAR_TYPE")
	private String carType;
	
	@Column(name="TON_TYPE")
	private String tonType;
	
}
