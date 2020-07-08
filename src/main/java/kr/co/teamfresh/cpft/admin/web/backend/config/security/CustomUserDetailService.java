package kr.co.teamfresh.cpft.admin.web.backend.config.security;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserNotFoundException;
import kr.co.teamfresh.cpft.admin.web.backend.repo.UserJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CacheKey;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UserJpaRepo userJpaRepo;

	@Cacheable(value = CacheKey.USER, key = "#userPk", unless = "#result == null")
	public UserDetails loadUserByUsername(String userPk) {
		return userJpaRepo.findById(userPk).orElseThrow(CUserNotFoundException::new);
	}
}