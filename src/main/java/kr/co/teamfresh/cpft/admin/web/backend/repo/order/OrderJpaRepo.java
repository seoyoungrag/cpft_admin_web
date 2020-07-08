package kr.co.teamfresh.cpft.admin.web.backend.repo.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.teamfresh.cpft.admin.web.backend.entity.order.Order;

public interface OrderJpaRepo extends JpaRepository<Order, String> {

	List<Order> findAllByCarrierCarrierSeqOrderByOrderSeq(String carrierSeq);
	List<Order> findAllByCarrierCarrierSeqAndStatusOrderByOrderSeq(String carrierSeq, String status);
	Page<Order> findAllByCarrierCarrierSeqAndOrderSeqOrderByOrderSeq(String carrierSeq, String orderSeq, Pageable pageable);

}
