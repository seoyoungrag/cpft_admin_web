package kr.co.teamfresh.cpft.admin.web.backend.repo.carrier;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.teamfresh.cpft.admin.web.backend.entity.Carrier;

public interface CarrierJpaRepo extends JpaRepository<Carrier, String> {

	Carrier findAllByCarrierSeq(String carrierSeq);
}