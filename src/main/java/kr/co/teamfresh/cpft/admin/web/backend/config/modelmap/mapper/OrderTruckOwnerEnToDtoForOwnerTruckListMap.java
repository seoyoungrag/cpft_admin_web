package kr.co.teamfresh.cpft.admin.web.backend.config.modelmap.mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.modelmapper.PropertyMap;

import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderTruckOwnerForTruckOwnerListDTO;
import kr.co.teamfresh.cpft.admin.web.backend.dto.truckOwner.TruckDTO;
import kr.co.teamfresh.cpft.admin.web.backend.entity.Truck;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.OrderTruckOwner;

public class OrderTruckOwnerEnToDtoForOwnerTruckListMap extends PropertyMap<OrderTruckOwner, OrderTruckOwnerForTruckOwnerListDTO> {
	@Override
	protected void configure() {
		map(source.getOrderTruckOwnerPK().getOrder()).setOrder(null);
		using(ctx -> generateDate(((OrderTruckOwner) ctx.getSource()).getCreatedAt())).map(source,
				destination.getCreatedAt());
		using(ctx -> generateDate(((OrderTruckOwner) ctx.getSource()).getModifiedAt())).map(source,
				destination.getModifiedAt());
	}

	private LocalDate generateDate(LocalDateTime createdAt) {
		if (createdAt == null) {
			return Instant.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
		} else {
			return createdAt.toInstant(ZoneOffset.UTC).atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
		}
	}

	private List<TruckDTO> generateTruck(Set<Truck> trucks) {
		List<TruckDTO> rtnList = new ArrayList<>();
		Iterator<Truck> it = trucks.iterator();
		while (it.hasNext()) {
			Truck t = it.next();
			TruckDTO dto = new TruckDTO();
			dto.setCarType(t.getTruckPK().getCarType());
			dto.setTonType(t.getTruckPK().getTonType());
			rtnList.add(dto);
		}
		rtnList.sort(new Comparator<TruckDTO>() {
			@Override
			public int compare(TruckDTO arg0, TruckDTO arg1) {
				// TODO Auto-generated method stub
				int carType0 = Integer.parseInt(arg0.getCarType());
				int carType1 = Integer.parseInt(arg1.getCarType());
				int tonType0 = Integer.parseInt(arg0.getTonType());
				int tonType1 = Integer.parseInt(arg1.getTonType());
				
				if (carType0 == carType1 && tonType0 == tonType1)
					return 0;
				else if (carType0 > carType1) {
					return 1;
				}else
					return -1;
			}
		});
		return rtnList;
	}

}