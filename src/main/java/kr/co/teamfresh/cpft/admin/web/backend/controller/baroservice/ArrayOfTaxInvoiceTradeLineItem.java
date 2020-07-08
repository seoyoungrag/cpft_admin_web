package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(subTypes = {TaxInvoiceTradeLineItem.class})
public class ArrayOfTaxInvoiceTradeLineItem {

    @ApiModelProperty(value = "taxInvoiceTradeLineItem", required = true)
    protected List<TaxInvoiceTradeLineItem> taxInvoiceTradeLineItem;
    public List<TaxInvoiceTradeLineItem> getTaxInvoiceTradeLineItem() {
        if (taxInvoiceTradeLineItem == null) {
            taxInvoiceTradeLineItem = new ArrayList<TaxInvoiceTradeLineItem>();
            TaxInvoiceTradeLineItem item = new TaxInvoiceTradeLineItem();
            item.setPurchaseExpiry("20200624");
            item.setName("품목1");
            item.setInformation("규격");
            item.setChargeableUnit("1.00");
            item.setUnitPrice("1.00");
            item.setAmount("10000");
            item.setTax("100");
            taxInvoiceTradeLineItem.add(new TaxInvoiceTradeLineItem());
        }
        return this.taxInvoiceTradeLineItem;
    }
}
