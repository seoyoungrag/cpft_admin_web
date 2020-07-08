package kr.co.teamfresh.cpft.admin.web.backend.config.modelmap.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderDTO;
import kr.co.teamfresh.cpft.admin.web.backend.entity.carrier.WorkGroupManager;
import kr.co.teamfresh.cpft.admin.web.backend.entity.carrier.WorkGroupManagerPK;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.Order;

public class OrderDtoToOrderEnCv implements Converter<OrderDTO, Order> {

	@Override
	public Order convert(MappingContext<OrderDTO, Order> context) {
		
    	WorkGroupManager wg = new WorkGroupManager();
    	WorkGroupManagerPK workGroupManagerPK = new WorkGroupManagerPK(context.getSource().getCarrierSeq(), context.getSource().getWorkGroupNm(), context.getSource().getWorkGroupManager());
    	wg.setWorkGroupManagerPK(workGroupManagerPK);
    	context.getDestination().setWorkGroupManager(wg);
    	return context.getDestination();
	}

}
