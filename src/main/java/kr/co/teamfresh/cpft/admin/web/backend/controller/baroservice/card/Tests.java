package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.card;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfCard;
import com.baroservice.ws.PagedCardLog;
import com.baroservice.ws.PagedCardLogEx;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * 바로빌 카드조회 API 샘플
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
	 * GetCard - 등록한 카드번호 조회
	 */
	@Test
	public void GetCard() throws RemoteException {
		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)

		ArrayOfCard result = barobillApiService.card.getCard(certKey, corpNum);

		int intResult = 0;

		try {
            if (result.getCard().size() == 1) {
                intResult = Integer.parseInt(result.getCard().get(0).getCardNum());
            }
		} catch (NumberFormatException e) {
		}

		if (intResult < 0) { //실패
			System.out.println(intResult);
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetDailyCardLog - 일별 카드 사용내역 조회
	 */
	@Test
	public void GetDailyCardLog() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String cardNum = "";            //카드번호
		String baseDate = "";            //기준날짜
		int countPerPage = 1;            //페이지수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedCardLog result = barobillApiService.card.getDailyCardLog(certKey, corpNum, id, cardNum, baseDate, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) { //실패
			System.out.println(result.getCurrentPage());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetDailyCardLogEx - 일별 카드 사용내역 조회 (확장)
	 */
	@Test
	public void GetDailyCardLogEx() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String cardNum = "";            //카드번호
		String baseDate = "";            //기준날짜
		int countPerPage = 1;            //페이지수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedCardLogEx result = barobillApiService.card.getDailyCardLogEx(certKey, corpNum, id, cardNum, baseDate, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) { //실패
			System.out.println(result.getCurrentPage());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetMonthlyCardLog - 월별 카드 사용내역 조회
	 */
	@Test
	public void GetMonthlyCardLog() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String cardNum = "";            //카드번호
		String baseMonth = "";            //기준월
		int countPerPage = 1;            //페이지수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedCardLog result = barobillApiService.card.getDailyCardLog(certKey, corpNum, id, cardNum, baseMonth, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) { //실패
			System.out.println(result.getCurrentPage());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * 월별 카드 사용내역 조회 (확장)
	 */
	@Test
	public void GetMonthlyCardLogEx() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String cardNum = "";            //카드번호
		String baseMonth = "";            //기준월
		int countPerPage = 1;            //페이지수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedCardLogEx result = barobillApiService.card.getDailyCardLogEx(certKey, corpNum, id, cardNum, baseMonth, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) { //실패
			System.out.println(result.getCurrentPage());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetCardScrapRequestURL - 카드조회 서비스 신청 URL
	 */
	@Test
	public void GetCardScrapRequestURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String pwd = "";                //연계사업자 비밀번호

		String result = barobillApiService.card.getCardScrapRequestURL(certKey, corpNum, id, pwd);

		System.out.println(result);
	}

	/**
	 * GetCardManagementURL - 카드번호 관리 URL
	 */
	@Test
	public void GetCardManagementURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                //연계사업자 아이디
		String pwd = "";                //연계사업자 비밀번호

		String result = barobillApiService.card.getCardManagementURL(certKey, corpNum, id, pwd);

		System.out.println(result);
	}

	/**
	 * GetCardLogURL - 카드 사용내역 url
	 */
	@Test
	public void GetCardLogURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                //연계사업자 아이디
		String pwd = "";                //연계사업자 비밀번호

		String result = barobillApiService.card.getCardLogURL(certKey, corpNum, id, pwd);

		System.out.println(result);
	}

}
