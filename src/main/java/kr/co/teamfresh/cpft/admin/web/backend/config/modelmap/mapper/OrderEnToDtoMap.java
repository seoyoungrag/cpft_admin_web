package kr.co.teamfresh.cpft.admin.web.backend.config.modelmap.mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.modelmapper.PropertyMap;

import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderDTO;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.Order;

public class OrderEnToDtoMap extends PropertyMap<Order, OrderDTO> {
	@Override
	protected void configure() {
		map().setCarrierSeq(source.getCarrier().getCarrierSeq());
		map().setCarrierNm(source.getCarrier().getCarrierNm());
		map().setUserSeq(source.getUser().getUserSeq());
		map().setUserNm(source.getUser().getUserNm());
		map().setWorkGroupNm(source.getWorkGroupManager().getWorkGroupManagerPK().getWorkGroupNm());
		map().setWorkGroupManager(source.getWorkGroupManager().getWorkGroupManagerPK().getWorkGroupManagerNm());
		using(ctx -> generateDate( ((Order)ctx.getSource()).getCreatedAt() )).map(source, destination.getCreatedAt());
		using(ctx -> generateDate( ((Order)ctx.getSource()).getModifiedAt() )).map(source, destination.getModifiedAt());
	}

	private LocalDate generateDate(LocalDateTime createdAt) {
		if(createdAt==null) {
			return Instant.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
		}else {
		return createdAt.toInstant(ZoneOffset.UTC).atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
		}
	}
}