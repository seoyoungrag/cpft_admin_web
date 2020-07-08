import React, { Component } from "react";
import { connect } from "react-redux";
import AuthForm from "components/auth/AuthForm";
import { withRouter } from "react-router-dom";
import * as authActions from "store/modules/auth";

export class AuthContainer extends Component {
 componentDidMount() {
  attachJiraIssueColletor();
  console.log("authcontainer mount");
  this.initialize();
 }

 componentDidUpdate(prevProps, prevState) {
  // 하단에 AuthContainer를 withRouter로 감쌌기 때문에, history를 props로 이용할수 있습니다.
  //console.log("authcontainer componentDidUpdate");
  const { history } = this.props;
  //console.log("authcontainer " + prevProps.kind + " vs " + this.props.kind);
  if (prevProps.kind !== this.props.kind) {
   this.initialize();
  }

  //console.log("authcontainer " + prevProps.logged + " vs " + this.props.logged);
  if (prevProps.logged !== this.props.logged && this.props.logged) {
   // logged가 true가 되면 localStorage에 값을 저장합니다.
   //console.log("rememberMe");
   //console.log(this.props.rememberMe);
   //if (this.props.rememberMe == true) {
   //console.log(this.props.userInfo.token);
   console.log(this.props);
   localStorage.setItem(
    "userInfo",
    JSON.stringify({
     carrierSeq: this.props.userInfo.carrierSeq,
     userSeq: this.props.userInfo.userSeq,
     userLoginId: this.props.userInfo.userLoginId,
     userNm: this.props.userInfo.userNm,
     userEmail: this.props.userInfo.userEmail,
     token: this.props.userInfo.token,
    })
   );
   //}
   // 값을 저장후, main페이지로 이동시켜줍니다.
   history.push("/");
  }
 }

 initialize = () => {
  const { initializeInput, initializeError } = this.props;
  initializeError();
  initializeInput();
 };

 handleChangeInput = ({ name, value }) => {
  const { changeInput } = this.props;
  changeInput({ name, value });
 };

 handleLogin = () => {
  const { login } = this.props;
  login();
 };

 handleRegister = () => {
  const { register } = this.props;
  register();
 };
 render() {
  const { kind, userLoginId, userLoginPw, error } = this.props;
  const { handleChangeInput, handleLogin, handleRegister } = this;
  return (
   <AuthForm
    kind={kind}
    userLoginId={userLoginId}
    userLoginPw={userLoginPw}
    onChangeInput={handleChangeInput}
    onLogin={handleLogin}
    onRegister={handleRegister}
    error={error}
   />
  );
 }
}

const mapStateToProps = (state) => ({
 userLoginId: state.auth.form.userLoginId,
 userLoginPw: state.auth.form.userLoginPw,
 userInfo: state.auth.userInfo,
 logged: state.auth.logged,
 error: state.auth.error,
 rememberMe: state.auth.rememberMe,
});

const mapDispatchToProps = (dispatch) => {
 return {
  initializeInput: () => {
   dispatch(authActions.initializeInput());
  },
  changeInput: ({ name, value }) => {
   dispatch(authActions.changeInput({ name, value }));
  },
  initializeError: () => {
   dispatch(authActions.initializeError());
  },
  register: () => {
   dispatch(authActions.register());
  },
  login: () => {
   dispatch(authActions.login());
  },
 };
};

export default withRouter(
 connect(mapStateToProps, mapDispatchToProps)(AuthContainer)
);
