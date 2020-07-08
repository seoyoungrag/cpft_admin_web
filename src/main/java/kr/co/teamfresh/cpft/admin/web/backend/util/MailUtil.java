package kr.co.teamfresh.cpft.admin.web.backend.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.text.ParseException;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

	@Autowired
	private JavaMailSender javaMailSender;

	public int gmailSend(Map<String, Object> inOutMap, String sendEmail)
			throws ParseException, InvalidKeyException, UnsupportedEncodingException, AddressException, MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendEmail));

		message.setSubject(MimeUtility.encodeText("[용차블루] 임시비밀번호 발송", "UTF-8","B"));

		StringBuffer sb = new StringBuffer();

		sb.append("<div>");
		sb.append("<p>안녕하세요</p>");
		sb.append("<p>용차블루_운송사시스템입니다. </p>");
		sb.append("<br>");
		sb.append("<p> 사용자아이디: " + String.valueOf(inOutMap.get("userLoginId")) + " </p>");
		sb.append("<p> 임시비밀번호: " + String.valueOf(inOutMap.get("userLoginPw")) + " </p>");
		sb.append("<br>");
		sb.append("<p>문의사항은 아래 연락처로 문의 바랍니다.</p>");
		sb.append("<br>");
		sb.append("<p>팀프랩스 : 02-423-0525</p>");
		sb.append("<br>");
		sb.append("<p>감사합니다. </p>");
		sb.append("<br>");
		sb.append("</div>");

		message.setText(sb.toString(), "UTF-8", "html" );

		javaMailSender.send(message);

		return 0;
	}

	public static boolean isNumeric(Object o) {
		try {
			Double.parseDouble(o.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
