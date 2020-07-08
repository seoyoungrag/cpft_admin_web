package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel()
public class InvoicerParty {
	@ApiModelProperty(value = "mgtNum", example="ABC123", required = false, hidden = true)
	String mgtNum;// 연동사부여 문자키
	@ApiModelProperty(value = "corpNum", example="1463400539", required = true)
	String corpNum; //사업자 번호 //	'-'를 제외한 숫자만 입력 개인의 경우 주민등록번호 13자리, 외국인인 경우 '9999999999999'를 입력하고, 비고1 에 외국인등록번호 또는 여권번호 기입
	@ApiModelProperty(value = "taxRegID", example="", required = false, hidden = true)
	String taxRegID; //종사업장 번호 // 사업자 단위 과세제도 사용시 국세청 부여 종사업장 식별번호 숫자 4자리
	@ApiModelProperty(value = "corpName", example="주식회사 콜드체인로지스", required = true)
	String corpName; // 회사명 //특수문자 사용 불가 ex. 특수문자 중 1 byte '㈜' 는 '(주)'로 입력
	@ApiModelProperty(value = "ceoName", example="황우연", required = true)
	String ceoName; //대표자명
	@ApiModelProperty(value = "addr", example="경기도 이천시 신둔면", required = true)
	String addr; //주소
	@ApiModelProperty(value = "bizClass", example="", required = false, hidden = true)
	String bizClass; //업종
	@ApiModelProperty(value = "bizType", example="", required = false, hidden = true)
	String bizType; //업태
	@ApiModelProperty(value = "contactID", example="timfcc", required = false, hidden = true)
	String contactID="timflabs1"; //바로빌 회원 아이디 //대소문자 구분
	@ApiModelProperty(value = "contactName", example="유아름", required = false, hidden = true)
	String contactName="서영락"; //담당자명
	@ApiModelProperty(value = "hp", example="", required = false, hidden = true)
	String hp; //전화번호
	@ApiModelProperty(value = "tel", example="", required = false, hidden = true)
	String tel; //휴대폰
	@ApiModelProperty(value = "email", example="ahreum.yu@timf.co.kr", required = true)
	String email; //이메일
}
