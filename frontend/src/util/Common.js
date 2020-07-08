import $ from "jquery";
import axios from "axios";
import "styles/datatables.css";
$.validate = require("jquery-validation");
$.DataTable = require("datatables.net");
$.moment = require("moment");
$.datepicker = require("bootstrap-datepicker");
import "styles/bootstrap-datepicker3.standalone.css";
import "datatables.net-dt";
$.validator.setDefaults({
    debug: true,
    success : "valid"
    
  });
  $.extend( $.validator.messages, { required: "필수 항목입니다.", remote: "항목을 수정하세요.", email: "유효하지 않은 E-Mail주소입니다.", url: "유효하지 않은 URL입니다.", date: "올바른 날짜를 입력하세요.", dateISO: "올바른 날짜(ISO)를 입력하세요.", number: "유효한 숫자가 아닙니다.", digits: "숫자만 입력 가능합니다.", creditcard: "신용카드 번호가 바르지 않습니다.", equalTo: "같은 값을 다시 입력하세요.", extension: "올바른 확장자가 아닙니다.", maxlength: $.validator.format( "{0}자를 넘을 수 없습니다. " ), minlength: $.validator.format( "{0}자 이상 입력하세요." ), rangelength: $.validator.format( "문자 길이가 {0} 에서 {1} 사이의 값을 입력하세요." ), range: $.validator.format( "{0} 에서 {1} 사이의 값을 입력하세요." ), max: $.validator.format( "{0} 이하의 값을 입력하세요." ), min: $.validator.format( "{0} 이상의 값을 입력하세요." ) } );
$.fn.datepicker.dates["ko"] = {
 days: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"],
 daysShort: ["일", "월", "화", "수", "목", "금", "토"],
 daysMin: ["일", "월", "화", "수", "목", "금", "토"],
 months: [
  "1월",
  "2월",
  "3월",
  "4월",
  "5월",
  "6월",
  "7월",
  "8월",
  "9월",
  "10월",
  "11월",
  "12월",
 ],
 monthsShort: [
  "1월",
  "2월",
  "3월",
  "4월",
  "5월",
  "6월",
  "7월",
  "8월",
  "9월",
  "10월",
  "11월",
  "12월",
 ],
 today: "오늘",
 clear: "삭제",
 format: "yyyy-mm-dd",
 titleFormat: "yyyy년mm월",
 weekStart: 0,
};

Object.assign($.fn.datepicker.defaults, {
 format: "yyyy-mm-dd", //데이터 포맷 형식(yyyy : 년 mm : 월 dd : 일 )
 startDate: "-10d", //달력에서 선택 할 수 있는 가장 빠른 날짜. 이전으로는 선택 불가능 ( d : 일 m : 달 y : 년 w : 주)
 endDate: "+10d", //달력에서 선택 할 수 있는 가장 느린 날짜. 이후로 선택 불가 ( d : 일 m : 달 y : 년 w : 주)
 autoclose: true, //사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
 calendarWeeks: false, //캘린더 옆에 몇 주차인지 보여주는 옵션 기본값 false 보여주려면 true
 clearBtn: false, //날짜 선택한 값 초기화 해주는 버튼 보여주는 옵션 기본값 false 보여주려면 true
 datesDisabled: ["2020-06-24", "2019-06-26"], //선택 불가능한 일 설정 하는 배열 위에 있는 format 과 형식이 같아야함.
 daysOfWeekDisabled: [0, 6], //선택 불가능한 요일 설정 0 : 일요일 ~ 6 : 토요일
 daysOfWeekHighlighted: [3], //강조 되어야 하는 요일 설정
 disableTouchKeyboard: false, //모바일에서 플러그인 작동 여부 기본값 false 가 작동 true가 작동 안함.
 immediateUpdates: false, //사용자가 보는 화면으로 바로바로 날짜를 변경할지 여부 기본값 :false
 multidate: false, //여러 날짜 선택할 수 있게 하는 옵션 기본값 :false
 multidateSeparator: ",", //여러 날짜를 선택했을 때 사이에 나타나는 글짜 2019-05-01,2019-06-01
 templates: {
  leftArrow: "&laquo;",
  rightArrow: "&raquo;",
 }, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징
 showWeekDays: true, // 위에 요일 보여주는 옵션 기본값 : true
 title: "날짜를 선택하세요.", //캘린더 상단에 보여주는 타이틀
 todayHighlight: true, //오늘 날짜에 하이라이팅 기능 기본값 :false
 toggleActive: true, //이미 선택된 날짜 선택하면 기본값 : false인경우 그대로 유지 true인 경우 날짜 삭제
 weekStart: 0, //달력 시작 요일 선택하는 것 기본값은 0인 일요일
 language: "ko", //달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
});
$.fn.serializeObject = function () {
 "use strict";
 var result = {};
 var extend = function (i, element) {
  var node = result[element.name];
  if ("undefined" !== typeof node && node !== null) {
   if ($.isArray(node)) {
    node.push(element.value);
   } else {
    result[element.name] = [node, element.value];
   }
  } else {
   result[element.name] = element.value;
  }
 };

 $.each(this.serializeArray(), extend);
 return result;
};
