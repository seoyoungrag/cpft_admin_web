package kr.co.teamfresh.cpft.admin.web.backend.repo.TruckOwner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.teamfresh.cpft.admin.web.backend.entity.TruckOwner;

public interface TruckOwnerJpaRepo extends JpaRepository<TruckOwner, String> {

	Page<TruckOwner> findAllByOrdersOrderTruckOwnerPKOrderOrderSeqOrderByUserUserSeq(String orderSeq, Pageable pageable);

	Page<TruckOwner> findAllByOrderByUserUserSeq(Pageable page);
	
	Page<TruckOwner> findAllByOrdersStatusAndOrdersOrderTruckOwnerPKOrderStatusOrderByUserUserSeq(String orderTruckOwnerStatus, String orderStatus,  Pageable page);

}
