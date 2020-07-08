package kr.co.teamfresh.cpft.admin.web.backend.service.carrier;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kr.co.teamfresh.cpft.admin.web.backend.dto.carrier.CarrierDTO;
import kr.co.teamfresh.cpft.admin.web.backend.repo.carrier.CarrierJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.service.AbstractService;
import kr.co.teamfresh.cpft.admin.web.backend.util.ObjectMapperUtils;
import kr.co.teamfresh.cpft.admin.web.backend.entity.Carrier;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CarrierService  extends AbstractService{

	private final CarrierJpaRepo carrierJpaRepo;
	
	//@Cacheable(value = CacheKey.CARRIER, key = "#carrierSeq", unless = "#result == null")
	public CarrierDTO findCarrier(String carrierSeq) {
		return ObjectMapperUtils.map(carrierJpaRepo.findAllByCarrierSeq(carrierSeq), CarrierDTO.class);
	}
	
	//@Cacheable(value = CacheKey.CARRIER, key = "#carrierSeq", unless = "#result == null")
	public Carrier findCarrierForCache(String carrierSeq) {
		//Carrier carrier = carrierJpaRepo.findById(carrierSeq).orElseThrow(CResourceNotExistException::new);
		return carrierJpaRepo.findAllByCarrierSeq(carrierSeq);
	}
}

