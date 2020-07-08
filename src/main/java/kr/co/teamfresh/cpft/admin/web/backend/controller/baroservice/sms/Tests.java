package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.sms;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfSMSMessage;
import com.baroservice.ws.ArrayOfString;
import com.baroservice.ws.ArrayOfXMSMessage;
import com.baroservice.ws.SMSMessage;
import com.baroservice.ws.XMSMessage;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * 바로빌 문자 API 샘플
 */
public class Tests {

	/**
	 * 바로빌 API 정의 클래스
	 * <p>
	 * 환경에 따라 BarobillApiProfile 를 지정해주세요.
	 */
	private BarobillApiService barobillApiService;

	public Tests() throws MalformedURLException {
		barobillApiService = new BarobillApiService(BarobillApiProfile.TESTBED);
	}

	/**
	 * SendMessage - 문자(SMS/LMS) 발송
	 */
	@Test
	public void SendMessage() throws RemoteException {

		String certKey = "";            // 인증키
		String corpNum = "";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String senderId = "";            // 연계사업자 담당자 아이디
		String fromNumber = "";            // 발신번호
		String toName = "";                // 수신자명
		String toNumber = "";            // 수신번호
		String contents = "";            // 내용
		String sendDt = "";                // 전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)
		String refKey = "";                // 연동사부여 전송키

		String result = barobillApiService.sms.sendMessage(certKey, corpNum, senderId, fromNumber, toName, toNumber, contents, sendDt, refKey);

		System.out.println(result);

	}

	/**
	 * SendSMSMessage - 단문(SMS) 발송
	 */
	@Test
	public void SendSMSMessage() throws RemoteException {

		String certKey = "";            // 인증키
		String corpNum = "";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String senderId = "";        // 연계사업자 담당자 아이디
		String fromNumber = "";        // 발신번호
		String toName = "";            // 수신자명
		String toNumber = "";        // 수신번호
		String contents = "";        // 내용
		String sendDt = "";            // 전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)
		String refKey = "";            // 연동사부여 전송키

		String result = barobillApiService.sms.sendSMSMessage(certKey, corpNum, senderId, fromNumber, toName, toNumber, contents, sendDt, refKey);

		System.out.println(result);

	}

	/**
	 * SendLMSMessage - 장문(LMS) 발송
	 */
	@Test
	public void SendLMSMessage() throws RemoteException {

		String certKey = "";            // 인증키
		String corpNum = "";         // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String senderId = "";        // 연계사업자 담당자 아이디
		String fromNumber = "";      // 발신번호
		String toName = "";          // 수신자명
		String toNumber = "";        // 수신번호
		String subject = "";         // 제목
		String contents = "";        // 내용
		String sendDt = "";          // 전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)
		String refKey = "";          // 연동사부여 전송키

		String result = barobillApiService.sms.sendLMSMessage(certKey, corpNum, senderId, fromNumber, toName, toNumber, subject, contents, sendDt, refKey);

		System.out.println(result);

	}

	/**
	 * SendMMSMessageFromFTP - 포토(MMS) 발송 (FTP)
	 */
	@Test
	public void SendMMSMessageFromFTP() throws RemoteException {

		String certKey = "";           // 인증키
		String corpNum = "";        // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String senderId = "";       // 연계사업자 담당자 아이디
		String fromNumber = "";     // 발신번호
		String toName = "";         // 수신자명
		String toNumber = "";       // 수신번호
		String subject = "";        // 제목
		String contents = "";       // 내용
		String fileName = "";       // 전송할 파일명
		String sendDt = "";         // 전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)
		String refKey = "";         // 연동사부여 전송키

		String result = barobillApiService.sms.sendMMSMessageFromFTP(certKey, corpNum, senderId, fromNumber, toName, toNumber, subject, contents, fileName, sendDt, refKey);

		System.out.println(result);

	}

	/**
	 * SendMessages - 다량전송
	 */
	@Test
	public void SendMessages() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String senderId = "";           //연계사업자 담당자 아이디
		int sendCount = 2;                //전송건수
		boolean cutToSms = false;        //90 byte 보다 긴 내용의 문자인 경우 SMS 로 잘라서 전송할지 여부

		ArrayOfXMSMessage messages = new ArrayOfXMSMessage();        //메세지 정보 배열
		messages.getXMSMessage().add(new XMSMessage());
		messages.getXMSMessage().get(0).setMessage("");
		messages.getXMSMessage().get(0).setSenderNum("");
		messages.getXMSMessage().get(0).setReceiverNum("");
		messages.getXMSMessage().get(0).setReceiverName("");
		messages.getXMSMessage().add(new XMSMessage());
		messages.getXMSMessage().get(1).setMessage("");
		messages.getXMSMessage().get(1).setSenderNum("");
		messages.getXMSMessage().get(1).setReceiverNum("");
		messages.getXMSMessage().get(1).setReceiverName("");

		String sendDt = "";             //전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)

		String result = barobillApiService.sms.sendMessages(certKey, corpNum, senderId, sendCount, cutToSms, messages, sendDt);

		System.out.println(result);

	}

	/**
	 * CancelReservedSMSMessage - 예약 전송취소
	 */
	@Test
	public void CancelReservedSMSMessage() throws RemoteException {
		String certKey = "";           // 인증키
		String corpNum = "";        // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String sendKey = "";        // 바로빌부여 전송키

		int result = barobillApiService.sms.cancelReservedSMSMessage(certKey, corpNum, sendKey);

		System.out.println(result);
	}

	/**
	 * GetSMSSendState - 문자 전송 상태 (바로빌부여 전송키)
	 */
	@Test
	public void GetSMSSendState() throws RemoteException {
		String certKey = "";           // 인증키
		String corpNum = "";           // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String sendKey = "";           // 바로빌부여 전송키

		int result = barobillApiService.sms.getSMSSendState(certKey, corpNum, sendKey);

		System.out.println(result);

	}

	/**
	 * GetSMSSendMessage - 문자 메시지 확인 (바로빌부여 전송키)
	 */
	@Test
	public void GetSMSSendMessage() throws RemoteException {

		String certKey = "";           // 인증키
		String corpNum = "";           // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String sendKey = "";           // 바로빌부여 전송키

		SMSMessage result = barobillApiService.sms.getSMSSendMessage(certKey, corpNum, sendKey);

		System.out.println(result);

	}

	/**
	 * GetSMSSendMessages - 문자 메시지 대량 확인 (바로빌부여 전송키)
	 */
	@Test
	public void GetSMSSendMessages() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)

		ArrayOfString sendKeyList = new ArrayOfString();    //바로빌부여 전송키 배열
		sendKeyList.getString().add("");
		sendKeyList.getString().add("");

		ArrayOfSMSMessage result = barobillApiService.sms.getSMSSendMessages(certKey, corpNum, sendKeyList);

		System.out.println(result);

	}

	/**
	 * GetSMSSendMessagesByRefKey - 문자 메시지 확인 (연동사부여 전송키)
	 */
	@Test
	public void GetSMSSendMessagesByRefKey() throws RemoteException {

		String certKey = "";            // 인증키
		String corpNum = "";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String refKey = "";            // 연동사부여 전송키

		ArrayOfSMSMessage result = barobillApiService.sms.getSMSSendMessagesByRefKey(certKey, corpNum, refKey);

		System.out.println(result);

	}

	/**
	 * GetMessagesByReceiptNum - 다량전송 내역 확인 (바로빌 다량전송키)
	 */
	@Test
	public void GetMessagesByReceiptNum() throws RemoteException {

		String certKey = "";            // 인증키
		String corpNum = "";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String receiptNum = "";         // 바로빌부여 다량전송키

		ArrayOfSMSMessage result = barobillApiService.sms.getMessagesByReceiptNum(certKey, corpNum, receiptNum);

		System.out.println(result);

	}

	/**
	 * GetSMSHistoryURL - 문자 전송내역 URL
	 */
	@Test
	public void GetSMSHistoryURL() throws RemoteException {

		String certKey = "";        // 인증키
		String corpNum = "";        // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String id = "";             // 연계사업자 담당자 아이디
		String pwd = "";            // 연계사업자 담당자 비밀번호

		String result = barobillApiService.sms.getSMSHistoryURL(certKey, corpNum, id, pwd);

		System.out.println(result);

	}

}
