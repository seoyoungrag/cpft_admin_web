import React, { useState, useRef } from "react";
import "./AuthForm.css";
import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";

const cx = (txt) => {
 return txt;
};
const AuthForm = ({
 kind,
 onChangeInput,
 userLoginId,
 userLoginPw,
 onLogin,
 onRegister,
 error,
}) => {
 const [rememberMe, setRememberMe] = useState(false);
 const handleChange = (e) => {
  const { name, value } = e.target;
  const dataValue = e.target.type == "checkbox" ? e.target.checked : value;
  onChangeInput({ name, value: dataValue });
 };
 const handleKeyPress = (e) => {
  if (e.key === "Enter") {
   triggerSubmit();
  }
 };
 const toggleRememberMe = (e) => {
  setRememberMe(!rememberMe);
  handleChange(e);
 };
 const { handleSubmit, register, errors, watch } = useForm();

 const onSubmit = (values) => {
  if (kind == "register") {
   onRegister();
  } else {
   onLogin();
  }
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
       <h3 className="font-weight-light my-4">
        {kind == "login" ? "로그인" : "회원가입"}
       </h3>
      </div>
      <div className="card-body">
       <form onSubmit={handleSubmit(onSubmit)} ref={authForm}>
        <div className="form-group">
         <label className="small mb-1" htmlFor="userLoginId">
          아이디
         </label>
         <input
          className="form-control py-4"
          id="userLoginId"
          name="userLoginId"
          type="text"
          placeholder="youngrag.seo"
          onChange={handleChange}
          onKeyPress={handleKeyPress}
          ref={register({
           required: "true",
           minLength: 4,
           maxLength: 20,
          })}
         />
        </div>
        {errors.userLoginId && (
         <ul className="errorsList">
          <li>4~20자로 적어주세요.</li>
         </ul>
        )}
        <div className="form-group">
         <label className="small mb-1" htmlFor="userLoginPw">
          비밀번호
         </label>
         <input
          className="form-control py-4"
          id="userLoginPw"
          name="userLoginPw"
          type="password"
          placeholder="1234"
          onChange={handleChange}
          onKeyPress={handleKeyPress}
          autoComplete="off"
          ref={register({
           required: "true",
           minLength: 4,
           maxLength: 20,
          })}
         />
        </div>
        {errors.userLoginPw && (
         <ul className="errorsList">
          <li>4~20자로 적어주세요.</li>
         </ul>
        )}
        {kind === "register" ? (
         <div>
          <div className="form-group">
           <label className="small mb-1" htmlFor="userLoginPw_repeat">
            비밀번호 확인
           </label>
           <input
            className="form-control py-4"
            id="userLoginPw_repeat"
            name="userLoginPw_repeat"
            type="password"
            placeholder="1234"
            ref={register({
             validate: (value) => {
              return value === watch("userLoginPw");
             },
            })}
           />
          </div>
          {errors.userLoginPw_repeat && (
           <ul className="errorsList">
            <li>비밀번호가 일치하지 않습니다.</li>
           </ul>
          )}
          <div className="form-group">
           <label className="small mb-1" htmlFor="userNm">
            사용자 이름
           </label>
           <input
            className="form-control py-4"
            id="userNm"
            name="userNm"
            type="text"
            placeholder="서영락"
            onChange={handleChange}
            onKeyPress={handleKeyPress}
            ref={register({
             required: "true",
             minLength: 2,
             maxLength: 6,
            })}
           />
          </div>
          {errors.userNm && (
           <ul className="errorsList">
            <li>2~6자로 적어주세요.</li>
           </ul>
          )}
          <div className="form-group">
           <label className="small mb-1" htmlFor="userNm">
            사용자 이메일
           </label>
           <input
            className="form-control py-4"
            id="userEmail"
            name="userEmail"
            type="email"
            placeholder="youngrag.seo@timf.co.kr"
            onChange={handleChange}
            onKeyPress={handleKeyPress}
            ref={register({
             required: "true",
             pattern: {
              value: /^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$/i,
             },
            })}
           />
          </div>
          {errors.userEmail && (
           <ul className="errorsList">
            <li>올바른 이메일 형태로 적어주세요.</li>
           </ul>
          )}
         </div>
        ) : null}
        <ul className="errorsList">
         {error.triggered && <li>{error.message}</li>}
        </ul>
        {/*<div className="form-group">
         <div className="custom-control custom-checkbox">
          <label
           className="col-form-label pr-3 radio-inline"
           htmlFor="rememberMe"
          >
           <input
            className="checkbox mr-3"
            id="rememberMe"
            name="rememberMe"
            type="checkbox"
            useref="rememberMe"
            onChange={toggleRememberMe}
            defaultChecked={rememberMe}
           />
           로그인 유지하기
          </label>
         </div>
        </div>
          */}
        <div className="form-group d-flex align-items-center justify-content-between mt-4 mb-0">
         <Link className="small" to="/auth/passwordRecovery">
          비밀번호를 잊으셨나요?
         </Link>
         {kind === "register" ? (
          <div className="btn btn-primary" onClick={triggerSubmit}>
           회원가입
          </div>
         ) : (
          <div className="btn btn-primary" onClick={triggerSubmit}>
           로그인
          </div>
         )}
        </div>
       </form>
      </div>
      <div className="card-footer text-center">
       <div className="small">
        {/*<a href="register-basic.html">Need an account? Sign up!</a>*/}

        {kind === "register" ? (
         <Link to={`/auth/login`} className={cx("description")}>
          이미 계정을 가지고 계신가요?
         </Link>
        ) : (
         <Link to={`/auth/register`} className={cx("description")}>
          회원가입이 필요하시나요?
         </Link>
        )}
       </div>
      </div>
     </div>
    </div>
   </div>
  </div>
 );
};

export default AuthForm;
