package kr.co.teamfresh.cpft.admin.web.backend.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CAuthenticationEntryPointException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CNotOwnerException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CPasswordNotMatchedException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CResourceNotExistException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserEmailDuplicatedException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserLoginIdDuplicatedException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserNotFoundException;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.CommonResult;
import kr.co.teamfresh.cpft.admin.web.backend.service.ResponseService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

	private final ResponseService responseService;

	private final MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult defaultException(HttpServletRequest request, Exception e) {
		// 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
		return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
	}

	@ExceptionHandler(CUserLoginIdDuplicatedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userLoginIdDuplicatedException(HttpServletRequest request, CUserLoginIdDuplicatedException e) {
		// 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
		return responseService.getFailResult(Integer.valueOf(getMessage("userLoginIdDuplicated.code")),
				getMessage("userLoginIdDuplicated.msg"));
	}
	@ExceptionHandler(CUserEmailDuplicatedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userEmailDuplicatedException(HttpServletRequest request, CUserEmailDuplicatedException e) {
		// 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
		return responseService.getFailResult(Integer.valueOf(getMessage("userEmailDuplicated.code")),
				getMessage("userEmailDuplicated.msg"));
	}
	@ExceptionHandler(CUserNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
		// 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
		return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")),
				getMessage("userNotFound.msg"));
	}

	@ExceptionHandler(CPasswordNotMatchedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult emailSigninFailed(HttpServletRequest request, CPasswordNotMatchedException e) {
		return responseService.getFailResult(Integer.valueOf(getMessage("passwordNotMatched.code")),
				getMessage("passwordNotMatched.msg"));
	}

	@ExceptionHandler(CAuthenticationEntryPointException.class)
	public CommonResult authenticationEntryPointException(HttpServletRequest request,
			CAuthenticationEntryPointException e) {
		return responseService.getFailResult(Integer.valueOf(getMessage("entryPointException.code")),
				getMessage("entryPointException.msg"));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public CommonResult AccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
		return responseService.getFailResult(Integer.valueOf(getMessage("accessDenied.code")),
				getMessage("accessDenied.msg"));
	}

	@ExceptionHandler(CNotOwnerException.class)
	@ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
	public CommonResult notOwnerException(HttpServletRequest request, CNotOwnerException e) {
		return responseService.getFailResult(Integer.valueOf(getMessage("notOwner.code")), getMessage("notOwner.msg"));
	}

	@ExceptionHandler(CResourceNotExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonResult resourceNotExistException(HttpServletRequest request, CResourceNotExistException e) {
		return responseService.getFailResult(Integer.valueOf(getMessage("resourceNotExist.code")),
				getMessage("resourceNotExist.msg"));
	}
// code정보에 해당하는 메시지를 조회합니다.
	private String getMessage(String code) {
		return getMessage(code, null);
	}

// code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
	private String getMessage(String code, Object[] args) {
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
}
