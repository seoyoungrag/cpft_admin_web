package kr.co.teamfresh.cpft.admin.web.backend.config.modelmap.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.modelmapper.PropertyMap;

import kr.co.teamfresh.cpft.admin.web.backend.dto.truckOwner.TruckDTO;
import kr.co.teamfresh.cpft.admin.web.backend.dto.truckOwner.TruckOwnerForTruckOwnerListDTO;
import kr.co.teamfresh.cpft.admin.web.backend.entity.Truck;
import kr.co.teamfresh.cpft.admin.web.backend.entity.TruckOwner;

public class TruckOwnerEnToForTruckOwnerListDtoMap extends PropertyMap<TruckOwner, TruckOwnerForTruckOwnerListDTO> {
	@Override
	protected void configure() {
		map().setUserEmail(source.getUser().getUserEmail());
		map().setUserNm(source.getUser().getUserNm());
		using(ctx -> generateTruck( (Set<Truck>)ctx.getSource()) ).map(source.getTrucks(), destination.getTrucks());
		//using(ctx -> generateOrderTruckOwner( (Set<OrderTruckOwner>)ctx.getSource()) ).map(source.getOrders(), destination.getOrders());
	}

	private List<TruckDTO> generateTruck(Set<Truck> trucks) {
		List<TruckDTO> rtnList = new ArrayList<>();
		Iterator<Truck> it = trucks.iterator();
		while(it.hasNext()) {
			Truck t = it.next();
			TruckDTO dto = new TruckDTO();
			dto.setCarType(t.getTruckPK().getCarType());
			dto.setTonType(t.getTruckPK().getTonType());
			rtnList.add(dto);
		}
		return rtnList;
	}

}