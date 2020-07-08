import React, { useState, useRef } from "react";
import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import axios from "axios";
import $ from "jquery";

const PasswordRecovery = ({}) => {
 const { handleSubmit, register, errors, watch } = useForm();
 const onSubmit = (values) => {
  console.log(values);
  const params = new URLSearchParams();
  params.append("email", values.userEmail);
  axios.post("/v1/passwordRecovery", params).then((res) => {
   if (res.status == 200) {
    $("#goToLogin").modal();
   }
  });
 };

 const authForm = useRef(null);
 const triggerSubmit = () => {
  authForm.current.dispatchEvent(new Event("submit"));
 };
 return (
  <div className="container">
   <div className="row justify-content-center">
    <div className="col-lg-5">
     <div className="card shadow-lg border-0 rounded-lg mt-5">
      <div className="card-header justify-content-center">
       <h3 className="font-weight-light my-4">비밀번호 초기화</h3>
      </div>
      <div className="card-body">
       <div className="small mb-3 text-muted">
        가입할 때 입력한 이메일을 제출하시면 이메일로 사용자 아이디와 초기화된
        비밀번호를 전송합니다.
       </div>
       <form onSubmit={handleSubmit(onSubmit)} ref={authForm}>
        <div className="form-group">
         <label className="small mb-1" htmlFor="userEmail">
          사용자 이메일
         </label>
         <input
          className="form-control py-4"
          id="userEmail"
          name="userEmail"
          type="email"
          aria-describedby="emailHelp"
          placeholder="ex) youngrag.seo@timf.co.kr"
          ref={register({
           required: "true",
           pattern: {
            value: /^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$/i,
           },
          })}
         />
         <div>
          {errors.userEmail && (
           <ul className="errorsList">
            <li>올바른 이메일 형태로 적어주세요.</li>
           </ul>
          )}
         </div>
        </div>
        <div className="form-group d-flex align-items-center justify-content-between mt-4 mb-0">
         <Link to={`/auth/login`} className="small">
          로그인 화면으로 돌아가기
         </Link>
         <a className="btn btn-primary" onClick={triggerSubmit}>
          패스워드 초기화 하기
         </a>
        </div>
       </form>
      </div>
      <div className="card-footer text-center">
       <div className="small">
        <Link to={`/auth/register`} className={"description"}>
         회원가입이 필요하시나요?
        </Link>
       </div>
      </div>
     </div>
    </div>
   </div>
   <div
    className="modal fade show"
    id="goToLogin"
    tabIndex="-1"
    role="dialog"
    aria-labelledby="exampleModalLabel"
    style={{ display: "none" }}
    aria-hidden="true"
   >
    <div className="modal-dialog" role="document">
     <div className="modal-content">
      <div className="modal-header">
       <h5 className="modal-title" id="exampleModalLabel">
        비밀번호 초기화 이메일이 발송되었습니다.
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
       <a
        className="btn btn-primary"
        onClick={() => {
         $("goToLogin").modal("hide");
         location.href = "/auth/login";
        }}
       >
        확인
       </a>
      </div>
     </div>
    </div>
   </div>
  </div>
 );
};

export default PasswordRecovery;
