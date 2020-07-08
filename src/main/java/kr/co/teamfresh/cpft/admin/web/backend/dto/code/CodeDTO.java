package kr.co.teamfresh.cpft.admin.web.backend.dto.code;

import java.io.Serializable;

import lombok.Data;

@Data
public class CodeDTO  implements Serializable{

	private String code;
	private String codeCtgryNm;
	private String codeValue;
	private String codeDc;
	private char codeUseYn;
	
}
