import React from "react";
import { connect } from "react-redux";
import MainStructure from "components/structure/MainStructure";
import "./OrderRegist.css";
import { Component } from "react";
import axios from "axios";
import $ from "jquery";
import "util/Common";
$.validate = require("jquery-validation");

export const VALIDATION_ORDER_REGIST_FORM = {
 rules: {
  rcritMans: {
   required: true,
  },
  dlvyPrdlst: {
   required: true,
  },
  payAmt: {
   required: true,
  },
  opratSctn: {
   required: true,
  },
  workingDaysType: {
   required: true,
  },
  detailMatter: {
   required: true,
  },
  rcritType: {
   required: true,
  },
  workDay: {
   required: true,
  },
  tonType: {
   required: true,
  },
 },
 submitHandler: function (form) {
  alert("valid form submitted");
  return false;
 },
 errorPlacement: function (error, element) {
  var label = $("label[for='" + $(element).attr("name") + "']");

  $(error).addClass("col-12");
  $(error).addClass("row");
  if ($(label).hasClass("text-sm-right")) {
   $(label).addClass("align-items-sm-end");
  }
  label.append(error);

  //$(error).wrapInner('<div class="col-12"/>');

  /*
    var lastError = $(element).data('lastError'),
        newError = $(error).text();
    
    $(element).data('lastError', newError);
                    
    if(newError !== '' && newError !== lastError){
        $(element).tooltipster('content', newError);
        $(element).tooltipster('show');
    } 
    */
 },
 success: function (label, element) {
  /*$(element).tooltipster('hide');*/
 },
};

class OrderRegist extends Component {
 constructor(props) {
  super(props);
  this.state = {
   jusos: null,
   loading: false,
   registOrderWorkingAreaEtcMatterToggle: false,
   orderRegistWorkTypeValue: "",
   workGroups: [],
   workGroupManagers: [],
   order: null,
  };
  this.orderRegistWorkGroupRef = React.createRef();
  this._saveTempOrderRegist = (e) => {
   $("#saveTempModalPopup").modal("hide");
   const userInfo = JSON.parse(localStorage.getItem("userInfo"));
   const formObj = $("#orderRegistForm").serializeObject();
   if (!Array.isArray(formObj.carTypes)) {
    formObj.carTypes = [formObj.carTypes];
   }
   if (!Array.isArray(formObj.workDays)) {
    formObj.workDays = [formObj.workDays];
   }
   formObj.status = "0703";
   axios
    .post("/v1/order", formObj, {
     headers: {
      "Content-Type": "application/json",
      "X-AUTH-TOKEN": userInfo.token,
     },
    })
    .then((res) => {
     if (res.status == 200) {
      if (res.data.data.orderSeq > 0) {
       this.setState({
        order: null,
       });
       $("#orderRegistForm input[type=text]").val("");
       $("#orderRegistForm select").val("");
       $("#orderRegistForm input[type=checkbox]").prop("checked", false);
       $("#orderRegistForm input[type=radio]").prop("checked", false);
       //document.getElementById("orderRegistForm").reset();
       $("#saveCompleteModalPopup").modal();
      }
     } else {
      $("#saveFailModalPopup").modal();
     }
    });
  };
  this._saveCompleteOrderRegist = (e) => {
   $("#saveModalPopup").modal("hide");
   const userInfo = JSON.parse(localStorage.getItem("userInfo"));
   const formObj = $("#orderRegistForm").serializeObject();
   if (!Array.isArray(formObj.carTypes)) {
    formObj.carTypes = [formObj.carTypes];
   }
   if (!Array.isArray(formObj.workDays)) {
    formObj.workDays = [formObj.workDays];
   }
   formObj.status = "0701";
   axios
    .post("/v1/order", formObj, {
     headers: {
      "Content-Type": "application/json",
      "X-AUTH-TOKEN": userInfo.token,
     },
    })
    .then((res) => {
     if (res.status == 200) {
      //console.log(res.data.data.orderSeq);
      if (res.data.data.orderSeq > 0) {
       this.setState({
        order: null,
       });
       $("#orderRegistForm input[type=text]").val("");
       $("#orderRegistForm select").val("");
       $("#orderRegistForm input[type=checkbox]").prop("checked", false);
       $("#orderRegistForm input[type=radio]").prop("checked", false);
       //document.getElementById("orderRegistForm").reset();
       $("#saveCompleteModalPopup").modal();
      }
     } else {
      $("#saveFailModalPopup").modal();
     }
    });
  };
  this._noValidateOrderRegistForm = (e) => {
   $("#orderRegistForm").validate(VALIDATION_ORDER_REGIST_FORM);
   var valid = $("#orderRegistForm").valid();
   if (valid) {
    $("#saveTempModalPopup").modal();
   }
  };
  this._validateOrderRegistForm = (e) => {
   /*
    $('#orderRegistForm input').tooltipster({
      theme: 'tooltipster-shadow',
        trigger: 'custom',
        onlyOne: false,
        position: 'right'
    });
    */

   $("#orderRegistForm").validate(VALIDATION_ORDER_REGIST_FORM);
   var valid = $("#orderRegistForm").valid();
   if (valid) {
    $("#saveModalPopup").modal();
   }
  };
  this._changeWorkGroup = (e) => {
   //console.log($("option:selected", e.target).data("managers"));
   //$("#workGroupManager").val($("option:selected", e.target).data("managers"));
   $("#workGroupManager").html("");
   var workGroupManagers = $("option:selected", e.target)
    .data("managers")
    .split(",");

   workGroupManagers.map((e1, i) => {
    $("#workGroupManager").append(new Option(`${e1}`, `${e1}`));
   });
  };
  this._getDaumAddressFinder = (event) => {
   new daum.Postcode({
    oncomplete: function (data) {
     $("#workingArea").val(data.address);
     // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
     // 예제를 참고하여 다양한 활용법을 확인해 보세요.
    },
   }).open();
  };
  this._setorderRegistWorkType = (event) => {
   switch (event.target.value) {
    case "":
     $("[id^=06]").prop("checked", false);
     $("[id^=06]").attr("disabled", true);
     break;
    case "fiveDay":
     $("[id^=06]").prop("checked", true);
     $("[id^=06]").attr("disabled", false);
     $("[id=0606]").prop("checked", false);
     $("[id=0607]").prop("checked", false);
     break;
    case "sixDay":
     $("[id^=06]").prop("checked", true);
     $("[id^=06]").attr("disabled", false);
     $("[id=0607]").prop("checked", false);
     break;
    case "directCheck":
     $("[id^=06]").prop("checked", false);
     $("[id^=06]").attr("disabled", false);
     break;
    default:
     break;
   }
  };
  this._setRegistOrderWorkingAreaEtcMatterToggle = (event) => {
   this.setState({
    registOrderWorkingAreaEtcMatterToggle: event.target.checked,
   });
  };
  /*
  this._onChangeHandler = async (e) => {
   $("#registOrderWorkingArea").siblings("div.awesomplete").show();
   this._searchJusos(e.target.value);
  };
  this._searchJusos = async (value) => {
   if (value.length > 0) {
    this.setState({ loading: true });
    const res = await axios(
     "http://www.juso.go.kr/addrlink/addrLinkApi.do?currentPage=1&countPerPage=10&keyword=" +
      value +
      "&confmKey=devU01TX0FVVEgyMDIwMDYxNjA3NTczNzEwOTg2ODc=&resultType=json"
    );

    const geoResult =
     (await res.data.results) &&
     res.data.results.juso &&
     res.data.results.juso.map((a) => {
      return {
       label: a.siNm + " " + a.sggNm + " " + a.emdNm,
       value: a.siNm + " " + a.sggNm + " " + a.emdNm,
       siNm: a.siNm,
       sggNm: a.sggNm,
       emdNm: a.emdNm,
      };
     });
    if (!geoResult || geoResult.length == 0) {
     return;
    }
    //var distinctGeoResult = [...new Set(geoResult)];
    geoResult = this._removeDuplicate(geoResult, "label");
    console.log(geoResult);
    geoResult = geoResult.filter((e) => {
     return e.label.indexOf($("#registOrderWorkingArea").val()) > -1;
    });
    //시를 검색하고 싶을 때
    const siNmList = geoResult.filter((e) => {
     if (e.siNm.indexOf($("#registOrderWorkingArea").val()) > -1) {
      e.label = e.siNm;
      e.sggNm = "";
      e.emdNm = "";
      return true;
     }
    });
    //시군구를 검색하고 싶을 때
    const sggNmList = geoResult.filter((e) => {
     if (e.sggNm.indexOf($("#registOrderWorkingArea").val()) > -1) {
      e.label = e.siNm + " " + e.sggNm;
      e.sggNm = "";
      return true;
     }
    });
    //동을 검색하고 싶을 때
    const emdNmList = geoResult.filter((e) => {
     return e.emdNm.indexOf($("#registOrderWorkingArea").val()) > -1;
    });
    //시 따로 시군구 따로 동 따로 중복 없앤 후 다 보여주자
    siNmList = this._removeDuplicate(siNmList, "siNm");
    sggNmList = this._removeDuplicate(sggNmList, "sggNm");
    emdNmList = this._removeDuplicate(emdNmList, "emdNm");
    console.log(siNmList);
    console.log(sggNmList);
    console.log(emdNmList);
    const jusoList = siNmList.concat(sggNmList, emdNmList);
    console.log(jusoList);
    this.setState({ jusos: jusoList, loading: false });
   }
  };
  this._removeDuplicate = (array, fieldNm) => {
   var obj = {};

   for (var i = 0, len = array.length; i < len; i++) {
    obj[array[i][fieldNm]] = array[i];
   }
   array = new Array();
   for (var key in obj) {
    array.push(obj[key]);
   }
   return array;
  };
  this._onclickJuso = (e) => {
   console.log(e.target.dataset);
   $("#registOrderWorkingArea").val(e.target.innerText);
   $("#registOrderWorkingArea").siblings("div.awesomplete").hide();
  };
  */
  this._setTimes = (hour, minute) => {
   var mil = false; // use am/pm

   var hour = document.getElementById(hour);
   var min = document.getElementById(minute);

   for (var i = 0; i < 24; i++) {
    var val = i < 10 && mil ? "0" + i : i;
    //if (!mil && val > 12) val -= 12;
    hour.options[i] = new Option(val, i);
   }
   for (var i = 0; i < 60; i++) {
    var val = i < 10 ? "0" + i : i;
    min.options[i] = new Option(val, i);
   }
   /*
   hour.onchange = function () {
    document.getElementById(hour).value =
     hour.options[hour.selectedIndex].text + ":" + min.value;
   };
   min.onchange = function () {
    hour.onchange();
   };
   var now = new Date();
   hour.selectedIndex = now.getHours();
   min.selectedIndex = now.getMinutes();
   hour.onchange();
   */
  };
 }
 /*
 get renderJusos() {
  let jusos = <div className="awesomplete"></div>;
  if (this.state.jusos && this.state.jusos.length > 0) {
   jusos = (
    <div className="awesomplete">
     <ul>
      {this.state.jusos.map((e, i) => {
       return (
        <li
         onClick={this._onclickJuso}
         key={i}
         data-sinm={e.siNm}
         data-sggnm={e.sggNm}
         data-emdnm={e.emdNm}
        >
         {e.label}
        </li>
       );
      })}
     </ul>
    </div>
   );
  } else {
   jusos = (
    <div className="awesomplete">
     <ul>
      <li>검색된 주소가 없습니다.</li>
     </ul>
    </div>
   );
  }

  return jusos;
 }
 */
 async componentDidMount() {
  const userInfo = JSON.parse(localStorage.getItem("userInfo"));
  //console.log(userInfo.token);
  //console.log(this.props.token);
  let { data } = await axios.get("/v1/carrier", {
   headers: {
    "Content-Type": "application/json",
    "X-AUTH-TOKEN": userInfo.token,
   },
  });
  if (data.data.workGroups) {
   this.setState({
    workGroups: data.data.workGroups,
    workGroupManagers:
     data.data.workGroups && data.data.workGroups.length > 0
      ? data.data.workGroups[0].workGroupManagers
      : [],
   });
  }
  /*
  $("#workGroupManager").val(
   $("option:selected", this.orderRegistWorkGroupRef.current).data("manager")
  );
*/
  attachJiraIssueColletor();

  // Activate Bootstrap scrollspy for the sticky nav component
  $("body").scrollspy({
   target: "#stickyNav",
   offset: 1000,
  });
  // Scrolls to an offset anchor when a sticky nav link is clicked
  $('.nav-sticky a.nav-link[href*="#"]:not([href="#"])').click(function () {
   if (
    location.pathname.replace(/^\//, "") == this.pathname.replace(/^\//, "") &&
    location.hostname == this.hostname
   ) {
    var target = $(this.hash);
    target = target.length ? target : $("[name=" + this.hash.slice(1) + "]");
    if (target.length) {
     $("html, body").animate(
      {
       scrollTop: target.offset().top - 81,
      },
      200
     );
     return false;
    }
   }
  });
  this._setTimes("workHourStart", "workMinuteStart");
  this._setTimes("workHourEnd", "workMinuteEnd");

  const order =
   this.props.location &&
   this.props.location.state &&
   this.props.location.state.order
    ? this.props.location.state.order
    : null;

  if (order) {
   $("#orderSeq").val(order.orderSeq);
   $("#carrierSeq").val(order.carrierSeq);
   $("#userSeq").val(order.userSeq);
   $("#orderRegisWorkGroup").val(order.workGroupNm);
   $("input:radio[name=rcritType]:input[value=" + order.rcritType + "]").attr(
    "checked",
    true
   );
   $("#workGroupManager").html("");
   var workGroupManagers = $("option:selected", "#orderRegisWorkGroup")
    .data("managers")
    .split(",");

   workGroupManagers.map((e1, i) => {
    $("#workGroupManager").append(new Option(`${e1}`, `${e1}`));
   });
   $("#workGroupManager").val(order.workGroupManager);
   $("#rcritMans").val(order.rcritMans);
   order.carTypes.map((e) => {
    $("input:checkbox[name=carTypes]:input[value=" + e + "]").attr(
     "checked",
     true
    );
   });
   $("input:radio[name=tonType]:input[value=" + order.tonType + "]").attr(
    "checked",
    true
   );
   $("#dlvyPrdlst").val(order.dlvyPrdlst);
   $("#payAmt").val(order.payAmt);
   $(
    "input:radio[name=payFullType]:input[value=" + order.payFullType + "]"
   ).attr("checked", true);
   $("#workingArea").val(order.workingArea);
   $("#opratSctn").val(order.opratSctn);
   $("#workingDaysType").val(order.workingDaysType);
   this._setorderRegistWorkType({
    target: { value: document.getElementById("workingDaysType").value },
   });
   order.workDays.map((e) => {
    $("input:checkbox[name=workDays]:input[value=" + e + "]").attr(
     "checked",
     true
    );
   });
   $("#workHourStart").val(order.workHourStart);
   $("#workMinuteStart").val(order.workMinuteStart);
   $("#workHourEnd").val(order.workHourEnd);
   $("#workMinuteEnd").val(order.workMinuteEnd);
   $("#detailMatter").val(order.detailMatter);
   if (order.workingAreaEtc) {
    this.setState({
     registOrderWorkingAreaEtcMatterToggle: true,
    });
    $("#workingAreaEtc").val(order.workingAreaEtc);
   }
  }
 }

 render() {
  const { workGroups, workGroupManagers } = this.state;
  const order =
   this.props.location &&
   this.props.location.state &&
   this.props.location.state.order
    ? this.props.location.state.order
    : null;
  return (
   <MainStructure>
    <main>
     <div className="page-header pb-10 page-header-dark bg-gradient-primary-to-secondary">
      <div className="container-fluid">
       <div className="page-header-content">
        <h1 className="page-header-title">
         <div className="page-header-icon">
          <svg
           xmlns="http://www.w3.org/2000/svg"
           width="24"
           height="24"
           viewBox="0 0 24 24"
           fill="none"
           stroke="currentColor"
           strokeWidth="2"
           strokeLinecap="round"
           strokeLinejoin="round"
           className="feather feather-edit-3"
          >
           <path d="M12 20h9"></path>
           <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"></path>
          </svg>
         </div>
         <span>{order ? "오더 수정" : "오더 등록"}</span>
        </h1>
        <div className="page-header-subtitle">
         오더를 {order ? "수정" : "등록"}합니다.
        </div>
       </div>
      </div>
     </div>
     <div className="container-fluid mt-n10">
      <div className="row">
       <div className="col-lg-12">
        <div id="default">
         <div className="card mb-4">
          <div className="card-header">{order ? "오더 수정" : "오더 등록"}</div>
          <div className="card-body">
           <div className="sbp-preview">
            <div className="sbp-preview-content">
             <form role="form" id="orderRegistForm">
              {order ? (
               <div className="form-group row">
                <label
                 htmlFor="orderSeqDp"
                 className="col-12 col-sm-2 col-form-label"
                >
                 오더번호
                </label>
                <input type="hidden" id="orderSeq" name="orderSeq" />
                <input
                 value={`W${order.workGroupNm}C${order.carrierSeq}U${order.userSeq}O${order.orderSeq}`}
                 readOnly
                 className="form-control col-12 col-sm-10"
                 id="orderSeqDp"
                 name="orderSeqDp"
                 type="text"
                 key="orderSeqDp"
                 autoComplete="off"
                 required
                />
               </div>
              ) : null}
              <div className="form-group row">
               <label
                htmlFor="orderRegisWorkGroup"
                className="col-12 col-sm-2 col-form-label"
               >
                운송그룹
               </label>
               <input
                type="hidden"
                value={this.props.carrierSeq}
                name="carrierSeq"
               />
               <input type="hidden" value={this.props.userSeq} name="userSeq" />
               <select
                className="form-control col-12 col-sm-4"
                id="orderRegisWorkGroup"
                name="workGroupNm"
                onChange={this._changeWorkGroup}
                ref={this.orderRegistWorkGroupRef}
               >
                {workGroups.length > 0
                 ? workGroups.map((obj, index) => {
                    return (
                     <option
                      key={obj.workGroupNm}
                      value={obj.workGroupNm}
                      data-managers={obj.workGroupManagers.map((e) => {
                       return e.workGroupManagerNm;
                      })}
                     >
                      {obj.workGroupNm}
                     </option>
                    );
                   })
                 : null}
               </select>
               <label
                htmlFor="workGroupManager"
                className="col-12 col-sm-2 text-sm-right pr-5 col-form-label"
               >
                담당자
               </label>
               <select
                className="form-control col-12 col-sm-4"
                id="workGroupManager"
                name="workGroupManager"
                key="workGroupManager"
               >
                {workGroupManagers.length > 0
                 ? workGroupManagers.map((obj, index) => {
                    console.log(obj);
                    return (
                     <option
                      key={obj.workGroupManagerNm}
                      value={obj.workGroupManagerNm}
                     >
                      {obj.workGroupManagerNm}
                     </option>
                    );
                   })
                 : null}
               </select>
              </div>
              <div className="form-group row">
               <label
                htmlFor="rcritType"
                className="col-12 col-sm-2 col-form-label"
               >
                모집 유형
               </label>
               <div className="col-12 col-sm-4 pl-sm-0" id="rcritType">
                {this.props.rcritTypeCodes.map((obj, index) => {
                 return (
                  <label
                   className="col-form-label pr-3 radio-inline"
                   htmlFor={obj.code}
                   key={obj.code}
                  >
                   <input
                    className="radio mr-1"
                    type="radio"
                    name="rcritType"
                    key={obj.code}
                    id={obj.code}
                    value={obj.code}
                    required
                   />
                   {obj.codeDc}
                  </label>
                 );
                })}
               </div>
               <label
                htmlFor="rcritMans"
                className="col-12 col-sm-2 text-sm-right pr-5 col-form-label"
               >
                모집인원
               </label>
               <input
                className="form-control col-12 col-sm-4"
                id="rcritMans"
                type="number"
                placeholder="5"
                key="rcritMans"
                name="rcritMans"
                required
               />
              </div>

              <div className="form-group row">
               <label className="col-12 col-sm-2 col-form-label">
                운행차량
               </label>
               <div className="col-12 col-sm-10 row mx-0 px-0 d-flex justify-content-start">
                <div className="col-12 col-sm-6 ml-sm-0 pl-sm-0">
                 <div className="card">
                  <div className="card-header">
                   <label htmlFor="carTypes" className="mb-0 col-12">
                    차종(중복 선택 가능)
                   </label>
                  </div>
                  <div className="card-body py-0">
                   <div className="col-12 row">
                    <div className="custom-control custom-radio" id="carTypes">
                     {this.props.carTypeCodes.map((obj, index) => {
                      return (
                       <label
                        className="col-form-label pr-3 radio-inline"
                        htmlFor={obj.code}
                        key={obj.code}
                       >
                        <input
                         className="checkbox mr-1"
                         type="checkbox"
                         name="carTypes"
                         key={obj.code}
                         id={obj.code}
                         value={obj.code}
                         required
                        />
                        {obj.codeDc}
                       </label>
                      );
                     })}
                    </div>
                   </div>
                  </div>
                 </div>
                </div>
                <div className="col-12 col-sm-6 mr-sm-0 pr-sm-0">
                 <div className="card">
                  <div className="card-header">
                   <label className="mb-0 col-12" htmlFor="tonType">
                    톤수
                   </label>
                  </div>
                  <div className="card-body py-0">
                   <div className="col-12 row">
                    <div className="custom-control custom-radio">
                     {this.props.tonTypeCodes.map((obj, index) => {
                      return (
                       <label
                        className="col-form-label pr-3 radio-inline"
                        htmlFor={obj.code}
                        key={obj.code}
                       >
                        <input
                         className="radio mr-1"
                         type="radio"
                         name="tonType"
                         key={obj.code}
                         id={obj.code}
                         value={obj.code}
                         required
                        />
                        {obj.codeDc}
                       </label>
                      );
                     })}
                    </div>
                   </div>
                  </div>
                 </div>
                </div>
               </div>
              </div>
              <div className="form-group row">
               <label
                htmlFor="dlvyPrdlst"
                className="col-12 col-sm-2 col-form-label"
               >
                배송 품목
               </label>
               <input
                className="form-control col-12 col-sm-10"
                id="dlvyPrdlst"
                type="text"
                placeholder="ex) 박스 일 40건"
                key="dlvyPrdlst"
                name="dlvyPrdlst"
                required
               />
              </div>
              <div className="form-group row">
               <label
                htmlFor="payAmt"
                className="col-12 col-sm-2 col-form-label"
               >
                급여
               </label>
               <input
                className="form-control col-12 col-sm-4"
                id="payAmt"
                type="text"
                placeholder="ex) 500만원"
                key="payAmt"
                name="payAmt"
                required
               />
               <label
                htmlFor="payFullType"
                className="col-12 col-sm-2 text-sm-right pr-5 col-form-label"
               >
                완제/무제
               </label>
               <div className="col-12 col-sm-4 row">
                <div className="custom-control custom-radio" id="payFullType">
                 {this.props.payFullTypeCodes.map((obj, index) => {
                  return (
                   <label
                    className="col-form-label pr-3 radio-inline"
                    htmlFor={obj.code}
                    key={obj.code}
                   >
                    <input
                     className="radio mr-1"
                     type="radio"
                     name="payFullType"
                     key={obj.code}
                     id={obj.code}
                     value={obj.code}
                     required
                    />
                    {obj.codeDc}
                   </label>
                  );
                 })}
                </div>
               </div>
              </div>
              <div className="form-group row">
               <label
                htmlFor="workingArea"
                className="col-12 col-sm-2 col-form-label"
               >
                지역
               </label>
               <div className="col-12 col-sm-10 row pr-sm-0">
                <div className="col-12 row">
                 {/*
                 <input
                  value={this.state.value}
                  onChange={this._onChangeHandler}
                  className="form-control col-8"
                  id="registOrderWorkingArea"
                  type="text"
                  placeholder="서이천물류센터"
                  key="registOrderWorkingArea"
                  autoComplete="off"
                 />
 */}
                 <input
                  value={this.state.value}
                  onClick={this._getDaumAddressFinder}
                  readOnly
                  className="form-control col-sm-8 col-12"
                  id="workingArea"
                  name="workingArea"
                  type="text"
                  placeholder="서이천물류센터"
                  key="workingArea"
                  autoComplete="off"
                  required
                 />
                 {/*this.renderJusos*/}

                 <div className="col-sm-3 col-12">
                  <label
                   className="col-form-label pr-3 radio-inline"
                   htmlFor="registOrderWorkingAreaEtcMatterToggle"
                  >
                   <input
                    className="checkbox mr-1"
                    id="registOrderWorkingAreaEtcMatterToggle"
                    name="registOrderWorkingAreaEtcMatterToggle"
                    type="checkbox"
                    onChange={this._setRegistOrderWorkingAreaEtcMatterToggle}
                    checked={this.state.registOrderWorkingAreaEtcMatterToggle}
                   />
                   기타입력사항
                  </label>
                 </div>
                </div>
               </div>
              </div>
              {this.state.registOrderWorkingAreaEtcMatterToggle ? (
               <div className="form-group row">
                <label
                 htmlFor="workingAreaEtc"
                 className="col-12 col-sm-2 col-form-label"
                ></label>

                <label
                 htmlFor="workingAreaEtc"
                 className="col-12 col-sm-2 col-form-label text-sm-right"
                >
                 기타입력사항:
                </label>
                <input
                 className="form-control col-12 col-sm-8"
                 id="workingAreaEtc"
                 type="text"
                 placeholder="ex) 기타입력사항"
                 key="workingAreaEtc"
                 name="workingAreaEtc"
                />
               </div>
              ) : null}
              <div className="form-group row">
               <label
                htmlFor="opratSctn"
                className="col-12 col-sm-2 col-form-label"
               >
                운행구간
               </label>
               <input
                className="form-control col-12 col-sm-10"
                id="opratSctn"
                name="opratSctn"
                type="text"
                placeholder="ex) 서울시"
                key="opratSctn"
                required
               />
              </div>
              <div className="form-group row">
               <label
                htmlFor="workingDaysType"
                className="col-12 col-sm-2 col-form-label"
               >
                근무요일
               </label>
               <select
                className="form-control col-12 col-sm-10"
                id="workingDaysType"
                onChange={this._setorderRegistWorkType}
                name="workingDaysType"
                required
               >
                <option value="">선택</option>
                <option value="fiveDay">주5일</option>
                <option value="sixDay">주6일</option>
                <option value="directCheck">직접입력</option>
               </select>
              </div>
              <div className="form-group row">
               <label
                htmlFor="workDays"
                className="col-12 col-sm-2 col-form-label"
               >
                상세요일 선택
               </label>
               <div className="col-12 col-sm-10 row" id="workDays">
                {this.props.workDayCodes.map((obj, index) => {
                 return (
                  <label
                   className="col-form-label pr-3 radio-inline"
                   htmlFor={obj.code}
                   key={obj.code}
                  >
                   <input
                    className="checkbox mr-1"
                    type="checkbox"
                    name="workDays"
                    key={obj.code}
                    id={obj.code}
                    value={obj.code}
                    disabled
                    required
                   />
                   {obj.codeDc}
                  </label>
                 );
                })}
               </div>
              </div>
              <div className="form-group row">
               <label
                htmlFor="workHourStart"
                className="col-12 col-sm-2 col-form-label"
               >
                근무시간
               </label>
               <div className="col-12 col-sm-10 row">
                <select
                 className="form-control col-sm-1 col-4"
                 id="workHourStart"
                 name="workHourStart"
                ></select>
                <label className="col-form-label ml-3 mr-3">:</label>
                <select
                 className="form-control col-sm-1 col-4"
                 id="workMinuteStart"
                 name="workMinuteStart"
                ></select>
                <label className="col-form-label ml-3 mr-3">~</label>
                <select
                 className="form-control col-sm-1 col-4"
                 id="workHourEnd"
                 name="workHourEnd"
                ></select>
                <label className="col-form-label ml-3 mr-3">:</label>
                <select
                 className="form-control col-sm-1 col-4"
                 id="workMinuteEnd"
                 name="workMinuteEnd"
                ></select>
               </div>
              </div>
              <div className="form-group row">
               <label
                htmlFor="detailMatter"
                className="col-12 col-sm-2 col-form-label"
               >
                상세 사항
               </label>
               <textarea
                className="form-control col-12 col-sm-10"
                id="detailMatter"
                name="detailMatter"
                rows="3"
                placeholder="ex) 연락주세요. 02-xxx-xxxx "
                required
               ></textarea>
              </div>
              <div className="d-flex flex-row-reverse">
               <button
                className="btn btn-primary"
                type="button"
                onClick={(e) => this._validateOrderRegistForm(e)}
               >
                등록 완료
               </button>
               {!order || (order && order.status == "0703") ? (
                <button
                 className="btn btn-secondary mr-3"
                 type="button"
                 onClick={(e) => this._noValidateOrderRegistForm(e)}
                >
                 임시저장
                </button>
               ) : null}
              </div>
             </form>
            </div>
            <div className="sbp-preview-text">
             저장한 오더는 오더관리 메뉴에서 확인할 수 있습니다. 진행중 상태와
             임시저장 상태로 구분됩니다.
            </div>
           </div>
          </div>
         </div>
        </div>
       </div>
      </div>
     </div>
     <div
      className="modal fade"
      id="saveModalPopup"
      tabIndex="-1"
      role="dialog"
      aria-labelledby="saveModalPopup"
      aria-hidden="true"
     >
      <div className="modal-dialog modal-dialog-centered" role="document">
       <div className="modal-content">
        <div className="modal-header">
         <h5 className="modal-title">
          저장{order ? "(수정)" : null} 하시겠습니까?
         </h5>
         <button
          className="close"
          type="button"
          data-dismiss="modal"
          aria-label="Close"
         >
          <span aria-hidden="true">×</span>
         </button>
        </div>
        <div className="modal-body">
         {order
          ? "진행 중인 오더 공고 내용을 변경합니다."
          : "저장하면 오더 공고가 시작됩니다."}
        </div>
        <div className="modal-footer">
         <button
          className="btn btn-secondary"
          type="button"
          data-dismiss="modal"
         >
          아니오
         </button>
         <button
          className="btn btn-primary"
          type="button"
          onClick={(e) => this._saveCompleteOrderRegist(e)}
         >
          네
         </button>
        </div>
       </div>
      </div>
     </div>
     <div
      className="modal fade"
      id="saveTempModalPopup"
      tabIndex="-1"
      role="dialog"
      aria-labelledby="saveTempModalPopup"
      aria-hidden="true"
     >
      <div className="modal-dialog modal-dialog-centered" role="document">
       <div className="modal-content">
        <div className="modal-header">
         <h5 className="modal-title">
          임시저장{order ? "(수정)" : null} 하시겠습니까?
         </h5>
         <button
          className="close"
          type="button"
          data-dismiss="modal"
          aria-label="Close"
         >
          <span aria-hidden="true">×</span>
         </button>
        </div>
        <div className="modal-footer">
         <button
          className="btn btn-secondary"
          type="button"
          data-dismiss="modal"
         >
          아니오
         </button>
         <button
          className="btn btn-primary"
          type="button"
          onClick={(e) => this._saveTempOrderRegist(e)}
         >
          네
         </button>
        </div>
       </div>
      </div>
     </div>
     <div
      className="modal fade"
      id="saveCompleteModalPopup"
      tabIndex="-1"
      role="dialog"
      aria-labelledby="saveCompleteModalPopup"
      aria-hidden="true"
     >
      <div className="modal-dialog modal-dialog-centered" role="document">
       <div className="modal-content">
        <div className="modal-header">
         <h5 className="modal-title">
          {order ? "수정" : "등록"}이 완료되었습니다.
         </h5>
         <button
          className="close"
          type="button"
          data-dismiss="modal"
          aria-label="Close"
         >
          <span aria-hidden="true">×</span>
         </button>
        </div>
       </div>
      </div>
     </div>
    </main>
   </MainStructure>
  );
 }
}

const mapStateToProps = (state) => ({
 codes: state.codes.codes,
 orderRegisWorkGroupCodes: state.codes.orderRegisWorkGroupCodes,
 rcritTypeCodes: state.codes.rcritTypeCodes,
 carTypeCodes: state.codes.carTypeCodes,
 tonTypeCodes: state.codes.tonTypeCodes,
 payFullTypeCodes: state.codes.payFullTypeCodes,
 workDayCodes: state.codes.workDayCodes,
 token: state.auth.userInfo.token,
 carrierSeq: state.auth.userInfo.carrierSeq,
 userSeq: state.auth.userInfo.userSeq,
});

export default connect(mapStateToProps)(OrderRegist);
