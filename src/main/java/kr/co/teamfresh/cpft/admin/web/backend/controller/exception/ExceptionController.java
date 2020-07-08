package kr.co.teamfresh.cpft.admin.web.backend.controller.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CAuthenticationEntryPointException;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.CommonResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {
	@GetMapping(value = "/entrypoint")
	public CommonResult entrypointException() {
		throw new CAuthenticationEntryPointException();
	}

	@GetMapping(value = "/accessdenied")
	public CommonResult accessdeniedException() {
		throw new AccessDeniedException("");
	}
}