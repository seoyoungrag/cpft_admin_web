package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.fax;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfFaxMessage;
import com.baroservice.ws.ArrayOfFaxMessageEx;
import com.baroservice.ws.ArrayOfString;
import com.baroservice.ws.FaxMessage;
import com.baroservice.ws.FaxMessageEx;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * 바로빌 팩스 API 샘플
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
	 * SendFaxFromFTP - 전송 (단일파일)
	 */
	@Test
	public void SendFaxFromFTP() throws RemoteException {

		String certKey = "";        // 인증키
		String corpNum = "";        // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String senderId = "";       // 연계사업자 담당자 아이디
		String fileName = "";       // 전송할 파일명
		String fromNumber = "";     // 발신번호
		String toNumber = "";       // 수신번호
		String toCorpName = "";     // 수신자 회사명
		String toName = "";         // 수신자명
		String sendDT = "";         // 전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)
		String refKey = "";         // 연동사부여 전송키

		String result = barobillApiService.fax.sendFaxFromFTP(certKey, corpNum, senderId, fileName, fromNumber, toNumber, toCorpName, toName, sendDT, refKey);

		System.out.println(result);

	}

	/**
	 * SendFaxFromFTPEx - 전송 (다량파일)
	 */
	@Test
	public void SendFaxFromFTPEx() throws RemoteException {

		String certKey = "";        //인증키
		String corpNum = "";        //연계사업자 사업자번호 ('-' 제외, 10자리)
		String senderId = "";       //연계사업자 담당자 아이디
		int fileCount = 2;        //전송할 파일수

		ArrayOfString fileNames = new ArrayOfString();        //전송할 파일명
		fileNames.getString().add("");
		fileNames.getString().add("");

		String fromNumber = "";        //발신번호
		String toNumber = "";        //수신번호
		String toCorpName = "";        //수신자 회사명
		String toName = "";            //수신자명
		String sendDt = "";            //전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)
		String refKey = "";            //연동사부여 전송키

		String result = barobillApiService.fax.sendFaxFromFTPEx(certKey, corpNum, senderId, fileCount, fileNames, fromNumber, toNumber, toCorpName, toName, sendDt, refKey);

		System.out.println(result);

	}

	/**
	 * SendFaxesFromFTP - 다량전송 (단일파일)
	 */
	@Test
	public void SendFaxesFromFTP() throws RemoteException {

		String certKey = "";        //인증키
		String corpNum = "";        //연계사업자 사업자번호 ('-' 제외, 10자리)
		String senderId = "";       //연계사업자 담당자 아이디
		String fileName = "";       //전송할 파일명
		int sendCount = 2;        //전송건수
		ArrayOfFaxMessage messages = new ArrayOfFaxMessage();  //팩스 정보
		messages.getFaxMessage().add(new FaxMessage());
		messages.getFaxMessage().get(0).setSenderNum("");
		messages.getFaxMessage().get(0).setReceiverNum("");
		messages.getFaxMessage().get(0).setReceiveCorp("");
		messages.getFaxMessage().get(0).setReceiverName("");
		messages.getFaxMessage().add(new FaxMessage());
		messages.getFaxMessage().get(1).setSenderNum("");
		messages.getFaxMessage().get(1).setReceiverNum("");
		messages.getFaxMessage().get(1).setReceiveCorp("");
		messages.getFaxMessage().get(1).setReceiverName("");

		String sendDt = "";         //전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)

		ArrayOfString result = barobillApiService.fax.sendFaxesFromFTP(certKey, corpNum, senderId, fileName, sendCount, messages, sendDt);

		System.out.println(result);

	}

	/**
	 * SendFaxesFromFTPEx - 다량전송 (다량파일)
	 */
	@Test
	public void SendFaxesFromFTPEx() throws RemoteException {

		String certKey = "";        //인증키
		String corpNum = "";        //연계사업자 사업자번호 ('-' 제외, 10자리)
		String senderId = "";       //연계사업자 담당자 아이디

		int fileCount = 2;        //전송할 파일수
		ArrayOfString fileNames = new ArrayOfString();            //전송할 파일명
		fileNames.getString().add("");
		fileNames.getString().add("");

		int sendCount = 2;        //전송건수
		ArrayOfFaxMessage messages = new ArrayOfFaxMessage();  //팩스 정보
		messages.getFaxMessage().add(new FaxMessage());
		messages.getFaxMessage().get(0).setSenderNum("");
		messages.getFaxMessage().get(0).setReceiverNum("");
		messages.getFaxMessage().get(0).setReceiveCorp("");
		messages.getFaxMessage().get(0).setReceiverName("");
		messages.getFaxMessage().add(new FaxMessage());
		messages.getFaxMessage().get(1).setSenderNum("");
		messages.getFaxMessage().get(1).setReceiverNum("");
		messages.getFaxMessage().get(1).setReceiveCorp("");
		messages.getFaxMessage().get(1).setReceiverName("");

		String sendDt = "";         //전송일시 (yyyyMMddHHmmss 형식) (빈 문자열 입력시 즉시 전송, 미래일자 입력시 예약 전송)

		ArrayOfString result = barobillApiService.fax.sendFaxesFromFTPEx(certKey, corpNum, senderId, fileCount, fileNames, sendCount, messages, sendDt);

		System.out.println(result);

	}

	/**
	 * CancelReservedFaxMessage - 예약 전송취소
	 */
	@Test
	public void CancelReservedFaxMessage() throws RemoteException {

		String certKey = "";            // 인증키
		String corpNum = "";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String sendKey = "";            // 바로빌부여 전송키

		int result = barobillApiService.fax.cancelReservedFaxMessage(certKey, corpNum, corpNum);

		System.out.println(result);

	}

	/**
	 * GetFaxSendState - 팩스 전송 상태 (바로빌부여 전송키)
	 */
	@Test
	public void GetFaxSendState() throws RemoteException {

		String certKey = "";            //  인증키
		String corpNum = "";         //  바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String sendKey = "";         //  바로빌부여 전송키

		int result = barobillApiService.fax.getFaxSendState(certKey, corpNum, sendKey);

		System.out.println(result);

	}

	/**
	 * GetFaxMessage - 팩스 확인A (바로빌부여 전송키)
	 */
	@Test
	public void GetFaxMessage() throws RemoteException {

		String certKey = "";        // 인증키
		String corpNum = "";        // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String sendKey = "";        // 바로빌부여 전송키

		FaxMessage result = barobillApiService.fax.getFaxMessage(certKey, corpNum, sendKey);

		System.out.println(result);

	}

	/**
	 * GetFaxMessageEx - 팩스 확인B (바로빌부여 전송키)
	 */
	@Test
	public void GetFaxMessageEx() throws RemoteException {

		String certKey = "";        // 인증키
		String corpNum = "";        // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String sendKey = "";        // 바로빌부여 전송키

		FaxMessageEx result = barobillApiService.fax.getFaxMessageEx(certKey, corpNum, sendKey);

		System.out.println(result);

	}

	/**
	 * GetFaxSendMessages - 팩스 대량확인A (바로빌부여 전송키)
	 */
	@Test
	public void GetFaxSendMessages() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)

		ArrayOfString sendKeyList = new ArrayOfString();    //바로빌부여 전송키 배열
		sendKeyList.getString().add("");
		sendKeyList.getString().add("");

		ArrayOfFaxMessage result = barobillApiService.fax.getFaxSendMessages(certKey, corpNum, sendKeyList);

		System.out.println(result);

	}

	/**
	 * GetFaxSendMessagesEx - 팩스 대량확인B (바로빌부여 전송키)
	 */
	@Test
	public void GetFaxSendMessagesEx() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)

		ArrayOfString sendKeyList = new ArrayOfString();    //바로빌부여 전송키 배열
		sendKeyList.getString().add("");
		sendKeyList.getString().add("");

		ArrayOfFaxMessageEx result = barobillApiService.fax.getFaxSendMessagesEx(certKey, corpNum, sendKeyList);

		System.out.println(result);

	}

	/**
	 * GetFaxSendMessagesByRefKey - 팩스 확인A (연동사부여 전송키)
	 */
	@Test
	public void GetFaxSendMessagesByRefKey() throws RemoteException {

		String certKey = "";        // 인증키
		String corpNum = "";        // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String refKey = "";         // 연동사부여 전송키

		ArrayOfFaxMessage result = barobillApiService.fax.getFaxSendMessagesByRefKey(certKey, corpNum, refKey);

		System.out.println(result);

	}

	/**
	 * GetFaxSendMessagesByRefKeyEx - 팩스 확인B (연동사부여 전송키)
	 */
	@Test
	public void GetFaxSendMessagesByRefKeyEx() throws RemoteException {

		String certKey = "";        // 인증키
		String corpNum = "";        // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String refKey = "";         // 연동사부여 전송키

		ArrayOfFaxMessageEx result = barobillApiService.fax.getFaxSendMessagesByRefKeyEx(certKey, corpNum, refKey);

		System.out.println(result);

	}

	/**
	 * 팩스 전송내역 URL
	 */
	@Test
	public void GetFaxHistoryURL() throws RemoteException {

		String certKey = "";            // 인증키
		String corpNum = "";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String id = "";                // 바로빌 회원 아이디
		String pwd = "";                // 바로빌 회원 비밀번호

		String result = barobillApiService.fax.getFaxHistoryURL(certKey, corpNum, id, pwd);

		System.out.println(result);

	}

	/**
	 * 팩스 파일 다운로드 URL
	 */
	@Test
	public void GetFaxFileURL() throws RemoteException {

		String certKey = "";            // 인증키
		String corpNum = "";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String sendKey = "";            // 바로빌부여 전송키
		int fileType = 1;                // 1:원본파일 2:TIF(변환)파일

		ArrayOfString result = barobillApiService.fax.getFaxFileURL(certKey, corpNum, sendKey, fileType);

		System.out.println(result);
	}

}
