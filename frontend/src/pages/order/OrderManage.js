import React from "react";
import { connect } from "react-redux";
import MainStructure from "components/structure/MainStructure";
import "./OrderManage.css";
import axios from "axios";
import "styles/datatables.css";
import $ from "jquery";
$.DataTable = require("datatables.net");
import "datatables.net-dt";
import { Component } from "react";
import OrderManageItem from "./OrderManageItem";

class OrderManage extends Component {
 constructor(props) {
  super(props);
  this.state = {
   names: [],
   orderList701: [],
   orderList702: [],
   orderList703: [],
   orderList: [],
   workGroups: [],
   changeWorkGroup: "all",
   changeWorkGroup701: "all",
   changeWorkGroup702: "all",
   changeWorkGroup703: "all",
  };

  this._changeWorkGroup = (e) => {
   this.setState({
    changeWorkGroup: $(e.target).val(),
   });
  };
  this._changeWorkGroup701 = (e) => {
   this.setState({
    changeWorkGroup701: $(e.target).val(),
   });
  };
  this._changeWorkGroup702 = (e) => {
   this.setState({
    changeWorkGroup702: $(e.target).val(),
   });
  };
  this._changeWorkGroup703 = (e) => {
   this.setState({
    changeWorkGroup703: $(e.target).val(),
   });
  };

  this._getCarrierOrders = async () => {
   const userInfo = JSON.parse(localStorage.getItem("userInfo"));
   let { data } = await axios.get("/v1/order/carrier/" + userInfo.carrierSeq, {
    headers: {
     "Content-Type": "application/json",
     "X-AUTH-TOKEN": userInfo.token,
    },
   });
   if (data.list.length > 0) {
    let orderList701 = data.list.filter((n) => {
     return n.status == "0701";
    });
    let orderList702 = data.list.filter((n) => {
     return n.status == "0702";
    });
    let orderList703 = data.list.filter((n) => {
     return n.status == "0703";
    });
    let orderList = data.list.filter((n) => {
     return n.status != "0704";
    });
    this.setState({ orderList701, orderList702, orderList703, orderList });
   }

   let data2 = await axios.get("/v1/carrier", {
    headers: {
     "Content-Type": "application/json",
     "X-AUTH-TOKEN": userInfo.token,
    },
   });

   if (data2.data.data.workGroups) {
    this.setState({ workGroups: data2.data.data.workGroups });
   }
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
  this._getCarrierOrders();
 }
 componentDidUpdate() {
 }
 componentWillUnmount() {
  $(".data-table-wrapper").find("table").DataTable().destroy(true);
 }
 reloadTableData = (names) => {
  const table = $(".data-table-wrapper").find("table").DataTable();
  table.clear();
  table.rows.add(names);
  table.draw();
 };

 updateTable = (names) => {
  const table = $(".data-table-wrapper").find("table").DataTable();
  let dataChanged = false;
  table.rows().every(function () {
   const oldNameData = this.data();
   const newNameData = names.find((nameData) => {
    return nameData.name === oldNameData.name;
   });
   if (oldNameData.nickname !== newNameData.nickname) {
    dataChanged = true;
    this.data(newNameData);
   }
   return true; // RCA esLint configuration wants us to
   // return something
  });

  if (dataChanged) {
   table.draw();
  }
 };
 /*
 shouldComponentUpdate(nextProps, nextState) {
   
  if (nextState.names.length !== this.state.names.length) {
   this.reloadTableData(nextState.names);
  } else {
   this.updateTable(nextState.names);
  }
  if (nextState.orderList701.length !== this.state.orderList701.length) {
   return true;
  }
  if (nextState.orderList702.length !== this.state.orderList702.length) {
   return true;
  }
  if (nextState.orderList703.length !== this.state.orderList703.length) {
   return true;
  }
  if (nextState.orderList.length !== this.state.orderList.length) {
   return true;
  }
  if (nextState.workGroups.length !== this.state.workGroups.length) {
   return true;
  }
  return false;
  
 }
*/
 onAddClick(name, nickname) {
  let updated = false;
  const result = this.state.names.map((nameData) => {
   if (nameData.name === name) {
    updated = true;
    return { name, nickname };
   }
   return nameData;
  });
  if (!updated) {
   result.push({ name, nickname });
  }

  this.setState({
   names: result,
  });
 }

 render() {
  const {
   workGroups,
   orderList701,
   orderList702,
   orderList703,
   orderList,
   changeWorkGroup,
   changeWorkGroup701,
   changeWorkGroup702,
   changeWorkGroup703,
  } = this.state;
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
         <span>오더 관리</span>
        </h1>
        <div className="page-header-subtitle">오더와 지원자를 관리합니다.</div>
       </div>
      </div>
     </div>
     <div className="container-fluid mt-n10">
      <div className="row">
       <div className="col-lg-12">
        <div className="card">
         <div className="card-header card-header-tabs card-header-primary pb-0">
          <div className="nav-tabs-navigation">
           <div className="nav-tabs-wrapper">
            {/*<span className="nav-tabs-title">Tasks:</span>*/}
            <ul className="nav nav-tabs" data-tabs="tabs">
             <li className="nav-item">
              <a
               className="nav-link d-flex align-items-center active"
               href="#ing"
               data-toggle="tab"
              >
               <i className="fas fa-fw fa-hourglass-start"></i>{" "}
               <span className="sm-display-none">진행중</span>
               <div className="ripple-container"></div>
              </a>
             </li>
             <li className="nav-item">
              <a
               className="nav-link d-flex align-items-center"
               href="#complete"
               data-toggle="tab"
              >
               <i className="fa fa-fw fa-hourglass-half"></i>{" "}
               <span className="sm-display-none">채용종료</span>
               <div className="ripple-container"></div>
              </a>
             </li>
             <li className="nav-item">
              <a
               className="nav-link d-flex align-items-center"
               href="#temp"
               data-toggle="tab"
              >
               <i className="fa fa-fw fa-hourglass-end"></i>{" "}
               <span className="sm-display-none">임시저장</span>
               <div className="ripple-container"></div>
              </a>
             </li>
             <li className="nav-item">
              <a
               className="nav-link d-flex align-items-center"
               href="#all"
               data-toggle="tab"
              >
               <i className="fa fa-fw fa-hourglass"></i>{" "}
               <span className="sm-display-none">전체</span>
               <div className="ripple-container"></div>
              </a>
             </li>
            </ul>
           </div>
          </div>
         </div>
         <div className="card-body">
          <div className="tab-content">
           <div className="tab-pane col-12 active" id="ing">
            <div className="form-row my-2 mb-3">
             {/*
             <label
              htmlFor="orderRegisWorkGroup"
              className="col-form-label col-2"
             >
              운송그룹
             </label>*/}
             <select
              className="form-control col-12"
              id="orderRegisWorkGroup"
              onChange={this._changeWorkGroup701}
             >
              <option value="all">전체보기</option>
              {workGroups.length > 0
               ? workGroups.map((obj, index) => {
                  return (
                   <option key={obj.workGroupNm} value={obj.workGroupNm}>
                    {obj.workGroupNm}
                   </option>
                  );
                 })
               : null}
             </select>
            </div>
            <table className="table col-12">
             <tbody className="col-12">
              {orderList701.length > 0
               ? orderList701.map((obj, index) => {
                  return (
                   <OrderManageItem
                    getCarrierOrders={this._getCarrierOrders}
                    obj={obj}
                    key={obj.orderSeq}
                    rcritTypeCodes={this.props.rcritTypeCodes}
                    tonTypeCodes={this.props.tonTypeCodes}
                    carTypeCodes={this.props.carTypeCodes}
                    changeWorkGroup={changeWorkGroup701}
                    tabType={"ing"}
                    display={
                     changeWorkGroup701 == obj.workGroupNm ||
                     changeWorkGroup701 == "all"
                      ? "table-row"
                      : "none"
                    }
                   />
                  );
                 })
               : null}
             </tbody>
            </table>
           </div>
           <div className="tab-pane col-12" id="complete">
            <div className="form-row my-2 mb-3">
             {/*
             <label
              htmlFor="orderRegisWorkGroup"
              className="col-form-label col-2"
             >
              운송그룹
             </label>*/}
             <select
              className="form-control col-12"
              id="orderRegisWorkGroup"
              onChange={this._changeWorkGroup702}
             >
              <option value="all">전체보기</option>
              {workGroups.length > 0
               ? workGroups.map((obj, index) => {
                  return (
                   <option key={obj.workGroupNm} value={obj.workGroupNm}>
                    {obj.workGroupNm}
                   </option>
                  );
                 })
               : null}
             </select>
            </div>
            <table className="table col-12">
             <tbody className="col-12">
              {orderList702.length > 0
               ? orderList702.map((obj, index) => {
                  return (
                   <OrderManageItem
                    getCarrierOrders={this._getCarrierOrders}
                    obj={obj}
                    key={obj.orderSeq}
                    rcritTypeCodes={this.props.rcritTypeCodes}
                    tonTypeCodes={this.props.tonTypeCodes}
                    carTypeCodes={this.props.carTypeCodes}
                    tabType={"complete"}
                    display={
                     changeWorkGroup702 == obj.workGroupNm ||
                     changeWorkGroup702 == "all"
                      ? "table-row"
                      : "none"
                    }
                   />
                  );
                 })
               : null}
             </tbody>
            </table>
           </div>
           <div className="tab-pane col-12" id="temp">
            <div className="form-row my-2 mb-3">
             {/*
             <label
              htmlFor="orderRegisWorkGroup"
              className="col-form-label col-2"
             >
              운송그룹
             </label>*/}
             <select
              className="form-control col-12"
              id="orderRegisWorkGroup"
              onChange={this._changeWorkGroup703}
             >
              <option value="all">전체보기</option>
              {workGroups.length > 0
               ? workGroups.map((obj, index) => {
                  return (
                   <option key={obj.workGroupNm} value={obj.workGroupNm}>
                    {obj.workGroupNm}
                   </option>
                  );
                 })
               : null}
             </select>
            </div>
            <table className="table col-12">
             <tbody className="col-12">
              {orderList703.length > 0
               ? orderList703.map((obj, index) => {
                  return (
                   <OrderManageItem
                    getCarrierOrders={this._getCarrierOrders}
                    obj={obj}
                    key={obj.orderSeq}
                    rcritTypeCodes={this.props.rcritTypeCodes}
                    tonTypeCodes={this.props.tonTypeCodes}
                    carTypeCodes={this.props.carTypeCodes}
                    tabType={"temp"}
                    display={
                     changeWorkGroup703 == obj.workGroupNm ||
                     changeWorkGroup703 == "all"
                      ? "table-row"
                      : "none"
                    }
                   />
                  );
                 })
               : null}
             </tbody>
            </table>

            {/*
            <table ref="main" className="d-none" names={this.state.names} />
            <Input
             className="d-none"
             onAddClick={(name, nickname) => {
              this.onAddClick(name, nickname);
             }}
            />
             */}
           </div>

           <div className="tab-pane col-12" id="all">
            <div className="form-row my-2 mb-3">
             {/*
             <label
              htmlFor="orderRegisWorkGroup"
              className="col-form-label col-2"
             >
              운송그룹
             </label>*/}
             <select
              className="form-control col-12"
              id="orderRegisWorkGroup"
              onChange={this._changeWorkGroup}
             >
              <option value="all">전체보기</option>
              {workGroups.length > 0
               ? workGroups.map((obj, index) => {
                  return (
                   <option key={obj.workGroupNm} value={obj.workGroupNm}>
                    {obj.workGroupNm}
                   </option>
                  );
                 })
               : null}
             </select>
            </div>
            <table className="table col-12">
             <tbody className="col-12">
              {orderList.length > 0
               ? orderList.map((obj, index) => {
                  return (
                   <OrderManageItem
                    getCarrierOrders={this._getCarrierOrders}
                    obj={obj}
                    key={obj.orderSeq}
                    rcritTypeCodes={this.props.rcritTypeCodes}
                    tonTypeCodes={this.props.tonTypeCodes}
                    carTypeCodes={this.props.carTypeCodes}
                    tabType={"all"}
                    display={
                     changeWorkGroup == obj.workGroupNm ||
                     changeWorkGroup == "all"
                      ? "table-row"
                      : "none"
                    }
                   />
                  );
                 })
               : null}
             </tbody>
            </table>
           </div>
          </div>
         </div>
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
         <h5 className="modal-title">마감이 완료되었습니다.</h5>
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
     <div
      className="modal fade"
      id="deleteCompleteModalPopup"
      tabIndex="-1"
      role="dialog"
      aria-labelledby="deleteCompleteModalPopup"
      aria-hidden="true"
     >
      <div className="modal-dialog modal-dialog-centered" role="document">
       <div className="modal-content">
        <div className="modal-header">
         <h5 className="modal-title">삭제 완료되었습니다.</h5>
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

export default connect(mapStateToProps)(OrderManage);
