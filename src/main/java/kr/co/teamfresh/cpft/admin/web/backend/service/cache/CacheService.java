package kr.co.teamfresh.cpft.admin.web.backend.service.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CacheKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CacheService {

	@Caching(evict = { @CacheEvict(value = CacheKey.POST, key = "#postId"),
			@CacheEvict(value = CacheKey.POSTS, key = "#boardName") })
	public boolean deleteBoardCache(long postId, String boardName) {
		log.debug("deleteBoardCache - postId {}, boardName {}", postId, boardName);
		return true;
	}
}