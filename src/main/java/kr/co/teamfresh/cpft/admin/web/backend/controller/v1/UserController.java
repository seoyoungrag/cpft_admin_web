package kr.co.teamfresh.cpft.admin.web.backend.controller.v1;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserNotFoundException;
import kr.co.teamfresh.cpft.admin.web.backend.dto.user.UserDTO;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.CommonResult;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.ListResult;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.SingleResult;
import kr.co.teamfresh.cpft.admin.web.backend.repo.UserJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.service.ResponseService;
import kr.co.teamfresh.cpft.admin.web.backend.util.ObjectMapperUtils;
import kr.co.teamfresh.cpft.admin.web.backend.entity.User;
import lombok.RequiredArgsConstructor;

@Api(tags = { "2. User" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

	private final UserJpaRepo userJpaRepo;
	private final ResponseService responseService; // 결과를 처리할 Service

	@Secured("ROLE_USER")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
	@GetMapping(value = "/users")
	public ListResult<User> findAllUser() {
// 결과데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
		return responseService.getListResult(userJpaRepo.findAll());
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
	@GetMapping(value = "/user")
	public SingleResult<User> findUser() {
// SecurityContext에서 인증받은 회원의 정보를 얻어온다.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
// 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
		return responseService.getSingleResult(userJpaRepo.findByUserLoginId(id).orElseThrow(CUserNotFoundException::new));
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
	@PutMapping(value = "/user")
	public SingleResult<User> modify(@ApiParam(value = "회원번호", required = true) @RequestParam String msrl,
			@ApiParam(value = "회원이름", required = true) @RequestParam String name) {
		User user = User.builder().userSeq(msrl).userNm(name).build();
		return responseService.getSingleResult(userJpaRepo.save(user));
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
	@DeleteMapping(value = "/user/{msrl}")
	public CommonResult delete(@ApiParam(value = "회원번호", required = true) @PathVariable String msrl) {
		userJpaRepo.deleteById(msrl);
// 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
		return responseService.getSuccessResult();
	}
	

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "권한별 사용자 조회", notes = "권한별 사용자들을 조회한다.")
	@GetMapping(value = "/user/role/{role}")
	public ListResult<UserDTO> findAllUserByRole(@PathVariable String role) {
		return responseService.getListResult(ObjectMapperUtils.mapAll(userJpaRepo.findAllByUserRolesRolePKRole(role), UserDTO.class));
	}
}