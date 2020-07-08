package kr.co.teamfresh.cpft.admin.web.backend.controller.v1;

import java.util.Random;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baroservice.api.BarobillApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.RegistAndIssueBrokerTaxInvoice;
import kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.RegistAndIssueTaxInvoice;
import kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.RegistBrokerTaxInvoiceEX;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.CommonResult;
import kr.co.teamfresh.cpft.admin.web.backend.service.ResponseService;
import lombok.RequiredArgsConstructor;

@Api(tags = { "0. Barobill" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/barobill")
public class BaroController {

	Logger logger = LoggerFactory.getLogger(BaroController.class);

	private final ResponseService responseService;
	private final BarobillApiService barobillApiService;
	private final ModelMapper modelMapper;

	String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9"; // 인증키
	String corpNum = "5618801138"; // 연계사업자 사업자번호 ('-' 제외, 10자리)
	String checkCorpNumDumy = "1548801468"; // 확인할 사업자번호 ('-' 제외, 10자리)

	@Transactional
	@ApiOperation(value = "CheckCorpIsMember - 회원사 여부 확인", notes = "바로빌에 가입되어 있는 사업자인지 확인합니다.")
	@PostMapping(value = "/checkCorpIsMember")
	public CommonResult CheckCorpIsMember(
			@ApiParam(value = "확인할 사업자번호 ('-' 제외, 10자리)", required = true) @RequestParam(defaultValue = "1548801468") String checkCorpNum) {

		int result = barobillApiService.taxInvoice.checkCorpIsMember(certKey, corpNum, checkCorpNum);

		return responseService.getSingleResult(result);
	}

	@Transactional
	@ApiOperation(value = "RegistAndIssueTaxInvoice - 간편 발행", notes = "(세금)계산서의 '임시저장'과 '발행'을 한번에 처리합니다. (RegistTaxInvoice 함수 + IssueTaxInvoice 함수)\r\n"
			+ "발행을 위해서는 공급자의 공인인증서가 바로빌에 등록되어야 합니다.\r\n" + "발행이 완료된 (세금)계산서는 자동으로 국세청으로 전송됩니다.")
	@PostMapping(value = "/registAndIssueTaxInvoice")
	public CommonResult registAndIssueTaxInvoice(
			@ApiParam(value = "간편 발행 요청값", required = true) @RequestBody RegistAndIssueTaxInvoice registAndIssueTaxInvoice)
			throws JsonProcessingException {
		Random rnd = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 24; i++) {
			if (rnd.nextBoolean()) {
				buf.append((char) ((int) (rnd.nextInt(26)) + 97));
			} else {
				buf.append((rnd.nextInt(10)));
			}
		}

		String mgtKey = buf.toString();
		ObjectMapper mapper = new ObjectMapper();
		com.baroservice.ws.TaxInvoice requestInfo = modelMapper.map(registAndIssueTaxInvoice.getInvoice(),
				com.baroservice.ws.TaxInvoice.class);
		requestInfo.getInvoicerParty().setMgtNum(mgtKey);
		requestInfo.getInvoiceeParty().setMgtNum(mgtKey);
		requestInfo.setBrokerParty(null);
		requestInfo.setIssueDirection(1);
		requestInfo.setTaxInvoiceType(1);
		logger.info(mapper.writeValueAsString(requestInfo));

		int result = barobillApiService.taxInvoice.registAndIssueTaxInvoice(certKey, corpNum, requestInfo,
				registAndIssueTaxInvoice.isSendSms(), registAndIssueTaxInvoice.isForceIssue(),
				registAndIssueTaxInvoice.getMailTitle());

		if (result < 0) {
			String errResult = result + " " + getErrString(result);
			logger.error(mapper.writeValueAsString(errResult));
			logger.error(mapper.writeValueAsString(requestInfo));
			return responseService.getFailResult(result, errResult);
		}
		return responseService.getSingleResult(result);
	}

	@Transactional
	@ApiOperation(value = "RegistAndIssueBrokerTaxInvoice - 위수탁발행", notes = "위수탁 (세금)계산서의 '임시저장'과 '발행'을 한번에 처리합니다.발행을 위해서는 공급자의 공인인증서가 바로빌에 등록되어야 합니다. 발행이 완료된 (세금)계산서는 자동으로 국세청으로 전송됩니다. ")
	@PostMapping(value = "/registAndIssueBrokerTaxInvoice")
	public CommonResult registAndIssueBrokerTaxInvoice(
			@ApiParam(value = "위수탁발행 발행 요청값", required = true) @RequestBody RegistAndIssueBrokerTaxInvoice registAndIssueBrokerTaxInvoice)
			throws JsonProcessingException {
		Random rnd = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 24; i++) {
			if (rnd.nextBoolean()) {
				buf.append((char) ((int) (rnd.nextInt(26)) + 97));
			} else {
				buf.append((rnd.nextInt(10)));
			}
		}

		String mgtKey = buf.toString();
		ObjectMapper mapper = new ObjectMapper();

		com.baroservice.ws.TaxInvoice requestInfo = modelMapper.map(registAndIssueBrokerTaxInvoice.getInvoice(),
				com.baroservice.ws.TaxInvoice.class);

		requestInfo.getInvoicerParty().setMgtNum(mgtKey);
		requestInfo.getInvoiceeParty().setMgtNum(mgtKey);
		requestInfo.getBrokerParty().setMgtNum(mgtKey);

		int result = barobillApiService.taxInvoice.registAndIssueBrokerTaxInvoice(certKey, corpNum, requestInfo,
				registAndIssueBrokerTaxInvoice.isSendSMS(), registAndIssueBrokerTaxInvoice.isForceIssue(),
				registAndIssueBrokerTaxInvoice.getMailTitle());

		if (result < 0) {
			String errResult = result + " " + getErrString(result);
			logger.error(mapper.writeValueAsString(errResult));
			logger.error(mapper.writeValueAsString(requestInfo));
			return responseService.getFailResult(result, errResult);
		} else {
			com.baroservice.ws.TaxInvoice responseInfo = barobillApiService.taxInvoice.getTaxInvoice(certKey, corpNum,
					mgtKey);
			if (responseInfo.getTaxInvoiceType() < 0) {
				String errResult = result + " " + getErrString(result);
				return responseService.getFailResult(result, errResult);
			} else {
				String url = barobillApiService.taxInvoice.getTaxInvoicePopUpURL(certKey, corpNum, mgtKey, "timflabs",
						"Timf180525!");

				logger.info(mapper.writeValueAsString(url));
				logger.info(mapper.writeValueAsString(responseInfo));
				return responseService.getSingleResult(responseInfo);
			}
		}
		// return responseService.getSingleResult(result);
	}

	@Transactional
	@ApiOperation(value = "GetTaxInvoicePopUpURL - 세금계산서 URL 반환", notes = "(세금)계산서의 정보를 양식 형태로 볼 수 있는 URL을 반환합니다.\r\n" + 
			"반환된 URL은 60초까지만 유효합니다.")
	@PostMapping(value = "/getTaxInvoicePopUpURL")
	public CommonResult getTaxInvoicePopUpURL(
			@ApiParam(value = "연동사부여 문서키", required = true) @RequestParam(defaultValue = "7a7a2bg97o2w8oei93j5d18n") String mgtKey)
			throws JsonProcessingException {

		String url = barobillApiService.taxInvoice.getTaxInvoicePopUpURL(certKey, corpNum, mgtKey, "timflabs",
				"Timf180525!");

		ObjectMapper mapper = new ObjectMapper();
		int urlValue = 0;
	    try{
	    	urlValue = Integer.valueOf(url);
			String errResult = urlValue + " " + getErrString(urlValue);
			logger.error(mapper.writeValueAsString(errResult));
			logger.error(mgtKey);
			return responseService.getFailResult(urlValue, errResult);
	    } catch(NumberFormatException e) {
			return responseService.getSingleResult(url);
	    }
	}
	
	@Transactional
	@ApiOperation(value = "DeleteTaxInvoice - 회원사 여부 확인", notes = "(세금)계산서를 삭제합니다.\r\n'임시저장', '취소', '거부' 인 상태의 (세금)계산서만 삭제 가능합니다.\r\n삭제된 (세금)계산서는 '전자세금계산서 > 보관함 > 삭제 보관함' 에서 조회할 수 있습니다.\r\n")
	@PostMapping(value = "/deleteTaxInvoice")
	public CommonResult deleteTaxInvoice(
			@ApiParam(value = "연동사부여 문서키", required = true) @RequestParam(defaultValue = "ABC123111111111111111111") String mgtKey)
			throws JsonProcessingException {

		int result = barobillApiService.taxInvoice.deleteTaxInvoice(certKey, corpNum, mgtKey);

		ObjectMapper mapper = new ObjectMapper();
		if (result < 0) {
			String errResult = result + " " + getErrString(result);
			logger.error(mapper.writeValueAsString(errResult));
			logger.error(mgtKey);
			return responseService.getFailResult(result, errResult);
		}
		return responseService.getSingleResult(result);
	}

	@Transactional
	@ApiOperation(value = "RegistBrokerTaxInvoiceEX - 위수탁 (세금)계산서 임시저장", notes = "위수탁 (세금)계산서를 임시저장합니다. (승인 시 자동발행 옵션 추가)\r\n저장 후 바로빌 사이트의 '전자세금계산서 > 임시저장(연동)함' 에서 조회할 수 있습니다.")
	@PostMapping(value = "/registBrokerTaxInvoiceEX")
	public CommonResult registBrokerTaxInvoiceEX(
			@ApiParam(value = "위수탁발행 발행 요청값", required = true) @RequestBody RegistBrokerTaxInvoiceEX registBrokerTaxInvoiceEX)
			throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();

		logger.info(mapper.writeValueAsString(
				modelMapper.map(registBrokerTaxInvoiceEX.getInvoice(), com.baroservice.ws.TaxInvoice.class)));

		int result = barobillApiService.taxInvoice.registBrokerTaxInvoiceEX(certKey, corpNum,
				modelMapper.map(registBrokerTaxInvoiceEX.getInvoice(), com.baroservice.ws.TaxInvoice.class),
				registBrokerTaxInvoiceEX.getIssueTiming());

		if (result < 0) {
			String errResult = result + " " + getErrString(result);
			return responseService.getFailResult(result, errResult);
		}
		return responseService.getSingleResult(result);
	}

	@Transactional
	@ApiOperation(value = "RegistCorp - 바로빌 회원가입", notes = "연동사의 고객을 바로빌에 회원가입 합니다.")
	@PostMapping(value = "/registCorp")
	public CommonResult registCorp(
			@ApiParam(value = "사업자번호 ('-'제외)", required = true) @RequestParam(defaultValue = "888-88-78888") String CorpNum,
			@ApiParam(value = "회사명", required = true) @RequestParam(defaultValue = "테스트 회사용") String corpName,
			@ApiParam(value = "대표자명", required = true) @RequestParam(defaultValue = "서영락") String ceoName,
			@ApiParam(value = "업태", required = true) @RequestParam(defaultValue = "서비스") String bizType,
			@ApiParam(value = "업종", required = true) @RequestParam(defaultValue = "물류") String bizClass,
			@ApiParam(value = "우편번호", required = true) @RequestParam(defaultValue = "05637") String postNum,
			@ApiParam(value = "주소", required = true) @RequestParam(defaultValue = "서울 송파구 위례성대로 12길 15-1 (방이동)") String addr1,
			@ApiParam(value = "상세주소", required = true) @RequestParam(defaultValue = "2층") String addr2,
			@ApiParam(value = "담당자명", required = true) @RequestParam(defaultValue = "서영락") String memberName,
			@ApiParam(value = "담당자 아이디 (6~20 자)", required = true) @RequestParam(defaultValue = "timflabs") String id,
			@ApiParam(value = "담당자 비밀번호 (6~20 자)", required = true) @RequestParam(defaultValue = "Timf180525!") String pwd,
			@ApiParam(value = "담당자 전화번호", required = true) @RequestParam(defaultValue = "01066150136") String tel,
			@ApiParam(value = "담당자 이메일", required = true) @RequestParam(defaultValue = "youngrag.seo@timf.co.kr") String email)
			throws JsonProcessingException {

		int result = barobillApiService.bankAccount.registCorp(certKey, CorpNum, corpName, ceoName, bizType, bizClass,
				postNum, addr1, addr2, memberName, "", id, pwd, "", tel, "", email);

		ObjectMapper mapper = new ObjectMapper();
		if (result < 0) {
			String errResult = result + " " + getErrString(result);
			logger.error(mapper.writeValueAsString(errResult));
			return responseService.getFailResult(result, errResult);
		}
		return responseService.getSingleResult(result);
	}

	private String getErrString(int errCode) {
		return barobillApiService.taxInvoice.getErrString(certKey, errCode);
	}
}