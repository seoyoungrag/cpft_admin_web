package kr.co.teamfresh.cpft.admin.web.backend.controller.v1;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CPasswordNotMatchedException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserEmailDuplicatedException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserLoginIdDuplicatedException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserNotFoundException;
import kr.co.teamfresh.cpft.admin.web.backend.config.security.JwtTokenProvider;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.CommonResult;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.SingleResult;
import kr.co.teamfresh.cpft.admin.web.backend.repo.UserJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.service.ResponseService;
import kr.co.teamfresh.cpft.admin.web.backend.util.MailUtil;
import kr.co.teamfresh.cpft.admin.web.backend.entity.User;
import lombok.RequiredArgsConstructor;

@Api(tags = { "1. Sign" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

	private final UserJpaRepo userJpaRepo;
	private final JwtTokenProvider jwtTokenProvider;
	private final ResponseService responseService;
	private final PasswordEncoder passwordEncoder;
	//@PersistenceContext
	private final EntityManager entityManager;
	private final MailUtil mailUtil;

	@ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
	@PostMapping(value = "/signin")
	public SingleResult<String> signin(@ApiParam(value = "회원ID", required = true) @RequestParam String id,
			@ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
		User user = userJpaRepo.findByUserLoginId(id).orElseThrow(CUserNotFoundException::new);
		if (!passwordEncoder.matches(password, user.getPassword()))
			throw new CPasswordNotMatchedException();

		return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getUserSeq()), user));

	}

	@ApiOperation(value = "가입", notes = "회원가입을 한다.")
	@PostMapping(value = "/signup")
	public CommonResult signin(@ApiParam(value = "회원ID", required = true) @RequestParam String id,
			@ApiParam(value = "비밀번호", required = true) @RequestParam String password,
			@ApiParam(value = "이름", required = true) @RequestParam String name,
			@ApiParam(value = "이메일(비밀번호 초기화에 사용)", required = true) @RequestParam String email) {

		Optional<User> userLoginIdCheck = userJpaRepo.findByUserLoginId(id);
		if(userLoginIdCheck.isPresent()) {
			throw new CUserLoginIdDuplicatedException();
		}
		Optional<User> userEmailCheck = userJpaRepo.findByUserEmail(email);
		if(userEmailCheck.isPresent()) {
			throw new CUserEmailDuplicatedException();
		}
		userJpaRepo.save(User.builder().userLoginId(id).userLoginPw(passwordEncoder.encode(password)).userNm(name).userEmail(email)
				//.roles(Collections.singletonList("ROLE_USER")).build());
				.roles(Arrays.asList(new String[]{"ROLE_USER", "CARRIER_USER"})).build());
				//.roles(Arrays.asList(new UserRole[]{new UserRole() {{ setUserRolePK(new UserRolePK() {{setRole("ROLE_USER")}} }}null, "ROLE_USER"), new UserRole(null, "CARRIER_USER")})).build());

		User user = userJpaRepo.findByUserLoginId(id).orElseThrow(CPasswordNotMatchedException::new);
		return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getUserSeq()), user));
	}

	@Transactional
	@ApiOperation(value = "비밀번호 초기화", notes = "회원정보의 이메일 주소로 초기화된 비밀번호를 전송한다.")
	@PostMapping(value = "/passwordRecovery")
	public CommonResult signin(
			@ApiParam(value = "이메일", required = true) @RequestParam String email) {

		Optional<User> userEmailCheck = userJpaRepo.findByUserEmail(email);
		if(!userEmailCheck.isPresent()) {
			throw new CUserNotFoundException();
		}
		try {
			String password = randomPw();
			String encodedPwd = passwordEncoder.encode(password);
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaUpdate<User> cq = cb.createCriteriaUpdate(User.class);
			Root<User> user = cq.from(User.class);
			//cq.set(user.get(User_.userLoginPw), encodedPwd);
			//cq.where(cb.equal(user.get(User_.userSeq), userEmailCheck.get().getUserSeq()));
			entityManager.createQuery(cq).executeUpdate();
			Map<String, Object> inOutMap = new HashMap<String, Object>();
			inOutMap.put("userLoginId",userEmailCheck.get().getUserLoginId());
			inOutMap.put("userLoginPw",password);
			mailUtil.gmailSend(inOutMap, email);
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value = "로그아웃", notes = "회원 로그아웃을 한다.")
	@PostMapping(value = "/logout")
	public CommonResult logout() {
		return responseService.getSuccessResult();
	}


	public static String randomPw() {
		char pwCollection[] = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E',
				'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
				'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')' };// 배열에 선언

		String ranPw = "";

		for (int i = 0; i < 10; i++) {
			int selectRandomPw = (int) (Math.random() * (pwCollection.length));
			ranPw += pwCollection[selectRandomPw];
		}
		return ranPw;
	}

}