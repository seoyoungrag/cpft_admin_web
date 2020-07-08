package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(subTypes = {InvoicerParty.class, ArrayOfTaxInvoiceTradeLineItem.class})
public class TaxInvoiceForBroker {

	@ApiModelProperty(value = "invoiceeASPEmail", example="youngrag.seo@timf.co.kr", required = false, hidden = true)
	String invoiceeASPEmail; //ASP 이메일 //타 대용량 연계사업자의 이메일 주소를 기입합니다. 기입 시 국세청 전송완료 후 해당 이메일로 암호화된 전자(세금)계산서 XML파일을 첨부하여 전송하게 됩니다.

	@ApiModelProperty(value = "issueDirection", example="1", required = true)
	int issueDirection; //발행방향 //1:정발행 2:역발행

	@ApiModelProperty(value = "taxInvoiceType", example="4", required = true)
	int taxInvoiceType; //(세금)계산서 형태 //1:세금계산서 2:계산서 4:위수탁세금계산서 5:위수탁계산서 7:수입세금계산서 8:수입계산서

	@ApiModelProperty(value = "taxType", example="2", required = true)
	int taxType; //과세형태 //1:과세 2:영세 3:면세(계산서)

	@ApiModelProperty(value = "taxCalcType", example="2", required = true)
	int taxCalcType; //세율계산형태 //1:절상 2:절사 3:반올림

	@ApiModelProperty(value = "purposeType", example="1", required = true)
	int purposeType; //영수/청구형태 //1:영수 2:청구

	@ApiModelProperty(value = "modifyCode", required = false, hidden = true)
	String modifyCode; //수정사유 코드 //수정(세금)계산서 작성 시 필수 1:기재사항의 착오/정정 2:공급가액의 변동 3:재화의 환입 4:계약의 혜제 5:내국신용장 사후개설 6:단순 착오에 의한 이중발급

	@ApiModelProperty(value = "writeDate", required = false, hidden = true)
	String writeDate; //작성일자 //yyyyMMdd 형식, 미입력시 기본값은 현재일자.

	@ApiModelProperty(value = "amountTotal", example="0", required = true)
	String amountTotal; //공급가액 //소수점 불가, 컴마(',')를 제외한 숫자만 입력

	@ApiModelProperty(value = "taxTotal", example="0", required = true)
	String taxTotal; //세액 //소수점 불가, 컴마(',')를 제외한 숫자만 입력

	@ApiModelProperty(value = "totalAmount", example="0", required = true)
	String totalAmount; //합계금액 //소수점 불가, 컴마(',')를 제외한 숫자만 입력

	@ApiModelProperty(value = "cash", example="0", required = false, hidden = true)
	String cash; //현금 //소수점 불가, 컴마(',')를 제외한 숫자만 입력

	@ApiModelProperty(value = "chkBill", example="0", required = false, hidden = true)
	String chkBill; //수표 //소수점 불가, 컴마(',')를 제외한 숫자만 입력

	@ApiModelProperty(value = "note", example="0", required = false, hidden = true)
	String note; //어음 //소수점 불가, 컴마(',')를 제외한 숫자만 입력

	@ApiModelProperty(value = "credit", example="0", required = false, hidden = true)
	String credit; //외상미수금 //소수점 불가, 컴마(',')를 제외한 숫자만 입력

	@ApiModelProperty(value = "kwon", example="", required = false, hidden = true)
	String kwon; //권 
	
	@ApiModelProperty(value = "ho", example="", required = false, hidden = true)
	String ho; //호 

	@ApiModelProperty(value = "serialNum", example="", required = false, hidden = true)
	String serialNum; //일련번호

	@ApiModelProperty(value = "remark1", example="", required = false, hidden = true)
	String remark1; //비고1
	@ApiModelProperty(value = "remark2", example="", required = false, hidden = true)
	String remark2; //비고2
	@ApiModelProperty(value = "remark3", example="", required = false, hidden = true)
	String remark3; //비고3
	

	@ApiModelProperty(value = "invoicerParty", required = false)
    protected InvoiceeParty invoicerParty;

	@ApiModelProperty(value = "invoiceeParty", required = false)
    protected InvoicerParty invoiceeParty;

	@ApiModelProperty(value = "brokerParty", required = false)
    protected BrokerParty brokerParty;

	@ApiModelProperty(value = "taxInvoiceTradeLineItems", required = false)
    protected ArrayOfTaxInvoiceTradeLineItem taxInvoiceTradeLineItems;
	
	
}
