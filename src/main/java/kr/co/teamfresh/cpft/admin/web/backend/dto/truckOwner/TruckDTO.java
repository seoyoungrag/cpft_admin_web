package kr.co.teamfresh.cpft.admin.web.backend.dto.truckOwner;

import lombok.Data;

@Data
public class TruckDTO {
	private String carType;
	private String tonType;
	public TruckDTO() {
		super();
	}
	public TruckDTO(String carType, String tonType) {
		super();
		this.carType = carType;
		this.tonType = tonType;
	}
	
}
