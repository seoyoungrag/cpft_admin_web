package kr.co.teamfresh.cpft.admin.web.backend.config.modelmap.mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.modelmapper.PropertyMap;

import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderTruckOwnerForOrderManageDTO;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.OrderTruckOwner;

public class OrderTruckOwnerEnToDtoForOrderManageMap extends PropertyMap<OrderTruckOwner, OrderTruckOwnerForOrderManageDTO> {
	@Override
	protected void configure() {
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

}