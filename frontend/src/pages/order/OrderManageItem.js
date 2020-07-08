import React, { Component } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import $ from "jquery";

class OrderManageItem extends Component {
 constructor() {
  super();
 }

 render() {
  const { obj, tabType, getCarrierOrders } = this.props;
  if(obj.status=='0702'){
  console.log(obj);
  }
  this.modalClickUpdate = (obj, tabType) => {
   console.log("#updateModalPopup" + obj.orderSeq + tabType);
   $("#updateModalPopup" + obj.orderSeq + tabType).modal("hide");
  };
  this.modalClickComplete = (obj, tabType) => {
   console.log("#completeModalPopup" + obj.orderSeq + tabType);
   $("#completeModalPopup" + obj.orderSeq + tabType).modal("hide");
   const userInfo = JSON.parse(localStorage.getItem("userInfo"));
   obj.status = "0702";
   axios
    .post("/v1/order", obj, {
     headers: {
      "Content-Type": "application/json",
      "X-AUTH-TOKEN": userInfo.token,
     },
    })
    .then((res) => {
     if (res.status == 200) {
      //console.log(res.data.data.orderSeq);
      if (res.data.data.orderSeq > 0) {
       getCarrierOrders();
       $("#saveCompleteModalPopup").modal();
      }
     } else {
      $("#saveFailModalPopup").modal();
     }
    });
  };
  this.modalClickDelete = (obj, tabType) => {
   console.log("#deleteModalPopup" + obj.orderSeq + tabType);
   $("#deleteModalPopup" + obj.orderSeq + tabType).modal("hide");
   const userInfo = JSON.parse(localStorage.getItem("userInfo"));
   obj.status = "0704";
   axios
    .post("/v1/order", obj, {
     headers: {
      "Content-Type": "application/json",
      "X-AUTH-TOKEN": userInfo.token,
     },
    })
    .then((res) => {
     if (res.status == 200) {
      //console.log(res.data.data.orderSeq);
      if (res.data.data.orderSeq > 0) {
       getCarrierOrders();
       $("#deleteCompleteModalPopup").modal();
      }
     } else {
      $("#saveFailModalPopup").modal();
     }
    });
  };
  return (
   <tr style={{ display: this.props.display }} className="col-12">
    <td className="col-12">
     <div className="inner row col-sm-12 col-12">
      <div className="col-12 col-sm-7">
       <div className="jobTitWrap">
        <span className="infoBx">
         {obj.status == "0701" ? (
          <span className="badge badge-primary align-top mr-1">진행중</span>
         ) : obj.status == "0702" ? (
          <span className="badge badge-success align-top mr-1">종료</span>
         ) : (
          <span className="badge badge-info align-top mr-1">임시저장</span>
         )}
         <span className="date">
          <span className="date">
           W{obj.workGroupNm}C{obj.carrierSeq}U{obj.userSeq}O{obj.orderSeq}{" "}
           <span className="tahoma">
            {obj.workGroupNm} {obj.workGroupManager}
           </span>
          </span>
          <span className="name">작성자: {obj.userNm}</span>
         </span>
        </span>

        <a href="#" className="tit devLinkExpire col-12 row">
         <h4 className="h4 d-inline-block">
          <em className="used">
           {this.props.rcritTypeCodes
            .filter((e) => {
             return e.code == obj.rcritType;
            })
            .map((r) => {
             return r.codeValue;
            })}
          </em>{" "}
          {obj.carrierNm}{" "}
          {this.props.tonTypeCodes
           .filter((e) => {
            return e.code == obj.tonType;
           })
           .map((r) => {
            return r.codeValue;
           })}{" "}
          {this.props.carTypeCodes
           .filter((e) => {
            return e.code == obj.carType;
           })
           .map((r) => {
            return r.codeValue;
           })}{" "}
          {obj.workingDaysType == "fiveDay"
           ? "주5일"
           : obj.workingDaysType == "sixDay"
           ? "주6일"
           : null}
          &nbsp;{obj.payAmt} {obj.detailMatter}
         </h4>
        </a>
       </div>

       <div className="apyStatusBoard">
        <div className="tbCol tbDate">
         <span className="date">
          <span className="tahoma">
           {obj.workingArea} {"->"} {obj.opratSctn}
           <span className="mday">
            <span className="tahoma">{obj.createdAt}</span> 등록
           </span>
          </span>
         </span>
        </div>
       </div>
      </div>
      {obj.status == "0701" ? (
       <div className="col-12 col-sm-5 apyStatusBoard ">
        <div className="d-flex justify-content-end">
         <button
          type="button"
          className="btn btn-primary mx-1"
          data-toggle="modal"
          data-target={`#updateModalPopup${obj.orderSeq}${tabType}`}
         >
          수정
         </button>
         <button
          type="button"
          data-toggle="modal"
          data-target={`#completeModalPopup${obj.orderSeq}${tabType}`}
          className="btn btn-success mx-1"
         >
          <span>마감</span>
         </button>
         <button
          type="button"
          data-toggle="modal"
          data-target={`#deleteModalPopup${obj.orderSeq}${tabType}`}
          className="btn btn-danger  ml-1 mr-0"
         >
          <span>삭제</span>
         </button>
        </div>

        <div className="col-12 mt-3 p-0">
         <ul className="boardItem">
          <li className="w-25">
           <strong className="stepTit">지원자</strong>
           <a href="#" className="itemNum tahoma devLinkExpire" data-pts="-77">
             {obj.orderTruckOwners.length}
           </a>
          </li>
          <li className="apyStatusNotRead w-25">
           <strong className="stepTit">열람</strong>
           <a href="#" className="itemNum tahoma devLinkExpire" data-pts="-77">
            {obj.orderTruckOwners.filter((obj,idx)=>obj.isRead=='Y').length}
           </a>
          </li>
          <li className="on w-25">
           <strong className="stepTit">연락중</strong>
           <a href="#" className="itemNum tahoma devLinkExpire" data-pts="-77">
            {obj.orderTruckOwners.filter((obj,idx)=>obj.status=='0801').length}
           </a>
          </li>
          <li className="w-25">
           <strong className="stepTit">최종합격</strong>
           <a href="#" className="itemNum tahoma devLinkExpire" data-pts="-77">
            {obj.orderTruckOwners.filter((obj,idx)=>obj.status=='0802').length}
           </a>
          </li>
         </ul>
        </div>
       </div>
      ) : obj.status == "0702" ? (
       <div className="col-12 col-sm-5 apyStatusBoard ">
        <div className="col-12 mt-3 p-0">
         <ul className="boardItem">
          <li className="w-25">
           <strong className="stepTit">지원자</strong>
           <a href="#" className="itemNum tahoma devLinkExpire" data-pts="-77">
             {obj.orderTruckOwners.length}
           </a>
          </li>
          <li className="apyStatusNotRead w-25">
           <strong className="stepTit">열람</strong>
           <a href="#" className="itemNum tahoma devLinkExpire" data-pts="-77">
            {obj.orderTruckOwners.filter((obj,idx)=>obj.isRead=='Y').length}
           </a>
          </li>
          <li className="on w-25">
           <strong className="stepTit">연락중</strong>
           <a href="#" className="itemNum tahoma devLinkExpire" data-pts="-77">
            {obj.orderTruckOwners.filter((obj,idx)=>obj.status=='0801').length}
           </a>
          </li>
          <li className="w-25">
           <strong className="stepTit">최종합격</strong>
           <a href="#" className="itemNum tahoma devLinkExpire" data-pts="-77">
            {obj.orderTruckOwners.filter((obj,idx)=>obj.status=='0802').length}
           </a>
          </li>
         </ul>
        </div>
       </div>
      ) : (
       <div className="col-12 col-sm-5 apyStatusBoard ">
        <div className="d-flex justify-content-end">
         <button
          type="button"
          className="btn btn-primary mx-1"
          data-toggle="modal"
          data-target={`#updateModalPopup${obj.orderSeq}${tabType}`}
         >
          수정
         </button>
         <button
          type="button"
          data-toggle="modal"
          data-target={`#deleteModalPopup${obj.orderSeq}${tabType}`}
          className="btn btn-danger  ml-1 mr-0"
         >
          <span>삭제</span>
         </button>
        </div>
       </div>
      )}
     </div>
     <div
      className="modal fade"
      id={`updateModalPopup${obj.orderSeq}${tabType}`}
      tabIndex="-1"
      role="dialog"
      aria-labelledby={`updateModalPopup${obj.orderSeq}${tabType}`}
      aria-hidden="true"
     >
      <div className="modal-dialog modal-dialog-centered" role="document">
       <div className="modal-content">
        <div className="modal-header">
         <h5 className="modal-title">수정 하시겠습니까?</h5>
         <button
          className="close"
          type="button"
          data-dismiss="modal"
          aria-label="Close"
         >
          <span aria-hidden="true">×</span>
         </button>
        </div>
        <div className="modal-body">수정 페이지로 이동합니다.</div>
        <div className="modal-footer">
         <button
          className="btn btn-secondary"
          type="button"
          data-dismiss="modal"
         >
          아니오
         </button>
         <Link
          to={{ pathname: `/order/regist`, state: { order: obj } }}
          type="button"
          className="btn btn-secondary"
          onClick={() => this.modalClickUpdate(obj, tabType)}
         >
          네
         </Link>
        </div>
       </div>
      </div>
     </div>
     <div
      className="modal fade"
      id={`completeModalPopup${obj.orderSeq}${tabType}`}
      tabIndex="-1"
      role="dialog"
      aria-labelledby={`completeModalPopup${obj.orderSeq}${tabType}`}
      aria-hidden="true"
     >
      <div className="modal-dialog modal-dialog-centered" role="document">
       <div className="modal-content">
        <div className="modal-header">
         <h5 className="modal-title">공고를 마감 하시겠습니까?</h5>
         <button
          className="close"
          type="button"
          data-dismiss="modal"
          aria-label="Close"
         >
          <span aria-hidden="true">×</span>
         </button>
        </div>
        <div className="modal-body">마감 후 복원할 수 없습니다.</div>
        <div className="modal-footer">
         <button
          className="btn btn-secondary"
          type="button"
          data-dismiss="modal"
         >
          아니오
         </button>
         <button
          type="button"
          className="btn btn-secondary"
          onClick={() => this.modalClickComplete(obj, tabType)}
         >
          네
         </button>
        </div>
       </div>
      </div>
     </div>
     <div
      className="modal fade"
      id={`deleteModalPopup${obj.orderSeq}${tabType}`}
      tabIndex="-1"
      role="dialog"
      aria-labelledby={`deleteModalPopup${obj.orderSeq}${tabType}`}
      aria-hidden="true"
     >
      <div className="modal-dialog modal-dialog-centered" role="document">
       <div className="modal-content">
        <div className="modal-header">
         <h5 className="modal-title">공고를 삭제 하시겠습니까?</h5>
         <button
          className="close"
          type="button"
          data-dismiss="modal"
          aria-label="Close"
         >
          <span aria-hidden="true">×</span>
         </button>
        </div>
        <div className="modal-body">삭제 후 복원할 수 없습니다.</div>
        <div className="modal-footer">
         <button
          className="btn btn-secondary"
          type="button"
          data-dismiss="modal"
         >
          아니오
         </button>
         <button
          type="button"
          className="btn btn-secondary"
          onClick={() => this.modalClickDelete(obj, tabType)}
         >
          네
         </button>
        </div>
       </div>
      </div>
     </div>
    </td>
   </tr>
  );
 }
}
export default OrderManageItem;
