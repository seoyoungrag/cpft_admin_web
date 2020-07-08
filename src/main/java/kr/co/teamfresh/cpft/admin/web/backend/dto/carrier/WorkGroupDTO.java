package kr.co.teamfresh.cpft.admin.web.backend.dto.carrier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WorkGroupDTO implements Serializable {
	private String carrierSeq;
	private String workGroupNm;
	private List<WorkGroupManagerDTO> workGroupManagers = new ArrayList<WorkGroupManagerDTO>();
}
