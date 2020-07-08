import React from "react";
import AuthContainer from "containers/AuthContainer";

const Auth = ({ match }) => {
 const { kind } = match.params;
 return (
  <>
   <AuthContainer kind={kind} />
  </>
 );
};

export default Auth;
