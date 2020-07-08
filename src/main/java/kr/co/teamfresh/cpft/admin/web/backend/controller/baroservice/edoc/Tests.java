package kr.co.teamfresh.cpft.admin.web.backend.controller.baroservice.edoc;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfAttachedFile;
import com.baroservice.ws.ArrayOfEDocProperty;
import com.baroservice.ws.ArrayOfEDocState;
import com.baroservice.ws.ArrayOfEDocTradeLineItem;
import com.baroservice.ws.ArrayOfInvoiceLog;
import com.baroservice.ws.ArrayOfLinkedDoc;
import com.baroservice.ws.ArrayOfString;
import com.baroservice.ws.EDoc;
import com.baroservice.ws.EDocParty;
import com.baroservice.ws.EDocProperty;
import com.baroservice.ws.EDocState;
import com.baroservice.ws.EDocTradeLineItem;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * 바로빌 전자문서 API 샘플
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
	 * RegistEDoc - 등록
	 */
	@Test
	public void RegistEDoc() throws RemoteException {

		String certKey = "";                            //인증키

		EDoc edoc = new EDoc();
		edoc.setMgtKey("");                                //연동사부여 문서키
		edoc.setUserID("");                                //연계사업자 아이디 - 공급자 사업자번호(edoc.InvoicerParty.CorpNum)의 담당자 아이디

		//-------------------------------------------
		//문서양식 키
		//-------------------------------------------
		//바로빌 문서양식 또는 커스텀 문서양식의 키
		//바로빌 기본 문서양식 키
		//거래명세서 : FK901201207001
		//입금표 : FK902201207001
		//청구서 : FK903201207001
		//견적서 : FK904201207001
		//영수증 : FK905201207001
		//발주서 : FK906201207001
		//-------------------------------------------
		edoc.setFormKey("FK901201207001");

		edoc.setEDocInvoiceType(1);                        //1-거래명세서, 2-입금표, 3-청구서, 4-견적서, 5-영수증, 6-발주서

		edoc.setWriteDate("");                            //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		edoc.setCertYN(false);                            //발행 시 공인인증서 확인 여부
		edoc.setAutoAcceptYN(false);                    //발행 시 자동승인처리 여부
		edoc.setBusinessLicenseYN(false);                //사업자등록증 첨부 여부
		edoc.setBankBookYN(false);                        //통장사본 첨부 여부

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//거래명세서 : 1-과세, 2-영세, 3-면세
		//입금표 : 1-과세, 3-면세
		//그 외 : 0
		//-------------------------------------------
		edoc.setTaxType(1);

		//-------------------------------------------
		//영수,청구
		//-------------------------------------------
		//거래명세서 : 1-영수, 2-청구
		//입금표, 영수증 : 1
		//청구서 : 2
		//견적서, 발주서 : 0
		//-------------------------------------------
		edoc.setPurposeType(2);

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		edoc.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//edoc.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		edoc.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		edoc.setTotalAmount("");

		edoc.setRemark1("");
		edoc.setRemark2("");
		edoc.setRemark3("");
		edoc.setSerialNum("");

		//-------------------------------------------
		//공급자 정보
		//-------------------------------------------
		edoc.setInvoicerParty(new EDocParty());

		edoc.getInvoicerParty().setCorpNum("");            //필수입력 (연계사업자 사업자번호) ('-' 제외, 10자리)
		edoc.getInvoicerParty().setTaxRegID("");
		edoc.getInvoicerParty().setCorpName("");        //필수입력
		edoc.getInvoicerParty().setCEOName("");            //필수입력
		edoc.getInvoicerParty().setAddr("");
		edoc.getInvoicerParty().setBizType("");
		edoc.getInvoicerParty().setBizClass("");
		edoc.getInvoicerParty().setContactName("");        //필수입력
		edoc.getInvoicerParty().setDeptName("");
		edoc.getInvoicerParty().setTEL("");
		edoc.getInvoicerParty().setHP("");
		edoc.getInvoicerParty().setFAX("");
		edoc.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보
		//-------------------------------------------
		edoc.setInvoiceeParty(new EDocParty());

		edoc.getInvoiceeParty().setCorpNum("");
		edoc.getInvoiceeParty().setTaxRegID("");
		edoc.getInvoiceeParty().setCorpName("");        //필수입력
		edoc.getInvoiceeParty().setCEOName("");
		edoc.getInvoiceeParty().setAddr("");
		edoc.getInvoiceeParty().setBizType("");
		edoc.getInvoiceeParty().setBizClass("");
		edoc.getInvoiceeParty().setContactName("");
		edoc.getInvoiceeParty().setDeptName("");
		edoc.getInvoiceeParty().setTEL("");
		edoc.getInvoiceeParty().setHP("");
		edoc.getInvoiceeParty().setFAX("");
		edoc.getInvoiceeParty().setEmail("");

		//-------------------------------------------
		//속성
		//-------------------------------------------
		//아래 3가지 속성은 "바로빌 기본 거래명세서 양식"의 속성입니다.
		//-------------------------------------------
        ArrayOfEDocProperty arrayOfEDocProperty = new ArrayOfEDocProperty();

        arrayOfEDocProperty.getEDocProperty().add(new EDocProperty());
        arrayOfEDocProperty.getEDocProperty().get(0).setName("Balance");
        arrayOfEDocProperty.getEDocProperty().get(0).setValue("");

        arrayOfEDocProperty.getEDocProperty().add(new EDocProperty());
        arrayOfEDocProperty.getEDocProperty().get(1).setName("Deposit");
        arrayOfEDocProperty.getEDocProperty().get(1).setValue("");

        arrayOfEDocProperty.getEDocProperty().add(new EDocProperty());
        arrayOfEDocProperty.getEDocProperty().get(2).setName("CBalance");
        arrayOfEDocProperty.getEDocProperty().get(2).setValue("");

        edoc.setEDocProperties(arrayOfEDocProperty);
		//-------------------------------------------
		//품목
		//-------------------------------------------
		ArrayOfEDocTradeLineItem arrayOfEDocTradeLineItem = new ArrayOfEDocTradeLineItem();

		for (int i = 0; i < 6; i++) {
			EDocTradeLineItem eDocTradeLineItem = new EDocTradeLineItem();
			eDocTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
			eDocTradeLineItem.setName("");
			eDocTradeLineItem.setInformation("");
			eDocTradeLineItem.setChargeableUnit("");
			eDocTradeLineItem.setUnitPrice("");
			eDocTradeLineItem.setAmount("");
			eDocTradeLineItem.setTax("");
			eDocTradeLineItem.setDescription("");
		}

		edoc.setEDocTradeLineItems(arrayOfEDocTradeLineItem);

		//-------------------------------------------

		int result = barobillApiService.edoc.registEDoc(certKey, edoc.getInvoicerParty().getCorpNum(), edoc.getUserID(), edoc);

		System.out.println(result);
	}

	/**
	 * UpdateEDoc - 수정
	 */
	@Test
	public void UpdateEDoc() throws RemoteException {

		String certKey = "";                            //인증키

		EDoc edoc = new EDoc();
		edoc.setMgtKey("");                                //연동사부여 문서키
		edoc.setUserID("");                                //연계사업자 아이디 - 공급자 사업자번호(edoc.InvoicerParty.CorpNum)의 담당자 아이디

		edoc.setWriteDate("");                            //작성일자 (YYYYMMDD), 공백입력 시 Today로 작성됨.

		edoc.setCertYN(false);                            //발행 시 공인인증서 확인 여부
		edoc.setAutoAcceptYN(false);                    //발행 시 자동승인처리 여부
		edoc.setBusinessLicenseYN(false);                //사업자등록증 첨부 여부
		edoc.setBankBookYN(false);                        //통장사본 첨부 여부

		//-------------------------------------------
		//과세형태
		//-------------------------------------------
		//거래명세서 : 1-과세, 2-영세, 3-면세
		//입금표 : 1-과세, 3-면세
		//그 외 : 0
		//-------------------------------------------
		edoc.setTaxType(2);

		//-------------------------------------------
		//영수,청구
		//-------------------------------------------
		//거래명세서 : 1-영수, 2-청구
		//입금표, 영수증 : 1
		//청구서 : 2
		//견적서, 발주서 : 0
		//-------------------------------------------
		edoc.setPurposeType(1);

		//-------------------------------------------
		//공급가액 총액
		//-------------------------------------------
		edoc.setAmountTotal("");

		//-------------------------------------------
		//세액합계
		//-------------------------------------------
		//edoc.TaxType 이 2 또는 3 으로 셋팅된 경우 0으로 입력
		//-------------------------------------------
		edoc.setTaxTotal("");

		//-------------------------------------------
		//합계금액
		//-------------------------------------------
		//공급가액 총액 + 세액합계 와 일치해야 합니다.
		//-------------------------------------------
		edoc.setTotalAmount("");

		edoc.setRemark1("");
		edoc.setRemark2("");
		edoc.setRemark3("");
		edoc.setSerialNum("");

		//-------------------------------------------
		//공급자 정보
		//-------------------------------------------
		edoc.setInvoicerParty(new EDocParty());

		edoc.getInvoicerParty().setCorpNum("");            //필수입력 (연계사업자 사업자번호) ('-' 제외, 10자리)
		edoc.getInvoicerParty().setTaxRegID("");
		edoc.getInvoicerParty().setCorpName("");        //필수입력
		edoc.getInvoicerParty().setCEOName("");            //필수입력
		edoc.getInvoicerParty().setAddr("");
		edoc.getInvoicerParty().setBizType("");
		edoc.getInvoicerParty().setBizClass("");
		edoc.getInvoicerParty().setContactName("");        //필수입력
		edoc.getInvoicerParty().setDeptName("");
		edoc.getInvoicerParty().setTEL("");
		edoc.getInvoicerParty().setHP("");
		edoc.getInvoicerParty().setFAX("");
		edoc.getInvoicerParty().setEmail("");            //필수입력

		//-------------------------------------------
		//공급받는자 정보
		//-------------------------------------------
		edoc.setInvoiceeParty(new EDocParty());

		edoc.getInvoiceeParty().setCorpNum("");
		edoc.getInvoiceeParty().setTaxRegID("");
		edoc.getInvoiceeParty().setCorpName("");        //필수입력
		edoc.getInvoiceeParty().setCEOName("");
		edoc.getInvoiceeParty().setAddr("");
		edoc.getInvoiceeParty().setBizType("");
		edoc.getInvoiceeParty().setBizClass("");
		edoc.getInvoiceeParty().setContactName("");
		edoc.getInvoiceeParty().setDeptName("");
		edoc.getInvoiceeParty().setTEL("");
		edoc.getInvoiceeParty().setHP("");
		edoc.getInvoiceeParty().setFAX("");
		edoc.getInvoiceeParty().setEmail("");

		//-------------------------------------------
		//속성
		//-------------------------------------------
		//아래 3가지 속성은 "바로빌 기본 거래명세서 양식"의 속성입니다.
		//-------------------------------------------
        ArrayOfEDocProperty arrayOfEDocProperty = new ArrayOfEDocProperty();

        arrayOfEDocProperty.getEDocProperty().add(new EDocProperty());
        arrayOfEDocProperty.getEDocProperty().get(0).setName("Balance");
        arrayOfEDocProperty.getEDocProperty().get(0).setValue("");

        arrayOfEDocProperty.getEDocProperty().add(new EDocProperty());
        arrayOfEDocProperty.getEDocProperty().get(1).setName("Deposit");
        arrayOfEDocProperty.getEDocProperty().get(1).setValue("");

        arrayOfEDocProperty.getEDocProperty().add(new EDocProperty());
        arrayOfEDocProperty.getEDocProperty().get(2).setName("CBalance");
        arrayOfEDocProperty.getEDocProperty().get(2).setValue("");

        edoc.setEDocProperties(arrayOfEDocProperty);

		//-------------------------------------------
		//품목
		//-------------------------------------------
        ArrayOfEDocTradeLineItem arrayOfEDocTradeLineItem = new ArrayOfEDocTradeLineItem();

        for (int i = 0; i < 6; i++) {
            EDocTradeLineItem eDocTradeLineItem = new EDocTradeLineItem();
            eDocTradeLineItem.setPurchaseExpiry("");        //YYYYMMDD
            eDocTradeLineItem.setName("");
            eDocTradeLineItem.setInformation("");
            eDocTradeLineItem.setChargeableUnit("");
            eDocTradeLineItem.setUnitPrice("");
            eDocTradeLineItem.setAmount("");
            eDocTradeLineItem.setTax("");
            eDocTradeLineItem.setDescription("");
        }

        edoc.setEDocTradeLineItems(arrayOfEDocTradeLineItem);

		//-------------------------------------------

		int result = barobillApiService.edoc.updateEDoc(certKey, edoc.getInvoicerParty().getCorpNum(), edoc.getUserID(), edoc);

		System.out.println(result);
	}

	/**
	 * IssueEDoc - 발행
	 */
	@Test
	public void IssueEDoc() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		boolean smsSendYn = false;        //발행 알림문자 전송여부 (발행비용과 별도로 과금됨)
		String memo = "";                //발행 시 공급받는자에게 전달할 메모.
		String mailTitle = "";            //발행 알림메일의 제목 (공백이나 Null의 경우 바로빌 기본값으로 전송됨.)

		int result = barobillApiService.edoc.issueEDoc(certKey, corpNum, id, mgtKey, smsSendYn, memo, mailTitle);

		System.out.println(result);
	}

	/**
	 * CancelEDoc - 발행취소
	 */
	@Test
	public void CancelEDoc() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		String memo = "";                //발행취소 시 공급받는자에게 전달할 메모.

		int result = barobillApiService.edoc.cancelEDoc(certKey, corpNum, id, mgtKey, memo);

		System.out.println(result);
	}

	/**
	 * DeleteEDoc - 삭제
	 */
	@Test
	public void DeleteEDoc() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.edoc.deleteEDoc(certKey, corpNum, id, mgtKey);

		System.out.println(result);
	}

	//첨부파일

	/**
	 * GetAttachedFileList - 첨부파일 목록
	 */
	@Test
	public void GetAttachedFileList() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		ArrayOfAttachedFile result = barobillApiService.edoc.getAttachedFileList(certKey, corpNum, id, mgtKey);

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
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		String fileName = "";            //첨부할 파일명
		String displayFileName = "";    //다운로드시 보여질 파일명

		int result = barobillApiService.edoc.attachFileByFTP(certKey, corpNum, id, mgtKey, fileName, displayFileName);

		System.out.println(result);
	}

	/**
	 * DeleteAttachFileWithFileIndex - 첨부파일 삭제
	 */
	@Test
	public void DeleteAttachFileWithFileIndex() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키
		int fileIndex = 1;                //삭제할 첨부파일의 인덱스 (GetAttachedFileList 로 확인된 인덱스)

		int result = barobillApiService.edoc.deleteAttachFileWithFileIndex(certKey, corpNum, id, mgtKey, fileIndex);

		System.out.println(result);
	}

	/**
	 * DeleteAttachFile - 첨부파일 전체삭제
	 */
	@Test
	public void DeleteAttachFile() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		int result = barobillApiService.edoc.deleteAttachFile(certKey, corpNum, id, mgtKey);

		System.out.println(result);
	}

	//문서연결

	/**
	 * GetLinkedDocs - 연결된 문서 목록
	 */
	@Test
	public void GetLinkedDocs() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		int docType = 3;                //원본 문서의 종류 : 3-전자문서
		String mgtKey = "";                //연동사부여 문서키

		ArrayOfLinkedDoc result = barobillApiService.edoc.getLinkedDocs(certKey, corpNum, docType, mgtKey);

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
		String fromMgtKey = "";            //원본 연동사부여 문서키
		int fromDocType = 3;            //원본 문서의 종류 : 3-전자문서
		int toDocType = 3;                //대상 문서의 종류 : 1-세금계산서, 2-계산서, 3-전자문서
		String toMgtKey = "";            //대상 연동사부여 문서키

		int result = barobillApiService.edoc.makeDocLinkage(certKey, corpNum, fromDocType, fromMgtKey, toDocType, toMgtKey);

		System.out.println(result);
	}

	/**
	 * RemoveDocLinkage - 문서 연결 해제
	 */
	@Test
	public void RemoveDocLinkage() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		int fromDocType = 3;            //원본 문서의 종류 : 3-전자문서
		String fromMgtKey = "";            //원본 연동사부여 문서키
		int toDocType = 3;                //대상 문서의 종류 : 1-세금계산서, 2-계산서, 3-전자문서
		String toMgtKey = "";            //대상 연동사부여 문서키

		int result = barobillApiService.edoc.removeDocLinkage(certKey, corpNum, fromDocType, fromMgtKey, toDocType, toMgtKey);

		System.out.println(result);
	}

	//문서 정보

	/**
	 * GetEDoc - 문서 정보
	 */
	@Test
	public void GetEDoc() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		EDoc result = barobillApiService.edoc.getEDoc(certKey, corpNum, id, mgtKey);

		if (result.getEDocInvoiceType() < 0) { //실패
			System.out.println(result.getEDocInvoiceType());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetEDocLog - 문서 이력
	 */
	@Test
	public void GetEDocLog() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		ArrayOfInvoiceLog result = barobillApiService.edoc.getEDocLog(certKey, corpNum, id, mgtKey);

		if (result.getInvoiceLog().get(0).getSeq() < 0) { //실패
			System.out.println(result.getInvoiceLog().get(0).getSeq());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetEDocState - 문서 상태
	 */
	@Test
	public void GetEDocState() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		EDocState result = barobillApiService.edoc.getEDocState(certKey, corpNum, id, mgtKey);

		if (result.getBarobillState() < 0) { //실패
			System.out.println(result.getBarobillState());
		} else { //성공
			System.out.println(result);
		}
	}

	/**
	 * GetEDocStates - 문서 상태(대량, 100건 까지)
	 */
	@Test
	public void GetEDocStates() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디

		ArrayOfString mgtKeyList = new ArrayOfString();        //연동사부여 문서키 배열
		mgtKeyList.getString().add("");
        mgtKeyList.getString().add("");

		ArrayOfEDocState result = barobillApiService.edoc.getEDocStates(certKey, corpNum, id, mgtKeyList);

		for (EDocState r : result.getEDocState()) {
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

		int result = barobillApiService.edoc.sendEmail(certKey, corpNum, id, mgtKey, toEmailAddress);

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

		int result = barobillApiService.edoc.sendSMS(certKey, corpNum, id, mgtKey, fromNumber, toNumber, contents);

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

		int result = barobillApiService.edoc.sendFax(certKey, corpNum, id, mgtKey, fromFaxNumber, toFaxNumber);

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

		int result = barobillApiService.edoc.checkMgtKeyIsExists(certKey, corpNum, id, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetEDocPopUpURL - 문서 내용보기 팝업 URL
	 */
	@Test
	public void GetEDocPopUpURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String pwd = "";                //비밀번호
		String mgtKey = "";                //연동사부여 문서키

		String result = barobillApiService.edoc.getEDocPopUpURL(certKey, corpNum, id, pwd, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetEDocPrintURL - 인쇄 팝업 URL
	 */
	@Test
	public void GetEDocPrintURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String pwd = "";                //비밀번호
		String mgtKey = "";                //연동사부여 문서키

		String result = barobillApiService.edoc.getEDocPrintURL(certKey, corpNum, id, pwd, mgtKey);

		System.out.println(result);
	}

	/**
	 * GetEDocsPrintURL - 대량인쇄 팝업 URL
	 */
	@Test
	public void GetEDocsPrintURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String pwd = "";                //비밀번호

        ArrayOfString mgtKeyList = new ArrayOfString();        //연동사부여 문서키 배열
        mgtKeyList.getString().add("");
        mgtKeyList.getString().add("");

		String result = barobillApiService.edoc.getEDocsPrintURL(certKey, corpNum, id, pwd, mgtKeyList);

		System.out.println(result);
	}

	/**
	 * GetEDocMailURL - 이메일의 보기버튼 URL
	 */
	@Test
	public void GetEDocMailURL() throws RemoteException {

		String certKey = "";            //인증키
		String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)
		String id = "";                    //연계사업자 아이디
		String mgtKey = "";                //연동사부여 문서키

		String result = barobillApiService.edoc.getEDocMailURL(certKey, corpNum, id, mgtKey);

		System.out.println(result);
	}
}
