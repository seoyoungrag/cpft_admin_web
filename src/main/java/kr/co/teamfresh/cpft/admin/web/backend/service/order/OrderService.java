package kr.co.teamfresh.cpft.admin.web.backend.service.order;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderDTO;
import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderTruckOwnerForApplicationListDTO;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.PageReqRes;
import kr.co.teamfresh.cpft.admin.web.backend.repo.order.OrderJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.repo.orderTruckOwner.OrderTruckOwnerJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.util.ObjectMapperUtils;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.Order;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.OrderTruckOwner;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderJpaRepo orderJpaRepo;
	private final OrderTruckOwnerJpaRepo orderTruckOwnerJpaRepo;
	
	public OrderDTO saveOrder(Order order) {
		//User user = userJpaRepo.findById(uid).orElseThrow(CUserNotFoundException::new);		map().getWorkGroup().getOrders().add(destination);
		if(order.getOrderSeq()!=null && !order.getOrderSeq().equals("")) {
			//Order oriOrder = Optional.ofNullable(orderJpaRepo.getOne(order.getOrderSeq())).orElseThrow(CResourceNotExistException::new);
			//ObjectMapperUtils.map(order, oriOrder);
			//return orderJpaRepo.save(oriOrder);
			return ObjectMapperUtils.map(orderJpaRepo.save(order), OrderDTO.class);
		}else {
			return ObjectMapperUtils.map(orderJpaRepo.save(order), OrderDTO.class);
		}
	}

	public List<OrderDTO> findAllOrders() {
		return ObjectMapperUtils.mapAll(orderJpaRepo.findAll(), OrderDTO.class);
	}

	public List<OrderDTO> findAllByCarrierSeqAndStatus(String carrierSeq, String status) {
		return ObjectMapperUtils.mapAll(orderJpaRepo.findAllByCarrierCarrierSeqAndStatusOrderByOrderSeq(carrierSeq, status), OrderDTO.class);
	}
	
	public List<OrderDTO> findAllByCarrierSeq(String carrierSeq) {
		return ObjectMapperUtils.mapAll(orderJpaRepo.findAllByCarrierCarrierSeqOrderByOrderSeq(carrierSeq), OrderDTO.class);
	}

	/*
	public Page<Order> listOrderByCarrierIdAndOrderSeq(String carrierSeq, String orderSeq, PageReqRes<OrderDTO> pageable) {
		//return ObjectMapperUtils.mapAll(orderJpaRepo.findAllByCarrierCarrierSeqAndOrderSeqOrderByOrderSeq(carrierSeq, orderSeq, pageable), OrderDTO.class);
		return orderJpaRepo.findAllByCarrierCarrierSeqAndOrderSeqOrderByOrderSeq(carrierSeq, orderSeq, pageable);
	}
	*/

	public Page<OrderTruckOwner> listOrderTruckOwnerByStatusOrderByCreatedAt(String status, PageReqRes<OrderTruckOwner, OrderTruckOwnerForApplicationListDTO> pageable) {
		return orderTruckOwnerJpaRepo.findAllByOrderTruckOwnerPKOrderStatusOrderByCreatedAtAscOrderSeqAscUserSeqAsc(status, pageable);
	}

	public Page<OrderTruckOwner> listOrderTruckOwnerByOrderSeqOrderAndStatusByCreatedAt(String status, String orderSeq, PageReqRes<OrderTruckOwner, OrderTruckOwnerForApplicationListDTO> pageable) {
		return orderTruckOwnerJpaRepo.findAllByOrderTruckOwnerPKOrderStatusAndOrderTruckOwnerPKOrderOrderSeqOrderByCreatedAtAscOrderSeqAscUserSeqAsc(status, orderSeq, pageable);
	}
	
}
