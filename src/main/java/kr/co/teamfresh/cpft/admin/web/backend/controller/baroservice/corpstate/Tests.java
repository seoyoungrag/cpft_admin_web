package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.corpstate;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfCorpState;
import com.baroservice.ws.ArrayOfString;
import com.baroservice.ws.CorpState;

/**
 * 바로빌 휴폐업조회 API 샘플
 */
public class Tests {

	/**
	 * 바로빌 API 정의 클래스
	 *
	 * 환경에 따라 BarobillApiProfile 를 지정해주세요.
	 */
	private BarobillApiService barobillApiService;

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
		result.append("return of "+nameofCurrMethod+":");
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

	public Tests() throws MalformedURLException {
		barobillApiService = new BarobillApiService(BarobillApiProfile.TESTBED);
	}

	/**
	 * GetCorpState - 단건 조회
	 */
	@Test
	public void GetCorpState() throws RemoteException {

		String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            // 인증키
		String corpNum = "5618801138";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		//String checkCorpNum = "1058675080";
		String checkCorpNum = "5618801138";

		CorpState result = barobillApiService.corpState.getCorpState(certKey, corpNum, checkCorpNum);
		LOGGER.info(printAllField(result));
		//System.out.println(result);

	}

	/**
	 * GetCorpStates - 대량 조회
	 */
	//@Test
	public void GetCorpStates() throws RemoteException {
		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)


		ArrayOfString checkCorpNumList = new ArrayOfString();        //확인할 사업자번호 ('-' 제외, 10자리)
		checkCorpNumList.getString().add("");
		checkCorpNumList.getString().add("");

		ArrayOfCorpState result = barobillApiService.corpState.getCorpStates(certKey, corpNum, checkCorpNumList);

		System.out.println(result);
	}

	/**
	 * GetCorpStateScrapRequestURL - 휴폐업 서비스 신청 URL
	 */
	@Test
	public void GetCorpStateScrapRequestURL() throws RemoteException {

		String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            // 인증키
		String corpNum = "5618801138";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
		String id = "timflabs";                    //연계사업자 아이디
		String pwd = "Timf180525!"; 

		String result = barobillApiService.corpState.getCorpStateScrapRequestURL(certKey, corpNum, id, pwd);

		System.out.println(result);

	}

}
