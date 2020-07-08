import React from "react";
import { connect } from "react-redux";
import axios from "axios";
import MainStructure from "components/structure/MainStructure";
import "./ApplicantManage.css";

//import "styles/datatables.css";
import "vendor/datatables/dataTables.bootstrap4.min.css";
import $ from "jquery";
//$.DataTable = require("datatables.net");
$.DataTable = require("vendor/datatables/dataTables.bootstrap4.min.js");
import "vendor/datatables/jquery.dataTables.min.js";
import { Component } from "react";

const columns = [
 { title: "오더 번호", data: "order.orderSeq", width: "7%" },
 { title: "이름/나이", data: "truckOwner", width: "9%" },
 { title: "차량/톤수", data: "truckOwner.trucks", width: "9%" },
 { title: "경력", data: "truckOwner.carrerCn", width: "6%" },
 { title: "메세지", data: "message", width: "44%" },
 { title: "지원일", data: "createdAt", width: "7%" },
 { title: "열람", data: "isRead", width: "7%" },
 { title: "상태", data: "status", width: "7%" },
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
class ApplicantManage extends Component {
 constructor(props) {
  super(props);
  this.state = {
   orderList: [],
   orderSelecteButtonValue: "",
  };
  this._getCarrierOrders = async () => {
   const userInfo = JSON.parse(localStorage.getItem("userInfo"));
   let { data } = await axios.get("/v1/order/carrier/" + userInfo.carrierSeq+"/order/status/0701", {
    headers: {
     "Content-Type": "application/json",
     "X-AUTH-TOKEN": userInfo.token,
    },
   });
   if (data.list.length > 0) {
    let orderList = data.list.filter((n) => {
     return n.status != "0704";
    });
    this.setState({ orderList });
   }
  };
  this._changeOrderBySelect = async (e) => {
   const table = $(this.refs.applicantManageTbl).DataTable();

   table.ajax.url("/v1/order/" + $(e.target).val() + "/status/0701/truckOwner").load();
   this.reloadTableData();
   this.setState({
    orderSelecteButtonValue: $(e.target).val(),
   });
  };
 }

 componentDidMount() {
  this._getCarrierOrders();
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
  });}
 componentDidUpdate(prevProps) {
  if (this.props.carTypeCodes  && this.props.tonTypeCodes && !$.fn.DataTable.isDataTable(this.refs.applicantManageTbl)) {
    const COM = this;
    const table = $(this.refs.applicantManageTbl).DataTable({
     createdRow: function (row, data) {
      var obj = data;
      var orderNm =
       "W" +
       obj.order.workGroupNm +
       "C" +
       obj.order.carrierSeq +
       "U" +
       obj.order.userSeq +
       "O" +
       obj.order.orderSeq;
  
      var carAndTon = "";
      data.truckOwner.trucks.map((obj, index) => {
       if (index > 0) {
        carAndTon += "</br>";
       }
       var car = obj.carType;
       
       car = COM.props.carTypeCodes.filter((obj, index) => {
        if (obj.code == car) {
         return true;
        }
       })[0].codeValue;
       var ton = obj.tonType;
       ton = COM.props.tonTypeCodes.filter((obj, index) => {
        if (obj.code == ton) {
         return true;
        }
       })[0].codeValue;
       carAndTon += car + " " + ton;
      });
  
      var certs = "";
      data.truckOwner.crqfcs.map((obj, index) => {
       if (index > 0) {
        certs += "</br>";
       }
       certs += obj;
      });
      $(row)
       .unbind()
       .on("click", function () {
        if (data.isRead == "N") {
         axios
          .put(
           `/v1/order/${data.order.orderSeq}/truckOwner/${data.truckOwner.userSeq}/isRead`,
           {
            isRead: "Y",
           }
          )
          .then(function (response) {
           $(COM.refs.applicantManageTbl).DataTable().ajax.reload(null, false);
          });
        }
        $("#applicantModalTitle").text(orderNm);
        $("#applicantModalUserNm").text(data.truckOwner.userNm);
        $("#applicantModalUserPhone").text(data.truckOwner.phone);
        $("#applicantModalCarAndTon").html(carAndTon);
        $("#applicantModalCarrer").text(data.carrerCn);
        $("#applicantModalCert").html(certs);
        $("#applicantModalMsg").text(data.message);
        $("#applicantModalStatusIngBtn").data("orderSeq", data.order.orderSeq);
        $("#applicantModalStatusIngBtn").data("userSeq", data.truckOwner.userSeq);
        $("#applicantModalStatusIngBtn")
         .unbind()
         .on("click", function () {
          axios
           .put(
            `/v1/order/${data.order.orderSeq}/truckOwner/${data.truckOwner.userSeq}/status`,
            {
             status: "0801",
            }
           )
           .then(function (response) {
            $(COM.refs.applicantManageTbl).DataTable().ajax.reload(null, false);
            $("#applicantModal").modal("hide");
           });
         });
  
        $("#applicantModalStatusCompleteBtn").data(
         "orderSeq",
         data.order.orderSeq
        );
        $("#applicantModalStatusCompleteBtn").data(
         "userSeq",
         data.truckOwner.userSeq
        );
  
        $("#applicantModalStatusCompleteBtn")
         .unbind()
         .on("click", function () {
          axios
           .put(
            `/v1/order/${data.order.orderSeq}/truckOwner/${data.truckOwner.userSeq}/status`,
            {
             status: "0802",
            }
           )
           .then(function (response) {
            $(COM.refs.applicantManageTbl).DataTable().ajax.reload(null, false);
            $("#applicantModal").modal("hide");
           });
         });
  
        $("#applicantModalStatusRejectBtn").data("orderSeq", data.order.orderSeq);
        $("#applicantModalStatusRejectBtn").data(
         "userSeq",
         data.truckOwner.userSeq
        );
        $("#applicantModalStatusRejectBtn")
         .unbind()
         .on("click", function () {
          axios
           .put(
            `/v1/order/${data.order.orderSeq}/truckOwner/${data.truckOwner.userSeq}/status`,
            {
             status: "0803",
            }
           )
           .then(function (response) {
            $(COM.refs.applicantManageTbl).DataTable().ajax.reload(null, false);
            $("#applicantModal").modal("hide");
           });
         });
  
        if (data.status == "0801") {
         $("#applicantModalStatusIngBtn").hide();
         $("#applicantModalStatusCompleteBtn").show();
         $("#applicantModalStatusRejectBtn").show();
        } else if (data.status == "0802") {
         $("#applicantModalStatusIngBtn").show();
         $("#applicantModalStatusCompleteBtn").hide();
         $("#applicantModalStatusRejectBtn").show();
        } else {
         $("#applicantModalStatusIngBtn").show();
         $("#applicantModalStatusCompleteBtn").show();
         $("#applicantModalStatusRejectBtn").hide();
        }
        $("#applicantModal").modal();
       });
     },
     language: DataTable_language,
     columnDefs: [
      {
       defaultContent: "-",
       targets: "_all",
      },
      {
       targets: [0],
       createdCell: function (td, cellData, rowData, row, col) {
        var obj = rowData;
        var content =
         "W" +
         obj.order.workGroupNm +
         "C" +
         obj.order.carrierSeq +
         "U" +
         obj.order.userSeq +
         "O" +
         obj.order.orderSeq;
        $(td).text(content);
       },
      },
      {
       targets: [1],
       createdCell: function (td, cellData, rowData, row, col) {
        $(td).text(cellData.userNm + "/" + cellData.age);
       },
      },
      {
       targets: [2],
       createdCell: function (td, cellData, rowData, row, col) {
        var content = "";
        
        cellData.sort(function (a, b) {
         // 오름차순
         return a.carType < b.carType ? -1 : a.carType > b.carType ? 1 : 0;
         // 광희, 명수, 재석, 형돈
        });
        cellData.map((obj, index) => {
         if (index > 0) {
          content += "</p>";
         }
         var car = obj.carType;
         car = COM.props.carTypeCodes.filter((obj, index) => {
          if (obj.code == car) {
           return true;
          }
         })[0].codeValue;
         var ton = obj.tonType;
         ton = COM.props.tonTypeCodes.filter((obj, index) => {
          if (obj.code == ton) {
           return true;
          }
         })[0].codeValue;
         content += car + " " + ton;
        });
        $(td).html(content);
       },
      },
      {
       targets: [6],
       createdCell: function (td, cellData, rowData, row, col) {
        if (cellData == "Y") {
         $(td).html('<div class="badge badge-primary badge-pill">열람</div>');
        } else {
         $(td).html('<div class="badge badge-secondary badge-pill">미열람</div>');
        }
       },
      },
      {
       targets: [7],
       createdCell: function (td, cellData, rowData, row, col) {
        if (cellData == "0801") {
         $(td).html('<div class="badge badge-warning badge-pill">연락중</div>');
        } else if (cellData == "0802") {
         $(td).html('<div class="badge badge-success badge-pill">채용확정</div>');
        } else {
         $(td).html('<div class="badge badge-danger badge-pill">채용거절</div>');
        }
        //$(td).text(cellData.userNm + "/" + cellData.age);
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
      url: "/v1/order/" + $("#orderSelecteButton").val() + "/status/0701/truckOwner",
  
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
 reloadTableData = () => {
  const table = $(".data-table-wrapper").find("table").DataTable();
  table.clear();
  table.draw();
  //table.reload();
 };


 shouldComponentUpdate(nextProps, nextState) {
  if (nextState.orderSelecteButtonValue != this.state.orderSelecteButtonValue) {
   this.reloadTableData();
  }

  return true;
 }

 render() {
  const { orderList, orderSelecteButtonValue } = this.state;
  const { carTypeCodes, tonTypeCodes } = this.props;
  return (
   <MainStructure>
    {carTypeCodes && tonTypeCodes ? (
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
          <span>지원자 관리</span>
         </h1>
         <div className="page-header-subtitle">지원자를 관리합니다.</div>
        </div>
       </div>
      </div>
      <div className="container-fluid mt-n10">
       <div className="card mb-4">
        <div className="card-header">지원자 관리</div>
        <div className="card-body">
         <div className="form-row my-2 mb-3">
          <select
           className="form-control col-12"
           id="orderSelecteButton"
           onChange={this._changeOrderBySelect}
          >
           <option value="all">전체보기</option>
           {orderList.length > 0
            ? orderList.map((obj, index) => {
               var content =
                "W" +
                obj.workGroupNm +
                "C" +
                obj.carrierSeq +
                "U" +
                obj.userSeq +
                "O" +
                obj.orderSeq;
               content += this.props.rcritTypeCodes
                .filter((e) => {
                 return e.code == obj.rcritType;
                })
                .map((r) => {
                 return r.codeValue;
                });
               content += " " + obj.carrierNm;
               content +=
                " " +
                this.props.tonTypeCodes
                 .filter((e) => {
                  return e.code == obj.tonType;
                 })
                 .map((r) => {
                  return r.codeValue;
                 });
               content +=
                " " +
                this.props.carTypeCodes
                 .filter((e) => {
                  return e.code == obj.carType;
                 })
                 .map((r) => {
                  return r.codeValue;
                 });
               content +=
                " " +
                (obj.workingDaysType == "fiveDay"
                 ? "주5일"
                 : obj.workingDaysType == "sixDay"
                 ? "주6일"
                 : null);
               content += " " + obj.payAmt + " " + obj.detailMatter;
               return (
                <option key={obj.orderSeq} value={obj.orderSeq}>
                 {content}
                </option>
               );
              })
            : null}
          </select>
         </div>
         <div className="datatable table-responsive">
          <table
           id="applicantManageTbl"
           ref="applicantManageTbl"
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
        id="applicantModal"
        tabIndex="-1"
        role="dialog"
        aria-labelledby="applicantModalTitle"
        aria-hidden="true"
       >
        <div className="modal-dialog modal-dialog-centered" role="document">
         <div className="modal-content">
          <div className="modal-header">
           <h5 className="modal-title" id="applicantModalTitle">
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
             <h5 className="card-title text-primary row m-auto p-auto">
              <dt id="applicantModalUserNm">김차일</dt>&nbsp;{" "}
              <dd id="applicantModalUserPhone">010-1111-1411</dd>
             </h5>
             <div className="card-text row">
              <dl className="col-6 row mb-auto">
               <dt className="col-3 h-25 d-flex justify-content-end">차량:</dt>{" "}
               <dd id="applicantModalCarAndTon" className="col-9 h-25 ">
                1t 냉장
               </dd>
               <dt className="col-3 h-25 d-flex justify-content-end">경력:</dt>{" "}
               <dd className="col-9 h-25" id="applicantModalCarrer">
                1년 미만
               </dd>
              </dl>
              <dl className="col-6">
               <dt className="mb-3">면허 및 자격</dt>
               <dd id="applicantModalCert"></dd>
              </dl>
              <dl className="col-12 m-auto">
               <dt className="mb-3">메시지</dt>
               <dd id="applicantModalMsg">
                성실함으로 열심히 하겠습니다. 처음이지만 적극적으로!
               </dd>
              </dl>
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
           <button
            className="btn badge-warning"
            id="applicantModalStatusIngBtn"
            type="button"
           >
            연락중
           </button>
           <button
            className="btn badge-success"
            id="applicantModalStatusCompleteBtn"
            type="button"
           >
            채용확정
           </button>
           <button
            className="btn btn-danger"
            id="applicantModalStatusRejectBtn"
            type="button"
           >
            채용거절
           </button>
          </div>
         </div>
        </div>
       </div>
      </div>
     </main>
    ) : null}
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
export default connect(mapStateToProps)(ApplicantManage);
