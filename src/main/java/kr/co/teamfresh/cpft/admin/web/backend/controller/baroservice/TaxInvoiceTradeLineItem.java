package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class TaxInvoiceTradeLineItem {
    @ApiModelProperty(value = "purchaseExpiry", example = "20200622", required = false)
	String purchaseExpiry; //공급일자 //yyyyMMdd 형식
    @ApiModelProperty(value = "name", example = "품목1", required = false)
	String name; //품목
    @ApiModelProperty(value = "information", example = "규격1", required = false)
	String information; //규격
    @ApiModelProperty(value = "chargeableUnit", example = "10", required = false)
	String chargeableUnit; //수량 //소수점 2자리 허용, 컴마(',')를 제외한 숫자만 입력
    @ApiModelProperty(value = "unitPrice", example = "1000000", required = false)
	String unitPrice; //단가 //소수점 2자리 허용, 컴마(',')를 제외한 숫자만 입력
    @ApiModelProperty(value = "amount", example = "10000000", required = false)
	String amount; //공급가액 //소수점 불가, 컴마(',')를 제외한 숫자만 입력
    @ApiModelProperty(value = "tax", example = "1000000", required = false)
	String tax; //세액 //소수점 불가, 컴마(',')를 제외한 숫자만 입력
    @ApiModelProperty(value = "description", example = "비고", required = false)
	String description; //비고
}
