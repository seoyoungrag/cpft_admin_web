package kr.co.teamfresh.cpft.admin.web.backend.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CAuthenticationEntryPointException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CResourceNotExistException;
import kr.co.teamfresh.cpft.admin.web.backend.config.security.JwtTokenProvider;
import kr.co.teamfresh.cpft.admin.web.backend.dto.carrier.CarrierDTO;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.SingleResult;
import kr.co.teamfresh.cpft.admin.web.backend.service.ResponseService;
import kr.co.teamfresh.cpft.admin.web.backend.service.carrier.CarrierService;
import lombok.RequiredArgsConstructor;

@Api(tags = { "6. Carrier" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/carrier")
public class CarrierController {
	Logger logger = LoggerFactory.getLogger(CarrierController.class);
	private final ResponseService responseService;
	private final CarrierService carrierService;
	private final JwtTokenProvider jwtTokenProvider;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "운송사 정보 조회", notes = "로그인 사용자의 운송사 정보를 조회한다.")
	@GetMapping
	public SingleResult<CarrierDTO> findCarrier(@RequestHeader("X-AUTH-TOKEN") String token) {
		try {
			//Map carrier = jwtTokenProvider.getCarrier(token);
			String carrier = jwtTokenProvider.getCarrierSeq(token);
			if (carrier != null) {

				return responseService.getSingleResult(carrierService.findCarrier(carrier));
			} else {
				logger.info("인증 토큰이 유효하지 않습니다." + token);
				throw new CAuthenticationEntryPointException("인증 토큰이 유효하지 않습니다.");
			}
		} catch (CResourceNotExistException e) {
			throw e;
		} catch (ExpiredJwtException e) {
			logger.error(e.getMessage());
			logger.error("인증 토큰이 유효하지 않습니다." + token);
			throw new CAuthenticationEntryPointException("인증 토큰이 유효하지 않습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error("오류가 발생했습니다." + token);
			throw e;
		}
	}
}
