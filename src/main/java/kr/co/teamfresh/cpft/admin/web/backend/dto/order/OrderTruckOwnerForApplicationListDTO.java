package kr.co.teamfresh.cpft.admin.web.backend.dto.order;

import java.io.Serializable;

import kr.co.teamfresh.cpft.admin.web.backend.dto.CommonDate;
import kr.co.teamfresh.cpft.admin.web.backend.dto.truckOwner.TruckOwnerForApplicationListDTO;
import lombok.Data;

@Data
public class OrderTruckOwnerForApplicationListDTO extends CommonDate implements Serializable{

	private OrderDTO order;
	private TruckOwnerForApplicationListDTO truckOwner;
	private String message;
	private String status;
	private String isRead;
	
}
