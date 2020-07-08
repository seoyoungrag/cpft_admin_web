package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.barobill;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfContact;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * <h1>바로빌 기본 API</h1>
 * <p>
 * 바로빌 기본 API 는 BarobillApiService 클래스 안의(taxInvoice, edoc, cashbill 등 ..) 모든 API에 포함되어 있습니다.
 */
public class Tests {

	private static Logger LOGGER = null;
    
    @BeforeClass
    public static void setLogger() throws MalformedURLException
    {
        System.setProperty("log4j.configurationFile","log4j2.xml");
        LOGGER = LogManager.getLogger();
    }

	public static <T> String printAllField(T t) {
        String nameofCurrMethod = new Throwable() 
                .getStackTrace()[1] 
                .getMethodName(); 
        
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(t.getClass().getName());
		result.append(" return of "+nameofCurrMethod+":");
		result.append(" Object {");
		result.append(newLine);

		// determine fields declared in this class only (no fields of superclass)
		Field[] fields = t.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				field.setAccessible(true);
				result.append(field.get(t));
			} catch (IllegalAccessException ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}

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
     * CheckCorpIsMember - 회원사 여부 확인
     */
    @Test
    public void CheckCorpIsMember() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
        String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String checkCorpNum = "5618801138";        //확인할 사업자번호 ('-' 제외, 10자리)

        int result = barobillApiService.taxInvoice.checkCorpIsMember(certKey, corpNum, checkCorpNum);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * RegistCorp - 회원사 추가
     */
    //@Test
    public void RegistCorp() throws RemoteException {

        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String corpName = "";            //회사명
        String ceoName = "";            //대표자명
        String bizType = "";            //업태
        String bizClass = "";            //업종
        String postNum = "";            //우편번호
        String addr1 = "";                //주소1 (ex. 서울특별시 양천구 목1동)
        String addr2 = "";                //주소2 (ex. SBS방송센터 920)
        String memberName = "";            //담당자 성명
        String juminNum = "";            //주민등록번호 ('-' 제외, 13자리)
        String id = "";                    //연계사업자 아이디
        String pwd = "";                //연계사업자 비밀번호 (6~20자만 가능)
        String grade = "";                //직급
        String tel = "";                //전화번호
        String hp = "";                    //휴대폰
        String email = "";                //이메일

        int result = barobillApiService.taxInvoice.registCorp(certKey, corpNum, corpName, ceoName, bizType, bizClass, postNum, addr1, addr2, memberName, juminNum, id, pwd, grade, tel, hp, email);

        System.out.println(result);
    }

    /**
     * UpdateCorpInfo - 회원사 정보 수정
     */
    //@Test
    public void UpdateCorpInfo() throws RemoteException {

        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String corpName = "";            //회사명
        String ceoName = "";            //대표자명
        String bizType = "";            //업태
        String bizClass = "";            //업종
        String postNum = "";            //우편번호
        String addr1 = "";                //주소1 (ex. 서울특별시 양천구 목1동)
        String addr2 = "";                //주소2 (ex. SBS방송센터 920)

        int result = barobillApiService.taxInvoice.updateCorpInfo(certKey, corpNum, corpName, ceoName, bizType, bizClass, postNum, addr1, addr2);

        System.out.println(result);
    }

    /**
     * GetCorpMemberContacts - 회원사 담당자 목록
     */
    @Test
    public void GetCorpMemberContacts() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
        String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String checkCorpNum = "5618801138";        //확인할 사업자번호 ('-' 제외, 10자리)

        ArrayOfContact result = barobillApiService.taxInvoice.getCorpMemberContacts(certKey, corpNum, checkCorpNum);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * ChangeCorpManager - 회원사 관리자 변경
     */
    //@Test
    public void ChangeCorpManager() throws RemoteException {

        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String newManagerId = "";        //연계사업자 새 관리자 아이디

        int result = barobillApiService.taxInvoice.changeCorpManager(certKey, corpNum, newManagerId);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * AddUserToCorp - 사용자 추가
     */
    //@Test
    public void AddUserToCorp() throws RemoteException {
        String certKey = "";                //인증키
        String corpNum = "";                //연계사업자 사업자번호 ('-' 제외, 10자리)
        String memberName = "";                //담당자 성명
        String juminNum = "";                //주민등록번호 ('-' 제외, 13자리)
        String id = "";                        //연계사업자 아이디
        String pwd = "";                    //연계사업자 비밀번호 (6~20자만 가능)
        String grade = "";                    //직급
        String tel = "";                    //전화번호
        String hp = "";                        //휴대폰
        String email = "";                    //이메일

        int result = barobillApiService.taxInvoice.addUserToCorp(certKey, corpNum, id, pwd, memberName, juminNum, grade, tel, hp, email);

        System.out.println(result);
    }

    /**
     * UpdateUserInfo - 사용자 정보 수정
     */
    //@Test
    public void UpdateUserInfo() throws RemoteException {

        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String id = "";                    //연계사업자 아이디
        String memberName = "";            //담당자 성명
        String juminNum = "";            //주민등록번호 ('-' 제외, 13자리)
        String grade = "";                //직급
        String tel = "";                //전화번호
        String hp = "";                    //휴대폰
        String email = "";                //이메일

        int result = barobillApiService.taxInvoice.updateUserInfo(certKey, corpNum, id, memberName, juminNum, grade, tel, hp, email);

        System.out.println(result);
    }

    /**
     * UpdateUserPWD - 사용자 비밀번호 수정
     */
    //@Test
    public void UpdateUserPWD() throws RemoteException {

        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String id = "";                    //연계사업자 아이디
        String newPwd = "";                //새 비밀번호

        int result = barobillApiService.taxInvoice.updateUserPWD(certKey, corpNum, id, newPwd);

        System.out.println(result);
    }

    /**
     * GetBalanceCostAmount - 잔여포인트 확인
     */
    @Test
    public void GetBalanceCostAmount() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
        String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)

        long result = barobillApiService.taxInvoice.getBalanceCostAmount(certKey, corpNum);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * GetBalanceCostAmountOfInterOP - 연동사포인트 확인
     */
    @Test
    public void GetBalanceCostAmountOfInterOP() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키

        long result = barobillApiService.taxInvoice.getBalanceCostAmountOfInterOP(certKey);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * GetChargeUnitCost - 요금 단가 확인
     */
    @Test
    public void GetChargeUnitCost() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";    //인증키
        String corpNum = "5618801138";    //연계사업자 사업자번호 ('-' 제외, 10자리)
        int chargeCode = 11;    //1:세금계산서 2:계산서 3:거래명세서 4:입금표 5:청구서 6:견적서 7:영수증 8:발주서 9:현금영수증 11:SMS전송 12:FAX전송 13:LMS전송 14:MMS전송

        int result = barobillApiService.taxInvoice.getChargeUnitCost(certKey, corpNum, chargeCode);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * CheckChargeable - 과금 가능여부 확인
     */
    @Test
    public void CheckChargeable() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";        //인증키
        String corpNum = "5618801138";        //연계사업자 사업자번호 ('-' 제외, 10자리)
        int cType = 5;                //1:문서발행 2:발행+SMS전송 5:SMS전송 6:FAX전송 7:LMS전송 8:MMS전송
        int docType = 1;            //CType이 1,2 인 경우 = 1:세금계산서 2:계산서 3:거래명세서 4:입금표 5:청구서 6:견적서 7:영수증 8:발주서 9:현금영수증
        //CType이 5,6,7,8 인 경우 = 1

        int result = barobillApiService.taxInvoice.checkChargeable(certKey, corpNum, cType, docType);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * GetCertificateRegistURL - 공인인증서 등록 URL
     */
    @Test
    public void GetCertificateRegistURL() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
        String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String id = "timflabs";                    //연계사업자 아이디
        String pwd = "timfresh0525!!";                //연계사업자 비밀번호

        String result = barobillApiService.taxInvoice.getCertificateRegistURL(certKey, corpNum, id, pwd);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * GetCertificateExpireDate - 등록한 공인인증서 만료일 확인
     */
    @Test
    public void GetCertificateExpireDate() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
        String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)

        String result = barobillApiService.taxInvoice.getCertificateExpireDate(certKey, corpNum);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * RegistSMSFromNumber - 발신번호 추가
     */
    //@Test
    public void RegistSMSFromNumber() throws RemoteException {

        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String fromNumber = "";            //문자 발신번호

        int result = barobillApiService.taxInvoice.registSMSFromNumber(certKey, corpNum, fromNumber);

        System.out.println(result);
    }

    /**
     * CheckSMSFromNumber - 발신번호 확인
     */
    //@Test
    public void CheckSMSFromNumber() throws RemoteException {

        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String fromNumber = "";            //문자 발신번호

        int result = barobillApiService.taxInvoice.checkSMSFromNumber(certKey, corpNum, fromNumber);

        System.out.println(result);
    }

    /**
     * GetSMSFromNumberURL - 발신번호 관리 URL
     */
    @Test
    public void GetSMSFromNumberURL() throws RemoteException {

        String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
        String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String id = "timflabs";                    //연계사업자 아이디
        String pwd = "timfresh0525!!";                //연계사업자 비밀번호

        String result = barobillApiService.taxInvoice.getSMSFromNumberURL(certKey, corpNum, id, pwd);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
    }

    /**
     * GetBaroBillURL - 바로빌 URL
     */
    //@Test
    public void GetBaroBillURL() throws RemoteException {

        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
        String id = "";                    //연계사업자 아이디
        String pwd = "";                //연계사업자 비밀번호
        String togo = "";                //URL 코드

        String result = barobillApiService.taxInvoice.getBaroBillURL(certKey, corpNum, id, pwd, togo);

        System.out.println(result);
    }

}
