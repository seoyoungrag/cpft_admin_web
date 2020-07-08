package kr.co.teamfresh.cpft.admin.web.backend.service.code;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import kr.co.teamfresh.cpft.admin.web.backend.dto.code.CodeCtgryDTO;
import kr.co.teamfresh.cpft.admin.web.backend.entity.code.CodeCtgry;
import kr.co.teamfresh.cpft.admin.web.backend.repo.code.CodeCtgryJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.service.AbstractService;
import kr.co.teamfresh.cpft.admin.web.backend.util.ObjectMapperUtils;
import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CacheKey;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeService extends AbstractService{
	private final CodeCtgryJpaRepo codeCtgryJpaRepo;

	// 모든 카테고리와 코드 조회
	@Cacheable(value = CacheKey.CODE, unless = "#result == null")
	public List<CodeCtgryDTO> findAllCodes() {
		return ObjectMapperUtils.mapAll(codeCtgryJpaRepo.findAll(), CodeCtgryDTO.class);
	}
	// 모든 카테고리와 코드 조회
	@Cacheable(value = CacheKey.CODE, unless = "#result == null")
	public List<CodeCtgry> findAllByCodesCodeUseYn() {
		return codeCtgryJpaRepo.findAllByCodesCodeUseYn('Y');
	}
}
