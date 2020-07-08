package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.cashbill;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfCashBillLog;
import com.baroservice.ws.ArrayOfCashBillState;
import com.baroservice.ws.ArrayOfString;
import com.baroservice.ws.CashBill;
import com.baroservice.ws.CashBillState;
import com.baroservice.ws.PagedCashBill;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * 바로빌 현금영수증 API 샘플
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

	//문서 프로세스

	/**
	 * RegistCashBill - 등록
	 */
	@Test
	public void RegistCashBill() throws RemoteException {

		String certKey = "";                        //인증키

		CashBill cashBill = new CashBill();
		cashBill.setMgtKey("");                        //연동사부여 문서키

		//가맹점 정보
		cashBill.setFranchiseCorpNum("");            //가맹점 사업자번호
		cashBill.setFranchiseMemberID("");            //가맹점 바로빌 회원 아이디
		cashBill.setFranchiseCorpName("");            //가맹점 회사명
		cashBill.setFranchiseCEOName("");            //가맹점 대표자명
		cashBill.setFranchiseAddr("");                //가맹점 주소
		cashBill.setFranchiseTel("");                //가맹점 전화번호

		//소비자 정보
		cashBill.setIdentityNum("");                //소비자 신분확인번호 ("-" 를 제외한 주민등록번호/사업자번호/휴대폰번호/카드번호 중 택1)
		cashBill.setHP("");                            //소비자 휴대폰번호 (문자 전송시 활용)
		cashBill.setFax("");                        //소비자 팩스번호 (팩스 전송시 활용)
		cashBill.setEmail("");                        //소비자 이메일 (이메일 전송시 활용)

		//현금영수증 기본정보
		cashBill.setTradeType("N");                    //거래구분 : N-승인거래, D-취소거래
		cashBill.setTradeUsage("1");                //거래용도 : 1-소득공제용, 2-지출증빙용 (신분확인번호가 사업자번호인 경우 지출증빙용으로)
		cashBill.setTradeMethod("1");                //거래방법 : 1-카드, 3-주민등록번호, 4-사업자번호, 5-휴대폰번호 (신분확인번호 종류에 따라 선택)

		cashBill.setCancelType("");                    //취소사유 : 1-거래취소, 2-오류발행, 3-기타 (거래구분이 취소거래일 경우에만 작성)
		cashBill.setCancelNTSConfirmNum("");        //취소할 원본 현금영수증의 국세청 승인번호
		cashBill.setCancelNTSConfirmDate("");        //취소할 원본 현금영수증의 국세청 승인일자 (YYYYMMDD)

		cashBill.setTradeDate("");                    //거래일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.
		cashBill.setItemName("");                    //품목명
		cashBill.setAmount("");                        //공급가액
		cashBill.setTax("");                        //부가세
		cashBill.setServiceCharge("");                //봉사료

		int result = barobillApiService.cashbill.registCashBill(certKey, cashBill.getFranchiseCorpNum(), cashBill.getFranchiseMemberID(), cashBill);

		System.out.println(result);
	}

	/**
	 * UpdateCashBill - 수정
	 */
	@Test
	public void UpdateCashBill() throws RemoteException {

		String certKey = "";                        //인증키

		CashBill cashBill = new CashBill();
		cashBill.setMgtKey("");                        //연동사부여 문서키

		//가맹점 정보
		cashBill.setFranchiseCorpNum("");            //가맹점 사업자번호
		cashBill.setFranchiseMemberID("");            //가맹점 바로빌 회원 아이디
		cashBill.setFranchiseCorpName("");            //가맹점 회사명
		cashBill.setFranchiseCEOName("");            //가맹점 대표자명
		cashBill.setFranchiseAddr("");                //가맹점 주소
		cashBill.setFranchiseTel("");                //가맹점 전화번호

		//소비자 정보
		cashBill.setIdentityNum("");                //소비자 신분확인번호 ("-" 를 제외한 주민등록번호/사업자번호/휴대폰번호/카드번호 중 택1)
		cashBill.setHP("");                            //소비자 휴대폰번호 (문자 전송시 활용)
		cashBill.setFax("");                        //소비자 팩스번호 (팩스 전송시 활용)
		cashBill.setEmail("");                        //소비자 이메일 (이메일 전송시 활용)

		//현금영수증 기본정보
		cashBill.setTradeType("N");                    //거래구분 : N-승인거래, D-취소거래
		cashBill.setTradeUsage("1");                //거래용도 : 1-소득공제용, 2-지출증빙용 (신분확인번호가 사업자번호인 경우 지출증빙용으로)
		cashBill.setTradeMethod("1");                //거래방법 : 1-카드, 3-주민등록번호, 4-사업자번호, 5-휴대폰번호 (신분확인번호 종류에 따라 선택)

		cashBill.setCancelType("");                    //취소사유 : 1-거래취소, 2-오류발행, 3-기타 (거래구분이 취소거래일 경우에만 작성)
		cashBill.setCancelNTSConfirmNum("");        //취소할 원본 현금영수증의 국세청 승인번호
		cashBill.setCancelNTSConfirmDate("");        //취소할 원본 현금영수증의 국세청 승인일자 (YYYYMMDD)

		cashBill.setTradeDate("");                    //거래일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.
		cashBill.setItemName("");                    //품목명
		cashBill.setAmount("");                        //공급가액
		cashBill.setTax("");                        //부가세
		cashBill.setServiceCharge("");                //봉사료

		int result = barobillApiService.cashbill.updateCashBill(certKey, cashBill.getFranchiseCorpNum(), cashBill.getFranchiseMemberID(), cashBill);

		System.out.println(result);
	}

	/**
	 * IssueCashBill - 발행
	 */
	@Test
	public void IssueCashBill() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		boolean smsSendYn = false;        //발행 알림문자 전송여부 (발행비용과 별도로 과금됨)
		String mailTitle = "";            //발행 알림메일의 제목 (공백이나 Null의 경우 바로빌 기본값으로 전송됨.)

		int result = barobillApiService.cashbill.issueCashBill(certKey, corpNum, id, mgtKey, smsSendYn, mailTitle);

		System.out.println(result);
	}

	/**
	 * CancelCashBillBeforeNTSSend - 발행취소 (국세청전송 전)
	 */
	@Test
	public void CancelCashBillBeforeNTSSend() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.cashbill.cancelCashBillBeforeNTSSend(certKey, corpNum, id, mgtKey);

		System.out.println(result);
	}

	/**
	 * CancelCashBill - 발행취소 (국세청전송 후)
	 */
	@Test
	public void CancelCashBill() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		String cancelType = "1";        //취소사유 : 1-거래취소, 2-오류발행, 3-기타
		boolean smsSendYn = false;        //취소 알림문자 전송여부 (발행비용과 별도로 과금됨)
		String mailTitle = "";            //취소 알림메일의 제목 (공백이나 Null의 경우 바로빌 기본값으로 전송됨.)

		int result = barobillApiService.cashbill.cancelCashBill(certKey, corpNum, id, mgtKey, cancelType, smsSendYn, mailTitle);

		System.out.println(result);
	}

	/**
	 * CancelCashBillPartial - 부분취소 (국세청전송 후)
	 */
	@Test
	public void CancelCashBillPartial() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		String cancelType = "1";        //취소사유 : 1-거래취소, 2-오류발행, 3-기타
		boolean smsSendYn = false;        //취소 알림문자 전송여부 (발행비용과 별도로 과금됨)
		String mailTitle = "";            //취소 알림메일의 제목 (공백이나 Null의 경우 바로빌 기본값으로 전송됨.)

		int result = barobillApiService.cashbill.cancelCashBill(certKey, corpNum, id, mgtKey, cancelType, smsSendYn, mailTitle);

		System.out.println(result);
	}

	/**
	 * DeleteCashBill - 삭제
	 */
	@Test
	public void DeleteCashBill() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.cashbill.deleteCashBill(certKey, corpNum, id, mgtKey);

		System.out.println(result);
	}

	//홈택스 조회

	/**
	 * GetCashBillScrapRequestURL - 국세청 현금영수증 조회서비스 신청 URL
	 */
	@Test
	public void GetCashBillScrapRequestURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userID = "";                //연계사업자 아이디
		String pwd = "";                //연계사업자 비밀번호

		String result = barobillApiService.cashbill.getCashBillScrapRequestURL(certKey, corpNum, userID, pwd);

		System.out.println(result);
	}

	/**
	 * GetCashBillSalesList - 일별 현금영수증 발급분 조회(매출) [국세청 전송완료 건만]
	 */
	@Test
	public void GetDailyCashBillSalesList() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "";                //연계사업자 아이디
		String baseDate = "";            //기준날짜
		int countPerPage = 10;            //페이지수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedCashBill result = barobillApiService.cashbill.getDailyCashBillSalesList(certKey, corpNum, userId, baseDate, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) { //실패
			System.out.println(result.getCurrentPage());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetCashBillSalesList - 월별 현금영수증 발급분 조회(매출) [국세청 전송완료 건만]
	 */
	@Test
	public void GetMonthlyCashBillSalesList() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "";                //연계사업자 아이디
		String baseMonth = "";            //기준날짜
		int countPerPage = 10;            //페이지수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedCashBill result = barobillApiService.cashbill.getMonthlyCashBillSalesList(certKey, corpNum, userId, baseMonth, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) { //실패
			System.out.println(result.getCurrentPage());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetCashBillPurchaseList - 일별 현금영수증 수취분 조회(매입) [국세청 전송완료 건만]
	 */
	@Test
	public void GetDailyCashBillPurchaseList() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "";                //연계사업자 아이디
		String baseDate = "";            //기준날짜
		int countPerPage = 10;            //페이지수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedCashBill result = barobillApiService.cashbill.getDailyCashBillPurchaseList(certKey, corpNum, userId, baseDate, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) { //실패
			System.out.println(result.getCurrentPage());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetCashBillPurchaseList - 일별 현금영수증 수취분 조회(매입) [국세청 전송완료 건만]
	 */
	@Test
	public void GetMonthlyCashBillPurchaseList() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "";                //연계사업자 아이디
		String baseMonth = "";            //기준날짜
		int countPerPage = 10;            //페이지수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedCashBill result = barobillApiService.cashbill.getMonthlyCashBillPurchaseList(certKey, corpNum, userId, baseMonth, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) { //실패
			System.out.println(result.getCurrentPage());
		} else { //성공
			System.out.println(result);
		}
	}


	//문서정보

	/**
	 * GetCashBill - 문서 정보
	 */
	@Test
	public void GetCashBill() throws RemoteException {

		String certKey = "";            //인증키
		String CorpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		CashBill result = barobillApiService.cashbill.getCashBill(certKey, CorpNum, id, mgtKey);

		int intResult = 0;

		try {
			intResult = Integer.parseInt(result.getTradeType());
		} catch (NumberFormatException e) {
		}

		if (intResult < 0) { //실패
			System.out.println(intResult);
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetCashBillLog - 문서 이력
	 */
	@Test
	public void GetCashBillLog() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		ArrayOfCashBillLog result = barobillApiService.cashbill.getCashBillLog(certKey, corpNum, id, mgtKey);

		if (result.getCashBillLog().get(0).getSeq() < 0) { //실패
			System.out.println(result.getCashBillLog().get(0).getSeq());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetCashBillState - 문서 상태
	 */
	@Test
	public void GetCashBillState() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		CashBillState result = barobillApiService.cashbill.getCashBillState(certKey, corpNum, id, mgtKey);

		if (result.getBarobillState() < 0) { //실패
			System.out.println(result.getBarobillState());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetCashBillStates - 문서 상태(대량, 100건 까지)
	 */
	@Test
	public void GetCashBillStates() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		ArrayOfString mgtKeyList = new ArrayOfString();    //연동사부여 문서키 배열
		mgtKeyList.getString().add("");
		mgtKeyList.getString().add("");

		ArrayOfCashBillState result = barobillApiService.cashbill.getCashBillStates(certKey, corpNum, id, mgtKeyList);

		for (CashBillState r : result.getCashBillState()) {
			if (r.getBarobillState() < 0) { //실패
				System.out.println(r.getBarobillState());
			} else { //성공
				System.out.println(r);
			}
			System.out.println("<br>");
		}
	}

	//부가서비스

	/**
	 * SendEmail - 이메일 전송
	 */
	@Test
	public void SendEmail() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		String toEmailAddress = "";        //수신자 메일 주소

		int result = barobillApiService.cashbill.sendEmail(certKey, corpNum, id, mgtKey, toEmailAddress);

		System.out.println(result);
	}

	/**
	 * SendSMS - 문자 전송
	 */
	@Test
	public void SendSMS() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		String fromNumber = "";            //발신자 휴대폰 번호
		String toNumber = "";            //수신자 휴대폰 번호
		String contents = "";            //문자메세지 내용

		int result = barobillApiService.cashbill.sendSMS(certKey, corpNum, id, mgtKey, fromNumber, toNumber, contents);

		System.out.println(result);
	}

	/**
	 * SendFax - 팩스 전송
	 */
	@Test
	public void SendFax() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		String fromFaxNumber = "";        //발신자 팩스 번호
		String toFaxNumber = "";        //수신자 팩스 번호

		int result = barobillApiService.cashbill.sendFax(certKey, corpNum, id, mgtKey, fromFaxNumber, toFaxNumber);

		System.out.println(result);
	}

	//기타

	/**
	 * CheckMgtKeyIsExists - 연동사부여 문서키 사용여부 확인
	 */
	@Test
	public void CheckMgtKeyIsExists() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.cashbill.checkMgtKeyIsExists(certKey, corpNum, id, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetCashBillPopUpURL - 문서 내용보기 팝업 URL
	 */
	@Test
	public void GetCashBillPopUpURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String pwd = "";                //비밀번호
		String mgtKey = "";                //연동사부여 문서키

		String result = barobillApiService.cashbill.getCashBillPopUpURL(certKey, corpNum, id, pwd, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetCashBillPrintURL - 인쇄 팝업 URL
	 */
	@Test
	public void GetCashBillPrintURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String pwd = "";                //비밀번호
		String mgtKey = "";                //연동사부여 문서키

		String result = barobillApiService.cashbill.getCashBillPrintURL(certKey, corpNum, id, pwd, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetCashBillsPrintURL - 대량인쇄 팝업 URL
	 */
	@Test
	public void GetCashBillsPrintURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String pwd = "";                //비밀번호
		ArrayOfString mgtKeyList = new ArrayOfString();        //연동사부여 문서키 배열
		mgtKeyList.getString().add("");
		mgtKeyList.getString().add("");

		String result = barobillApiService.cashbill.getCashBillsPrintURL(certKey, corpNum, id, pwd, mgtKeyList);

		System.out.println(result);
	}


}
