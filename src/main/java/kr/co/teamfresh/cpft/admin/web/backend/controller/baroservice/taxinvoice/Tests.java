package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.taxinvoice;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfAttachedFile;
import com.baroservice.ws.ArrayOfEMAILPUBLICKEY;
import com.baroservice.ws.ArrayOfInvoiceLog;
import com.baroservice.ws.ArrayOfLinkedDoc;
import com.baroservice.ws.ArrayOfString;
import com.baroservice.ws.ArrayOfTaxInvoiceState;
import com.baroservice.ws.ArrayOfTaxInvoiceStateEX;
import com.baroservice.ws.ArrayOfTaxInvoiceTradeLineItem;
import com.baroservice.ws.InvoiceParty;
import com.baroservice.ws.NTSSendOption;
import com.baroservice.ws.PagedTaxInvoiceEx;
import com.baroservice.ws.TaxInvoice;
import com.baroservice.ws.TaxInvoiceState;
import com.baroservice.ws.TaxInvoiceStateEX;
import com.baroservice.ws.TaxInvoiceTradeLineItem;

/**
 * 바로빌 세금계산서 API 샘플
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
	 * RegistAndIssueTaxInvoice - 일반(수정)세금계산서 "등록" 과 "발행" 을 한번에 처리
	 */
	@Test
	public void RegistAndIssueTaxInvoice() throws RemoteException {

		String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";                                //인증키

		
		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		//-------------------------------------------
		//수정사유코드
		//-------------------------------------------
		//공백-일반세금계산서, 1-기재사항의 착오 정정, 2-공급가액의 변동, 3-재화의 환입, 4-계약의 해제, 5-내국신용장 사후개설, 6-착오에 의한 이중발행
		//-------------------------------------------
		taxInvoice.setModifyCode("");

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("2");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("1");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("3");

		taxInvoice.setCash("1");                                //현금
		taxInvoice.setChkBill("2");                            //수표
		taxInvoice.setNote("0");                                //어음
		taxInvoice.setCredit("0");                            //외상미수금

		taxInvoice.setRemark1("re1");
		taxInvoice.setRemark2("re2");
		taxInvoice.setRemark3("re3");

		taxInvoice.setWriteDate("20200422");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setMgtNum("3");        //필수입력 - 연동사부여 문서키
		taxInvoice.getInvoicerParty().setCorpNum("5618801138");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("팀프레시");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("이성일");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("공급자주소");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("timflabs");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("timfresh0525!!");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("young.rag@timf.co.kr");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setCorpNum("5618801138");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("더미회사");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("더미대표");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("더미회사주소");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");
		taxInvoice.getInvoiceeParty().setContactName("더미담당자");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");

		//-------------------------------------------
		//수탁자 정보 - 입력하지 않음
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setCorpNum("");
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");
		taxInvoice.getBrokerParty().setCEOName("");
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");
		taxInvoice.getBrokerParty().setContactName("");
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("20210422");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("아이템1");
			taxInvoiceTradeLineItem.setInformation("몰라");
			taxInvoiceTradeLineItem.setChargeableUnit("1");
			taxInvoiceTradeLineItem.setUnitPrice("1");
			taxInvoiceTradeLineItem.setAmount("2");
			taxInvoiceTradeLineItem.setTax("1");
			taxInvoiceTradeLineItem.setDescription("설명서");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		boolean sendSms = false;                        //문자 발송여부 (공급받는자 정보의 HP 항목이 입력된 경우에만 발송됨)

		boolean forceIssue = false;                        //가산세가 예상되는 세금계산서 발행 여부

		String mailTitle = "";                            //전송되는 이메일의 제목 설정 (공백 시 바로빌 기본 제목으로 전송됨)

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registAndIssueTaxInvoice(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice, sendSms, forceIssue, mailTitle);

		System.out.println(result);
	}

	/**
	 * RegistAndIssueBrokerTaxInvoice - 위수탁 일반(수정)세금계산서 "등록" 과 "발행" 을 한번에 처리
	 */
	@Test
	public void RegistAndIssueBrokerTaxInvoice() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		//-------------------------------------------
		//수정사유코드
		//-------------------------------------------
		//공백-일반세금계산서, 1-기재사항의 착오 정정, 2-공급가액의 변동, 3-재화의 환입, 4-계약의 해제, 5-내국신용장 사후개설, 6-착오에 의한 이중발행
		//-------------------------------------------
		taxInvoice.setModifyCode("");

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");

		//-------------------------------------------
		//수탁자 정보 - 입력하지 않음
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setMgtNum("");            //필수입력 - 연동사부여 문서키
		taxInvoice.getBrokerParty().setCorpNum("");            //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");        //필수입력
		taxInvoice.getBrokerParty().setCEOName("");            //필수입력
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getBrokerParty().setContactName("");        //필수입력
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		boolean sendSms = false;                        //문자 발송여부 (공급받는자 정보의 HP 항목이 입력된 경우에만 발송됨)

		boolean forceIssue = false;                        //가산세가 예상되는 세금계산서 발행 여부

		String mailTitle = "";                            //전송되는 이메일의 제목 설정 (공백 시 바로빌 기본 제목으로 전송됨)

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registAndIssueBrokerTaxInvoice(certKey, taxInvoice.getBrokerParty().getCorpNum(), taxInvoice, sendSms, forceIssue, mailTitle);

		System.out.println(result);
	}

	/**
	 * RegistAndPreIssueTaxInvoice - 일반(수정)세금계산서 "등록" 과 "발행예정" 을 한번에 처리
	 */
	@Test
	public void RegistAndPreIssueTaxInvoice() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		//-------------------------------------------
		//수정사유코드
		//-------------------------------------------
		//공백-일반세금계산서, 1-기재사항의 착오 정정, 2-공급가액의 변동, 3-재화의 환입, 4-계약의 해제, 5-내국신용장 사후개설, 6-착오에 의한 이중발행
		//-------------------------------------------
		taxInvoice.setModifyCode("");

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setMgtNum("");        //필수입력 - 연동사부여 문서키
		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");

		//-------------------------------------------
		//수탁자 정보 - 입력하지 않음
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setCorpNum("");
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");
		taxInvoice.getBrokerParty().setCEOName("");
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");
		taxInvoice.getBrokerParty().setContactName("");
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		boolean sendSms = false;                        //문자 발송여부 (공급받는자 정보의 HP 항목이 입력된 경우에만 발송됨)

		int issueTiming = 1;                            //발행시점 : 1-공급자 직접발행, 2-공급받는자 승인시 자동발행

		String mailTitle = "";                            //전송되는 이메일의 제목 설정 (공백 시 바로빌 기본 제목으로 전송됨)

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registAndPreIssueTaxInvoice(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice, sendSms, issueTiming, mailTitle);

		System.out.println(result);
	}

	/**
	 * RegistAndPreIssueBrokerTaxInvoice - 위수탁 일반(수정)세금계산서 "등록" 과 "발행예정" 을 한번에 처리
	 */
	@Test
	public void RegistAndPreIssueBrokerTaxInvoice() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		//-------------------------------------------
		//수정사유코드
		//-------------------------------------------
		//공백-일반세금계산서, 1-기재사항의 착오 정정, 2-공급가액의 변동, 3-재화의 환입, 4-계약의 해제, 5-내국신용장 사후개설, 6-착오에 의한 이중발행
		//-------------------------------------------
		taxInvoice.setModifyCode("");

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");

		//-------------------------------------------
		//수탁자 정보 - 입력하지 않음
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setMgtNum("");            //필수입력 - 연동사부여 문서키
		taxInvoice.getBrokerParty().setCorpNum("");            //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");        //필수입력
		taxInvoice.getBrokerParty().setCEOName("");            //필수입력
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getBrokerParty().setContactName("");        //필수입력
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		boolean sendSms = false;                        //문자 발송여부 (공급받는자 정보의 HP 항목이 입력된 경우에만 발송됨)

		int issueTiming = 1;                            //발행시점 : 1-공급자 직접발행, 2-공급받는자 승인시 자동발행

		String mailTitle = "";                            //전송되는 이메일의 제목 설정 (공백 시 바로빌 기본 제목으로 전송됨)

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registAndPreIssueBrokerTaxInvoice(certKey, taxInvoice.getBrokerParty().getCorpNum(), taxInvoice, sendSms, issueTiming, mailTitle);

		System.out.println(result);
	}

	/**
	 * RegistAndReverseIssueTaxInvoice - 역발행 세금계산서 "등록" 과 "역발행" 을 한번에 처리
	 */
	@Test
	public void RegistAndReverseIssueTaxInvoice() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setMgtNum("");        //필수입력 - 연동사부여 문서키
		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");            //필수입력

		//-------------------------------------------
		//수탁자 정보 - 입력하지 않음
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setCorpNum("");
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");
		taxInvoice.getBrokerParty().setCEOName("");
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");
		taxInvoice.getBrokerParty().setContactName("");
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		boolean sendSms = false;                        //문자 발송여부 (공급받는자 정보의 HP 항목이 입력된 경우에만 발송됨)

		boolean forceIssue = false;                        //가산세가 예상되는 세금계산서 발행 여부

		String mailTitle = "";                            //전송되는 이메일의 제목 설정 (공백 시 바로빌 기본 제목으로 전송됨)

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registAndReverseIssueTaxInvoice(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice, sendSms, forceIssue, mailTitle);

		System.out.println(result);
	}

	//홈택스 조회

	/**
	 * GetTaxInvoiceScrapRequestURL - 국세청 세금계산서 조회서비스 신청 URL
	 */
	@Test
	public void GetTaxInvoiceScrapRequestURL() throws RemoteException {

		String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
		String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "timflabs";                //연계사업자 아이디
		String pwd = "Timf180525!";                //연계사업자 비밀번호

		String result = barobillApiService.taxInvoice.getTaxInvoiceScrapRequestURL(certKey, corpNum, userId, pwd);

		LOGGER.info(printAllField(result));
		//System.out.println(result);
	}

	/**
	 * GetTaxInvoiceSalesList - 매출 세금계산서 조회 [국세청 전송완료 건만]
	 */
	@Test
	public void GetDailyTaxInvoiceSalesList() throws RemoteException {

		String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
		String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "timflabs";             //연계사업자 아이디
		int taxType = 1;                //과세형태, 1:과세 2:영세 3:면세
		int dateType = 1;                //조회기준, 1:작성일자 2:발행일자
		String baseDate = "2020-04-01";           //기준날짜
		int countPerPage = 10;             //페이지당 갯수
		int currentPage = 1;            //현재페이지

		PagedTaxInvoiceEx result = barobillApiService.taxInvoice.getDailyTaxInvoiceSalesList(certKey, corpNum, userId, taxType, dateType, baseDate, countPerPage, currentPage);

		if (result.getCurrentPage() < 0) {
			LOGGER.info(printAllField(result.getCurrentPage()));
			System.out.println(result.getCurrentPage());
		} else {
			LOGGER.info(printAllField(result));
			System.out.println(result);
		}
	}

	/**
	 * GetTaxInvoiceSalesListEx - 매출 세금계산서 조회 [국세청 전송완료 건만] (대표품목 포함)
	 */
	@Test
	public void GetMonthlyTaxInvoiceSalesList() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "";             //연계사업자 아이디
		int taxType = 1;                //과세형태, 1:과세 2:영세 3:면세
		int dateType = 1;            //조회기준, 1:작성일자 2:발행일자
		String baseMonth = "";            //기준월
		int countPerPage = 10;            //페이지당 갯수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedTaxInvoiceEx result = barobillApiService.taxInvoice.getMonthlyTaxInvoiceSalesList(certKey, corpNum, userId, taxType, dateType, baseMonth, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) {
			System.out.println(result.getCurrentPage());
		} else {
			System.out.println(result);
		}
	}

	/**
	 * GetTaxInvoicePurchaseList - 매입 세금계산서 조회 [국세청 전송완료 건만]
	 */
	@Test
	public void GetDailyTaxInvoicePurchaseList() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "";             //연계사업자 아이디
		int taxType = 1;                //과세형태, 1:과세 2:영세 3:면세
		int dateType = 1;                //조회기준, 1:작성일자 2:발행일자
		String baseDate = "";           //기준날짜
		int countPerPage = 10;            //페이지당 갯수
		int currentPage = 1;            //현재페이지

		PagedTaxInvoiceEx result = barobillApiService.taxInvoice.getDailyTaxInvoicePurchaseList(certKey, corpNum, userId, taxType, dateType, baseDate, countPerPage, currentPage);

		if (result.getCurrentPage() < 0) {
			System.out.println(result.getCurrentPage());
		} else {
			System.out.println(result);
		}
	}

	/**
	 * GetTaxInvoicePurchaseListEx - 매입 세금계산서 조회 [국세청 전송완료 건만] (대표품목 포함)
	 */
	@Test
	public void GetMonthlyTaxInvoicePurchaseListEx() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String userId = "";             //연계사업자 아이디
		int taxType = 1;                //과세형태, 1:과세 2:영세 3:면세
		int dateType = 1;                //조회기준, 1:작성일자 2:발행일자
		String baseMonth = "";           //기준날짜
		int countPerPage = 10;            //페이지당 갯수
		int currentPage = 1;            //현재페이지
		int orderDirection = 2;            //1:ASC 2:DESC

		PagedTaxInvoiceEx result = barobillApiService.taxInvoice.getMonthlyTaxInvoicePurchaseList(certKey, corpNum, userId, taxType, dateType, baseMonth, countPerPage, currentPage, orderDirection);

		if (result.getCurrentPage() < 0) {
			System.out.println(result.getCurrentPage());
		} else {
			System.out.println(result);
		}
	}

	//문서 등록/수정/삭제

	/**
	 * RegistTaxInvoice - 일반세금계산서 등록
	 */
	@Test
	public void RegistTaxInvoice() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setMgtNum("");        //정발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setMgtNum("");        //역발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");        //역발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");            //역발행시 필수입력

		//-------------------------------------------
		//수탁자 정보 - 위수탁 발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setMgtNum("");            //위수탁발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getBrokerParty().setCorpNum("");            //위수탁발행시 필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setCEOName("");            //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");        //위수탁발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getBrokerParty().setContactName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");            //위수탁발행시 필수입력

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registTaxInvoice(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice);            //정발행
		//int Result = barobillApiService.taxInvoice.registTaxInvoiceReverse(CERTKEY, taxInvoice.getInvoiceeParty().getCorpNum(), taxInvoice);	//역발행
		//int Result = barobillApiService.taxInvoice.registBrokerTaxInvoice(CERTKEY, taxInvoice.getBrokerParty().getCorpNum(), taxInvoice);		//위수탁

		System.out.println(result);
	}

	/**
	 * RegistTaxInvoiceEX - 일반세금계산서 등록 (승인 시 자동발행 옵션 추가)
	 */
	@Test
	public void RegistTaxInvoiceEX() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setMgtNum("");        //정발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setMgtNum("");        //역발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");        //역발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");            //역발행시 필수입력

		//-------------------------------------------
		//수탁자 정보 - 위수탁 발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setMgtNum("");            //위수탁발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getBrokerParty().setCorpNum("");            //위수탁발행시 필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setCEOName("");            //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");        //위수탁발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getBrokerParty().setContactName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");            //위수탁발행시 필수입력

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		int IssueTiming = 1;            //발행시점 : 1-공급자 직접발행, 2-공급받는자 승인시 자동발행
		//발행예정 기능을 사용한 경우에만 적용됨.

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registTaxInvoiceEX(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice, IssueTiming);        //정발행
		//int Result = barobillApiService.taxInvoice.registTaxInvoiceReverse(CERTKEY, taxInvoice.getInvoiceeParty().getCorpNum(), taxInvoice);				//역발행
		//int Result = barobillApiService.taxInvoice.registBrokerTaxInvoiceEX(CERTKEY, taxInvoice.getBrokerParty().getCorpNum(), taxInvoice, IssueTiming);	//위수탁

		System.out.println(result);
	}

	/**
	 * RegistModifyTaxInvoice - 수정세금계산서 등록
	 */
	@Test
	public void RegistModifyTaxInvoice() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		//-------------------------------------------
		//수정사유코드
		//-------------------------------------------
		//1-기재사항의 착오 정정, 2-공급가액의 변동, 3-재화의 환입, 4-계약의 해제, 5-내국신용장 사후개설, 6-착오에 의한 이중발행
		//-------------------------------------------
		taxInvoice.setModifyCode("6");

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setMgtNum("");        //정발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setMgtNum("");        //역발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");        //역발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");            //역발행시 필수입력

		//-------------------------------------------
		//수탁자 정보 - 위수탁 발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setMgtNum("");            //위수탁발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getBrokerParty().setCorpNum("");            //위수탁발행시 필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setCEOName("");            //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");        //위수탁발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getBrokerParty().setContactName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");            //위수탁발행시 필수입력

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		String OriginalNTSSendKey = "";            //당초 세금계산서의 국세청 승인번호
		//바로빌을 통해 발행된 세금계산서만 가능합니다.

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registModifyTaxInvoice(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice, OriginalNTSSendKey);            //정발행
		//int Result = BS_TI.registModifyBrokerTaxInvoice(CERTKEY, taxInvoice.getBrokerParty().getCorpNum(), taxInvoice, OriginalNTSSendKey);	//위수탁

		System.out.println(result);
	}

	/**
	 * RegistModifyTaxInvoiceEX - 수정세금계산서 등록 (승인 시 자동발행 옵션 추가)
	 */
	@Test
	public void RegistModifyTaxInvoiceEX() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		//-------------------------------------------
		//수정사유코드
		//-------------------------------------------
		//1-기재사항의 착오 정정, 2-공급가액의 변동, 3-재화의 환입, 4-계약의 해제, 5-내국신용장 사후개설, 6-착오에 의한 이중발행
		//-------------------------------------------
		taxInvoice.setModifyCode("6");

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setMgtNum("");        //정발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setMgtNum("");        //역발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");        //역발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");            //역발행시 필수입력

		//-------------------------------------------
		//수탁자 정보 - 위수탁 발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setMgtNum("");            //위수탁발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getBrokerParty().setCorpNum("");            //위수탁발행시 필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setCEOName("");            //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");        //위수탁발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getBrokerParty().setContactName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");            //위수탁발행시 필수입력

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		String OriginalNTSSendKey = "";        //당초 세금계산서의 국세청 승인번호
		//바로빌을 통해 발행된 세금계산서만 가능합니다.

		//-------------------------------------------

		int IssueTiming = 1;                //발행시점 : 1-공급자 직접발행, 2-공급받는자 승인시 자동발행
		//발행예정 기능을 사용한 경우에만 적용됨.

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.registModifyTaxInvoiceEX(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice, OriginalNTSSendKey, IssueTiming);            //정발행
		//int Result = BS_TI.registModifyBrokerTaxInvoiceEX(CERTKEY, taxInvoice.getBrokerParty().getCorpNum(), taxInvoice, OriginalNTSSendKey, IssueTiming);	//위수탁

		System.out.println(result);
	}

	/**
	 * UpdateTaxInvoice - 수정
	 */
	@Test
	public void UpdateTaxInvoice() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.setTaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setMgtNum("");        //정발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setMgtNum("");        //역발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");        //역발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");            //역발행시 필수입력

		//-------------------------------------------
		//수탁자 정보 - 위수탁 발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setMgtNum("");            //위수탁발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getBrokerParty().setCorpNum("");            //위수탁발행시 필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setCEOName("");            //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");        //위수탁발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getBrokerParty().setContactName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");            //위수탁발행시 필수입력

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.updateTaxInvoice(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice);            //정발행
		//int Result = BS_TI.updateTaxInvoice(CERTKEY, taxInvoice.getInvoiceeParty().getCorpNum(), taxInvoice);		//역발행
		//int Result = BS_TI.updateBrokerTaxInvoice(CERTKEY, taxInvoice.getBrokerParty().getCorpNum(), taxInvoice);	//위수탁

		System.out.println(result);
	}

	/**
	 * UpdateTaxInvoiceEX - 수정 (승인 시 자동발행 옵션 추가)
	 */
	@Test
	public void UpdateTaxInvoiceEX() throws RemoteException {

		String certKey = "";                                //인증키

		TaxInvoice taxInvoice = new TaxInvoice();

		taxInvoice.setIssueDirection(1);                    //1-정발행, 2-역발행(위수탁 세금계산서는 정발행만 허용)
		taxInvoice.setTaxInvoiceType(1);                    //1-세금계산서, 2-계산서, 4-위수탁세금계산서, 5-위수탁계산서

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//TaxInvoiceType 이 1,4 일 때 : 1-과세, 2-영세
		//TaxInvoiceType 이 2,5 일 때 : 3-면세
		//-------------------------------------------
		taxInvoice.setTaxType(1);

		taxInvoice.setTaxCalcType(1);                        //세율계산방법 : 1-절상, 2-절사, 3-반올림
		taxInvoice.setPurposeType(2);                        //1-영수, 2-청구

		taxInvoice.setKwon("");                                //별지서식 11호 상의 [권] 항목
		taxInvoice.setHo("");                                //별지서식 11호 상의 [호] 항목
		taxInvoice.setSerialNum("");                        //별지서식 11호 상의 [일련번호] 항목

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		taxInvoice.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//taxInvoice.setTaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		taxInvoice.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		taxInvoice.setTotalAmount("");

		taxInvoice.setCash("");                                //현금
		taxInvoice.setChkBill("");                            //수표
		taxInvoice.setNote("");                                //어음
		taxInvoice.setCredit("");                            //외상미수금

		taxInvoice.setRemark1("");
		taxInvoice.setRemark2("");
		taxInvoice.setRemark3("");

		taxInvoice.setWriteDate("");                        //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		//-------------------------------------------
		//공급자 정보 - 정발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoicerParty(new InvoiceParty());

		taxInvoice.getInvoicerParty().setMgtNum("");        //정발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoicerParty().setCorpNum("");        //필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getInvoicerParty().setTaxRegID("");
		taxInvoice.getInvoicerParty().setCorpName("");        //필수입력
		taxInvoice.getInvoicerParty().setCEOName("");        //필수입력
		taxInvoice.getInvoicerParty().setAddr("");
		taxInvoice.getInvoicerParty().setBizType("");
		taxInvoice.getInvoicerParty().setBizClass("");
		taxInvoice.getInvoicerParty().setContactID("");        //필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoicerParty().setContactName("");    //필수입력
		taxInvoice.getInvoicerParty().setTEL("");
		taxInvoice.getInvoicerParty().setHP("");
		taxInvoice.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보 - 역발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setInvoiceeParty(new InvoiceParty());

		taxInvoice.getInvoiceeParty().setMgtNum("");        //역발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getInvoiceeParty().setCorpNum("");        //필수입력
		taxInvoice.getInvoiceeParty().setTaxRegID("");
		taxInvoice.getInvoiceeParty().setCorpName("");        //필수입력
		taxInvoice.getInvoiceeParty().setCEOName("");        //필수입력
		taxInvoice.getInvoiceeParty().setAddr("");
		taxInvoice.getInvoiceeParty().setBizType("");
		taxInvoice.getInvoiceeParty().setBizClass("");
		taxInvoice.getInvoiceeParty().setContactID("");        //역발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getInvoiceeParty().setContactName("");    //필수입력
		taxInvoice.getInvoiceeParty().setTEL("");
		taxInvoice.getInvoiceeParty().setHP("");
		taxInvoice.getInvoiceeParty().setEmail("");            //역발행시 필수입력

		//-------------------------------------------
		//수탁자 정보 - 위수탁 발행시 세금계산서 작성자
		//-------------------------------------------
		taxInvoice.setBrokerParty(new InvoiceParty());

		taxInvoice.getBrokerParty().setMgtNum("");            //위수탁발행시 필수입력 - 연동사부여 문서키
		taxInvoice.getBrokerParty().setCorpNum("");            //위수탁발행시 필수입력 - 연계사업자 사업자번호 ('-' 제외, 10자리)
		taxInvoice.getBrokerParty().setTaxRegID("");
		taxInvoice.getBrokerParty().setCorpName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setCEOName("");            //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setAddr("");
		taxInvoice.getBrokerParty().setBizType("");
		taxInvoice.getBrokerParty().setBizClass("");
		taxInvoice.getBrokerParty().setContactID("");        //위수탁발행시 필수입력 - 담당자 바로빌 아이디
		taxInvoice.getBrokerParty().setContactName("");        //위수탁발행시 필수입력
		taxInvoice.getBrokerParty().setTEL("");
		taxInvoice.getBrokerParty().setHP("");
		taxInvoice.getBrokerParty().setEmail("");            //위수탁발행시 필수입력

		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfTaxInvoiceTradeLineItem arrayOfTaxInvoiceTradeLineItem = new ArrayOfTaxInvoiceTradeLineItem();

		for (int i = 0; i < 4; i++) {
			TaxInvoiceTradeLineItem taxInvoiceTradeLineItem = new TaxInvoiceTradeLineItem();
			taxInvoiceTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			taxInvoiceTradeLineItem.setName("");
			taxInvoiceTradeLineItem.setInformation("");
			taxInvoiceTradeLineItem.setChargeableUnit("");
			taxInvoiceTradeLineItem.setUnitPrice("");
			taxInvoiceTradeLineItem.setAmount("");
			taxInvoiceTradeLineItem.setTax("");
			taxInvoiceTradeLineItem.setDescription("");

			arrayOfTaxInvoiceTradeLineItem.getTaxInvoiceTradeLineItem().add(taxInvoiceTradeLineItem);
		}

		taxInvoice.setTaxInvoiceTradeLineItems(arrayOfTaxInvoiceTradeLineItem);
		//-------------------------------------------

		int IssueTiming = 1;            //발행시점 : 1-공급자 직접발행, 2-공급받는자 승인시 자동발행
		//발행예정 기능을 사용한 경우에만 적용됨.

		//-------------------------------------------

		int result = barobillApiService.taxInvoice.updateTaxInvoiceEX(certKey, taxInvoice.getInvoicerParty().getCorpNum(), taxInvoice, IssueTiming);        //정발행
		//int Result = BS_TI.updateTaxInvoiceEX(CERTKEY, taxInvoice.getInvoiceeParty().getCorpNum(), taxInvoice, 1);				//역발행
		//int Result = BS_TI.updateBrokerTaxInvoiceEX(CERTKEY, taxInvoice.getBrokerParty().getCorpNum(), taxInvoice, IssueTiming);	//위수탁

		System.out.println(result);
	}

	/**
	 * DeleteTaxInvoice - 삭제 (연동사부여 문서키)
	 */
	@Test
	public void DeleteTaxInvoice() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.taxInvoice.deleteTaxInvoice(certKey, corpNum, mgtKey);

		System.out.println(result);
	}

	//문서 프로세스

	/**
	 * IssueTaxInvoice - 발행처리
	 */
	@Test
	public void IssueTaxInvoice() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키
		boolean sendSms = false;        //발행 알림문자 전송여부 (발행비용과 별도로 과금됨)
		int ntsOption = 1;                //현재 사용되지 않는 항목으로 1을 입력하면 된다.
		boolean forceIssue = false;        //가산세 부과 여부에 상관없이 발행할지 여부
		String mailTitle = "";            //발행 알림메일의 제목 (공백이나 Null의 경우 바로빌 기본값으로 전송됨.)

		int result = barobillApiService.taxInvoice.issueTaxInvoice(certKey, corpNum, mgtKey, sendSms, ntsOption, forceIssue, mailTitle);

		System.out.println(result);
	}

	/**
	 * PreIssueTaxInvoice - 발행예정
	 */
	@Test
	public void PreIssueTaxInvoice() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키
		boolean sendSms = false;        //발행예정 알림문자 전송여부 (발행비용과 별도로 과금됨)
		String mailTitle = "";            //발행예정 알림메일의 제목 (공백이나 Null의 경우 바로빌 기본값으로 전송됨.)

		int result = barobillApiService.taxInvoice.preIssueTaxInvoice(certKey, corpNum, mgtKey, sendSms, mailTitle);

		System.out.println(result);
	}

	/**
	 * ProcTaxInvoice - 프로세스 처리
	 */
	@Test
	public void ProcTaxInvoice() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키
		String procType = "";            //프로세스 타입
		//CANCEL : 승인(발행)요청 취소
		//ACCEPT : 승인
		//REFUSE : 거부
		//ISSUE_CANCEL : 발행완료된 매출 세금계산서의 발행을 취소
		String memo = "";                //프로세스 처리시 거래처에 전달할 메모.

		int result = barobillApiService.taxInvoice.procTaxInvoice(certKey, corpNum, mgtKey, procType, memo);

		System.out.println(result);
	}

	/**
	 * SendToNTS - 세금계산서 국세청 즉시 전송
	 */
	@Test
	public void SendToNTS() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.taxInvoice.sendToNTS(certKey, corpNum, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetAttachedFileList - 첨부파일 목록
	 */
	@Test
	public void GetAttachedFileList() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		ArrayOfAttachedFile result = barobillApiService.taxInvoice.getAttachedFileList(certKey, corpNum, mgtKey);

		if (result.getAttachedFile().size() == 1 && result.getAttachedFile().get(0).getFileIndex() < 0) { //실패
			System.out.println(result.getAttachedFile().get(0).getFileIndex());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * AttachFileByFTP - 파일 첨부
	 */
	@Test
	public void AttachFileByFTP() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키
		String fileName = "";            //첨부할 파일명
		String displayFileName = "";    //다운로드시 보여질 파일명

		int result = barobillApiService.taxInvoice.attachFileByFTP(certKey, corpNum, mgtKey, fileName, displayFileName);

		System.out.println(result);
	}

	/**
	 * DeleteAttachFileWithFileIndex - 첨부파일 삭제
	 */
	@Test
	public void DeleteAttachFileWithFileIndex() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키
		int fileIndex = 0;                //삭제할 첨부파일의 인덱스 (GetAttachedFileList 로 확인된 인덱스)

		int result = barobillApiService.taxInvoice.deleteAttachFileWithFileIndex(certKey, corpNum, mgtKey, fileIndex);

		System.out.println(result);
	}

	/**
	 * DeleteAttachFile - 첨부파일 전체삭제
	 */
	@Test
	public void DeleteAttachFile() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.taxInvoice.deleteAttachFile(certKey, corpNum, mgtKey);

		System.out.println(result);
	}


	//문서 연결

	/**
	 * GetLinkedDocs - 연결된 문서 목록
	 */
	@Test
	public void GetLinkedDocs() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		int docType = 1;                //원본 문서의 종류 : 1-세금계산서, 2-계산서
		String mgtKey = "";                //연동사부여 문서키

		ArrayOfLinkedDoc result = barobillApiService.taxInvoice.getLinkedDocs(certKey, corpNum, docType, mgtKey);

		if (result.getLinkedDoc().size() == 1 && result.getLinkedDoc().get(0).getDocType() < 0) { //실패
			System.out.println(result.getLinkedDoc().get(0).getDocType());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * MakeDocLinkage - 문서 연결
	 */
	@Test
	public void MakeDocLinkage() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		int fromDocType = 1;            //원본 문서의 종류 : 1-세금계산서, 2-계산서
		String fromMgtKey = "";            //원본 연동사부여 문서키
		int toDocType = 1;                //대상 문서의 종류 : 1-세금계산서, 2-계산서, 3-전자문서
		String toMgtKey = "";            //대상 연동사부여 문서키

		int result = barobillApiService.taxInvoice.makeDocLinkage(certKey, corpNum, fromDocType, fromMgtKey, toDocType, toMgtKey);

		System.out.println(result);
	}

	/**
	 * RemoveDocLinkage - 문서 연결 해제
	 */
	@Test
	public void RemoveDocLinkage() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		int fromDocType = 1;            //원본 문서의 종류 : 1-세금계산서, 2-계산서
		String fromMgtKey = "";            //원본 연동사부여 문서키
		int toDocType = 1;                //대상 문서의 종류 : 1-세금계산서, 2-계산서, 3-전자문서
		String toMgtKey = "";            //대상 연동사부여 문서키

		int result = barobillApiService.taxInvoice.removeDocLinkage(certKey, corpNum, fromDocType, fromMgtKey, toDocType, toMgtKey);

		System.out.println(result);
	}


	//문서 정보

	/**
	 * GetTaxInvoice - 문서 정보 (연동사부여 문서키)
	 */
	@Test
	public void GetTaxInvoice() throws RemoteException {

		String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
		String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "asefdasfedfasdfewq";                //연동사부여 문서키

		TaxInvoice result = barobillApiService.taxInvoice.getTaxInvoice(certKey, corpNum, mgtKey);

		if (result.getTaxInvoiceType() < 0) { //실패
			System.out.println(result.getTaxInvoiceType());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetTaxInvoiceLog - 문서 이력 (연동사부여 문서키)
	 */
	@Test
	public void GetTaxInvoiceLog() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		ArrayOfInvoiceLog result = barobillApiService.taxInvoice.getTaxInvoiceLog(certKey, corpNum, mgtKey);

		if (result.getInvoiceLog().get(0).getSeq() < 0) { //실패
			System.out.println(result.getInvoiceLog().get(0).getSeq());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetTaxInvoiceState - 문서 상태
	 */
	@Test
	public void GetTaxInvoiceState() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		TaxInvoiceState result = barobillApiService.taxInvoice.getTaxInvoiceState(certKey, corpNum, mgtKey);

		if (result.getBarobillState() < 0) { //실패
			System.out.println(result.getBarobillState());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetTaxInvoiceStates - 문서 상태(대량, 100건 까지) (연동사부여 문서키)
	 */
	@Test
	public void GetTaxInvoiceStates() throws RemoteException {

		String certKey = "";                    //인증키
		String corpNum = "";                    //연계사업자 사업자번호 ('-' 제외, 10자리)

		ArrayOfString mgtKeyList = new ArrayOfString();    //연동사부여 문서키 배열
		mgtKeyList.getString().add("");
        mgtKeyList.getString().add("");

		ArrayOfTaxInvoiceState result = barobillApiService.taxInvoice.getTaxInvoiceStates(certKey, corpNum, mgtKeyList);

		for (TaxInvoiceState r : result.getTaxInvoiceState()) {
			if (r.getBarobillState() < 0) { //실패
				System.out.println(r.getBarobillState());
			} else { //성공
				System.out.println(r);
			}
			System.out.println("<br>");
		}
	}

	/**
	 * GetTaxInvoiceStateEX - 문서 상태 (수신확인, 등록일시, 작성일자, 발행예정일시, 발행일시 추가)
	 */
	@Test
	public void GetTaxInvoiceStateEX() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		TaxInvoiceStateEX result = barobillApiService.taxInvoice.getTaxInvoiceStateEX(certKey, corpNum, mgtKey);

		if (result.getBarobillState() < 0) { //실패
			System.out.println(result.getBarobillState());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetTaxInvoiceStatesEX - 문서 상태 (수신확인, 등록일시, 작성일자, 발행예정일시, 발행일시 추가) (대량, 100건 까지)
	 */
	@Test
	public void GetTaxInvoiceStatesEX() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)

        ArrayOfString mgtKeyList = new ArrayOfString();    //연동사부여 문서키 배열
        mgtKeyList.getString().add("");
        mgtKeyList.getString().add("");

		ArrayOfTaxInvoiceStateEX result = barobillApiService.taxInvoice.getTaxInvoiceStatesEX(certKey, corpNum, mgtKeyList);

		for (TaxInvoiceStateEX r : result.getTaxInvoiceStateEX()) {
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
	 * ReSendEmail - 이메일 전송
	 */
	@Test
	public void ReSendEmail() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키
		String toEmailAddress = "";        //수신자 메일 주소

		int result = barobillApiService.taxInvoice.reSendEmail(certKey, corpNum, mgtKey, toEmailAddress);

		System.out.println(result);
	}

	/**
	 * ReSendSMS - 문자 재전송
	 */
	@Test
	public void ReSendSMS() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String senderId = "";            //연계사업자 아이디
		String fromNumber = "";            //발신자 휴대폰 번호
		String toCorpName = "";            //수신자 회사명
		String toName = "";                //수신자 이름
		String toNumber = "";            //수신자 휴대폰 번호
		String contents = "";            //문자메세지 내용

		int result = barobillApiService.taxInvoice.reSendSMS(certKey, corpNum, senderId, fromNumber, toCorpName, toName, toNumber, contents);

		System.out.println(result);
	}

	/**
	 * SendInvoiceSMS - 문자 전송 (문서이력에 기록됨)
	 */
	@Test
	public void SendInvoiceSMS() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String senderId = "";            //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		String fromNumber = "";            //발신자 휴대폰 번호
		String toNumber = "";            //수신자 휴대폰 번호
		String contents = "";            //문자메세지 내용

		int result = barobillApiService.taxInvoice.sendInvoiceSMS(certKey, corpNum, senderId, mgtKey, fromNumber, toNumber, contents);

		System.out.println(result);
	}

	/**
	 * SendInvoiceFax - 팩스 전송 (문서이력에 기록됨)
	 */
	@Test
	public void SendInvoiceFax() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키
		String senderId = "";            //연계사업자 아이디
		String fromFaxNumber = "";        //발신자 팩스 번호
		String toFaxNumber = "";        //수신자 팩스 번호

		int result = barobillApiService.taxInvoice.sendInvoiceFax(certKey, corpNum, mgtKey, senderId, fromFaxNumber, toFaxNumber);

		System.out.println(result);
	}


	//국세청 전송설정

	/**
	 * GetNTSSendOption - 국세청 전송설정 확인
	 */
	@Test
	public void GetNTSSendOption() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)

		NTSSendOption result = barobillApiService.taxInvoice.getNTSSendOption(certKey, corpNum);

		if (result.getTaxationOption() < 0) { //실패
			System.out.println(result.getTaxationOption());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * ChangeNTSSendOption - 국세청 전송설정 변경
	 */
	@Test
	public void ChangeNTSSendOption() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디

		NTSSendOption ntsSendOption = new NTSSendOption();

		//-------------------------------------------
		//과세, 영세 국세청 전송설정
		//-------------------------------------------
		//1-발행 익일 자동전송, 2-발행 즉시 전송
		//-------------------------------------------
		ntsSendOption.setTaxationOption(1);

		//-------------------------------------------
		//과세, 영세 가산세 허용여부
		//-------------------------------------------
		//1-허용, 0-차단
		//-------------------------------------------
		ntsSendOption.setTaxationAddTaxAllowYN(1);

		//-------------------------------------------
		//면세 국세청 전송설정
		//-------------------------------------------
		//1-발행 익일 자동전송, 2-발행 즉시 전송, 3-수동 전송
		//-------------------------------------------
		ntsSendOption.setTaxExemptionOption(1);

		//-------------------------------------------
		//면세 가산세 허용여부
		//-------------------------------------------
		//1-허용, 0-차단
		//-------------------------------------------
		ntsSendOption.setTaxExemptionAddTaxAllowYN(1);

		int result = barobillApiService.taxInvoice.changeNTSSendOption(certKey, corpNum, id, ntsSendOption);

		System.out.println(result);
	}


	//기타

	/**
	 * CheckMgtNumIsExists - 연동사부여 문서키 사용여부 확인
	 */
	@Test
	public void CheckMgtNumIsExists() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.taxInvoice.checkMgtNumIsExists(certKey, corpNum, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetTaxInvoicePopUpURL - 문서 내용보기 팝업 URL (연동사부여 문서키)
	 */
	@Test
	public void GetTaxInvoicePopUpURL() throws RemoteException {

		String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
		String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "asefdasfedfasdfewq";                //연동사부여 문서키
		String id = "timflabs";                    //연계사업자 아이디
		String pwd = "Timf180525!";                //연계사업자 비밀번호

		String result = barobillApiService.taxInvoice.getTaxInvoicePopUpURL(certKey, corpNum, mgtKey, id, pwd);

		System.out.println(result);
	}

	/**
	 * GetTaxInvoicePrintURL - 인쇄 팝업 URL (연동사부여 문서키)
	 */
	@Test
	public void GetTaxInvoicePrintURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키
		String id = "";                    //연계사업자 아이디
		String pwd = "";                //연계사업자 비밀번호

		String result = barobillApiService.taxInvoice.getTaxInvoicePrintURL(certKey, corpNum, mgtKey, id, pwd);

		System.out.println(result);
	}

	/**
	 * GetTaxInvoicesPrintURL - 대량인쇄 팝업 URL
	 */
	@Test
	public void GetTaxInvoicesPrintURL() throws RemoteException {

		String certKey = "330E5BFA-0EAB-46F5-ADBE-14134EF67FE9";            //인증키
		String corpNum = "5618801138";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "timflabs";                    //연계사업자 아이디
		String pwd = "Timf180525!";    

        ArrayOfString mgtKeyList = new ArrayOfString();    //연동사부여 문서키 배열
        mgtKeyList.getString().add("asefdasfedfasdfewq");
        mgtKeyList.getString().add("1");
        mgtKeyList.getString().add("2");
        mgtKeyList.getString().add("3");

		String result = barobillApiService.taxInvoice.getTaxInvoicesPrintURL(certKey, corpNum, mgtKeyList, id, pwd);

		System.out.println(result);
	}

	/**
	 * GetTaxInvoiceMailURL - 이메일의 보기버튼 URL
	 */
	@Test
	public void GetTaxInvoiceMailURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String mgtKey = "";                //연동사부여 문서키

		String result = barobillApiService.taxInvoice.getTaxinvoiceMailURL(certKey, corpNum, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetEmailPublicKeys - ASP업체 Email 목록확인
	 */
	@Test
	public void GetEmailPublicKeys() throws RemoteException {

		String certKey = "";            //인증키

		ArrayOfEMAILPUBLICKEY result = barobillApiService.taxInvoice.getEmailPublicKeys(certKey);

		int intResult = 0;

		try {
			if (result.getEMAILPUBLICKEY().size() == 1) {
				intResult = Integer.parseInt(result.getEMAILPUBLICKEY().get(0).getPK());
			}
		} catch (NumberFormatException e) {
		}

		if (intResult < 0) { //실패
			System.out.println(intResult);
		} else { //성공
			System.out.println(result);
		}
	}
}
