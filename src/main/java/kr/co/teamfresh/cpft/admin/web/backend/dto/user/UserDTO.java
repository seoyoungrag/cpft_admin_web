package kr.co.teamfresh.cpft.admin.web.backend.dto.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.teamfresh.cpft.admin.web.backend.dto.carrier.CarrierDTO;
import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderDTO;
import lombok.Data;

@Data
public class UserDTO implements Serializable{

	private String userSeq;
	@JsonIgnore
	private String userLoginId;
	@JsonIgnore
	private String userLoginPw;
	private String userNm;
	private String userEmail;
	private Set<OrderDTO> orders = new HashSet<OrderDTO>(0);
	private CarrierDTO carrier;
	private List<String> roles = new ArrayList<>();
}
