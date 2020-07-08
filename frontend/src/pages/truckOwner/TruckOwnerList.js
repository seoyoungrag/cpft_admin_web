import React from "react";
import { connect } from "react-redux";
import axios from "axios";
import MainStructure from "components/structure/MainStructure";
//import "./TruckOwnerList.css";

//import "styles/datatables.css";
import "vendor/datatables/dataTables.bootstrap4.min.css";
import $ from "jquery";
//$.DataTable = require("datatables.net");
$.DataTable = require("vendor/datatables/dataTables.bootstrap4.min.js");
import "vendor/datatables/jquery.dataTables.min.js";
import { Component } from "react";

const columns = [
 { title: "운송그룹", data: "ordersComplete", width: "15%" },
 { title: "담당자", data: "ordersComplete", width: "15%" },
 { title: "차주명", data: "userNm", width: "15%" },
 { title: "근무 현황", data: "ordersComplete", width: "20%" },
 { title: "근무일", data: "ordersComplete", width: "20%" },
 { title: "연락처", data: "phone", width: "15%" },
];

const DataTable_language = {
 decimal: ",",
 thousands: ".",
 paginate: {
  first: "",
  last: "",
  previous: "<",
  next: ">",
 },
 processing: "처리 중 입니다.",
 emptyTable: "처리할 내용이 없습니다.",
 info: "총 _PAGES_페이지/_TOTAL_개 중 (_START_ ~ _END_) ",
};
class TruckOwnerList extends Component {
 constructor(props) {
  super(props);
  this.state = {
   startRendering: "",
  };
 }
 componentDidMount() {
  attachJiraIssueColletor();
  // Activate Bootstrap scrollspy for the sticky nav component
  $("body").scrollspy({
   target: "#stickyNav",
   offset: 0,
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
  this.setState({
   startRendering: 1,
  });
 }
 componentDidUpdate(prevProps) {
  if (
   this.props.carTypeCodes &&
   this.props.tonTypeCodes &&
   !$.fn.DataTable.isDataTable(this.refs.truckOwnerListTbl)
  ) {
   const COM = this;
   const table = $(this.refs.truckOwnerListTbl).DataTable({
    createdRow: function (row, data) {},
    language: DataTable_language,
    columnDefs: [
     {
      defaultContent: "-",
      targets: "_all",
     },
     {
      targets: [0],
      createdCell: function (td, cellData, rowData, row, col) {
       var workGroup = "";
       cellData.map((obj, index) => {
        if (cellData.length > 0 && index > 0) {
         workGroup += "</br>";
        }
        workGroup += obj.order.workGroupNm;
       });
       $(td).html(workGroup);
      },
     },
     {
      targets: [1],
      createdCell: function (td, cellData, rowData, row, col) {
       var manager = "";
       cellData.map((obj, index) => {
        if (cellData.length > 0 && index > 0) {
         manager += "</br>";
        }
        manager += obj.order.workGroupManager;
       });
       $(td).html(manager);
      },
     },
     {
      targets: [3],
      createdCell: function (td, cellData, rowData, row, col) {
       var status = "";
       cellData.map((obj, index) => {
        if (cellData.length > 0 && index > 0) {
         status += "</br>";
        }
        if (obj.status == "0801") {
         status += "연락중";
        } else if (obj.status == "0802") {
         status += "근무중";
        } else {
         status += "탈락";
        }
       });
       $(td).html(status);
      },
     },
     {
      targets: [4],
      createdCell: function (td, cellData, rowData, row, col) {
       var workDays = "";
       cellData.map((obj, index) => {
        if (cellData.length > 0 && index > 0) {
         workDays += "</br>";
        }
        var workDay = obj.order.workDays
         .map((obj1, index) => {
          return COM.props.workDayCodes.filter((obj2, index) => {
           if (obj2.code == obj1) {
            return true;
           }
          })[0].codeValue;
         })
         .join();
        if (obj.order.workingDaysType == "fiveDay") {
         workDays += "주5일" + " (" + workDay + ")";
        } else if (obj.order.status == "sixDay") {
         workDays += "주6일" + " (" + workDay + ")";
        } else {
         workDays += "" + workDay;
        }
       });
       $(td).html(workDays);
      },
     },
    ],
    processing: true,
    serverSide: true,
    responsive: true,
    autoWidth: false,
    width: "100%",
    paging: true,
    ordering: false,
    select: false,
    dom:
     "<'row'<'col-sm-12'rt>>" +
     "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>>",
    data: this.state.DataTable,
    columns,
    ordering: false,
    ajax: {
     url: "/v1/truckOwner/order/status/0702/orderTruckOwner/status/0802",

     type: "GET",
     data: function (d) {
      delete d.columns;

      return d;
     },
    },
   });
  }
 }
 componentWillUnmount() {
  $(".data-table-wrapper").find("table").DataTable().destroy(true);
 }

 render() {
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
         <span>전체 차주 리스트</span>
        </h1>
        <div className="page-header-subtitle">
         전체 차주 리스트를 조회합니다.
        </div>
       </div>
      </div>
     </div>
     <div className="container-fluid mt-n10">
      <div className="card mb-4">
       <div className="card-header row">
        <div className="col-6">전체 차주 리스트</div>
        <div className="col-sm-12 col-md-6 row">
         <div className="col-12 d-flex justify-content-end">
          <button className="btn btn-info">
           <span>관리</span>
          </button>
         </div>
        </div>
       </div>
       <div className="card-body">
        <div className="datatable table-responsive">
         <table
          id="truckOwnerListTbl"
          ref="truckOwnerListTbl"
          className="table table-bordered table-hover"
          width="100%"
          cellSpacing="0"
          role="grid"
          aria-describedby="dataTable_info"
         />
        </div>
       </div>
      </div>
      <div
       className="modal fade"
       id="exampleModalCenter1"
       tabIndex="-1"
       role="dialog"
       aria-labelledby="exampleModalCenterTitle"
       aria-hidden="true"
      >
       <div className="modal-dialog modal-dialog-centered" role="document">
        <div className="modal-content">
         <div className="modal-header">
          <h5 className="modal-title" id="exampleModalCenterTitle">
           오더번호 001
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
          <div className="card mb-4">
           <div className="card-body">
            <h5 className="card-title text-primary">김차일 010-1111-1411</h5>
            <div className="card-text row">
             <div className="col-6">
              <div>차량: 1t 냉장</div>
              <div>경력: 1년 미만</div>
             </div>
             <div className="col-6">
              <div>
               <h4>면허 및 자격</h4>
              </div>
              <div>1종 대형</div>
              <div>화물운송자격증 보유</div>
              <div>개인사업자</div>
             </div>
            </div>
           </div>
           <a
            className="card-footer d-flex align-items-center justify-content-between"
            href="#"
           >
            차주 상세 보기
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
             className="feather feather-arrow-right"
            >
             <line x1="5" y1="12" x2="19" y2="12"></line>
             <polyline points="12 5 19 12 12 19"></polyline>
            </svg>
           </a>
          </div>
         </div>
         <div className="modal-footer">
          <button
           className="btn btn-secondary"
           type="button"
           data-dismiss="modal"
          >
           닫기
          </button>
          <button className="btn btn-primary" type="button">
           채용확정
          </button>
          <button className="btn btn-danger" type="button">
           채용거절
          </button>
         </div>
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
export default connect(mapStateToProps)(TruckOwnerList);
