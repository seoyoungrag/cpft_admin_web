package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(subTypes = {TaxInvoice.class})
public class RegistBrokerTaxInvoiceEX {

    //@XmlElement(name = "CERTKEY")
    @ApiModelProperty(value = "certkey", example = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9", hidden = true, position = 1)
    protected String certkey;
    //@XmlElement(name = "CorpNum")
    @ApiModelProperty(value = "corpNum", example = "5618801138", required = false, hidden = true, position = 2)
    protected String corpNum;
	@ApiModelProperty(value = "issueTiming", example = "1" ,required = true, position = 3)
	protected int issueTiming;
    //@XmlElement(name = "Invoice")
	@ApiModelProperty(value = "invoice", required = true, position = 4)
    protected TaxInvoice invoice;
}
