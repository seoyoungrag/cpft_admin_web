package kr.co.teamfresh.cpft.admin.web.backend.dto.carrier;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class CarrierDTO implements Serializable {
	private String carrierSeq;
	private String CarrierNm;
	//private Set<User> users = new HashSet<User>(0);
	private Set<WorkGroupDTO> workGroups = new HashSet<WorkGroupDTO>(0);
}
