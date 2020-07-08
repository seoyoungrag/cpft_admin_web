package kr.co.teamfresh.cpft.admin.web.backend.dto.order;

import java.io.Serializable;

import kr.co.teamfresh.cpft.admin.web.backend.dto.CommonDate;
import kr.co.teamfresh.cpft.admin.web.backend.dto.truckOwner.TruckOwnerForApplicationListDTO;
import lombok.Data;

@Data
public class OrderTruckOwnerForOrderManageDTO extends CommonDate implements Serializable{

	private String status;
	private String isRead;
	
}
