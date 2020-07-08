package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(subTypes = {TaxInvoice.class})
public class RegistAndIssueTaxInvoice {

    @ApiModelProperty(value = "certkey", example = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9", hidden = true, position = 1)
	String certKey;
    @ApiModelProperty(value = "corpNum", example = "5618801138", required = false, hidden = true, position = 2)
	String corpNum;
	@ApiModelProperty(value = "forceIssue", example = "true", required = true, position = 4)
	boolean sendSms;
	@ApiModelProperty(value = "forceIssue", example = "true", required = true, position = 4)
	boolean forceIssue;
	@ApiModelProperty(value = "mailTitle", example = "", required = false, hidden = true, position = 5)
	String mailTitle;
    @ApiModelProperty(value = "invoice", required = true, position = 6)
	TaxInvoice invoice;
}
