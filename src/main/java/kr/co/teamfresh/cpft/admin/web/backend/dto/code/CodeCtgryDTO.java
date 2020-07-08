package kr.co.teamfresh.cpft.admin.web.backend.dto.code;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class CodeCtgryDTO  implements Serializable{

	private String codeCtgryNm;
	private Set<CodeDTO> codes = new HashSet<CodeDTO>(0);
	
}
