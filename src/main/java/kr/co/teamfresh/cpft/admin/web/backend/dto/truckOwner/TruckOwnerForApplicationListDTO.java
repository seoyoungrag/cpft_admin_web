package kr.co.teamfresh.cpft.admin.web.backend.dto.truckOwner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TruckOwnerForApplicationListDTO implements Serializable{

	private String userSeq;
	@JsonIgnore
	private String userLoginId;
	@JsonIgnore
	private String userLoginPw;
	private String userNm;
	private String userEmail;
    private int age;
    private String carrerCn;
    private String carrerDetail;
    private String phone;
	private List<TruckDTO> trucks = new ArrayList<TruckDTO>(0);
	private List<String> crqfcs = new ArrayList<>();
	
}
