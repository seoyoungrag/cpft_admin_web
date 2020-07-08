import React from "react";
import { Route, Switch, withRouter } from "react-router-dom";

import BaseContainer from "containers/BaseContainer";
import MainStructure from "components/structure/MainStructure";

import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";

import reactElementToJSXString from "react-element-to-jsx-string";

import {
 Error400,
 Error401,
 Error403,
 Error404,
 Error500,
 Error503,
 Test,
 Main,
 Auth,
 NotFound,
 PasswordRecovery,
} from "pages";

import OrderRegist from "pages/order/OrderRegist";
import OrderManage from "pages/order/OrderManage";
import ApplicantManage from "pages/order/ApplicantManage";
import CarOwnerList from "pages/truckOwner/TruckOwnerList";
import DtStmnList from "pages/truckOwner/DtStmnList";
import ElctrnTaxBill from "pages/elctrnTaxBill/ElctrnTaxBill";

import BlackList from "pages/order/BlackList";
//import "tabler-react/dist/Tabler.css";
import ReactStore from "ReactStore";

class App extends React.Component {
 static contextType = ReactStore;
 componentDidUpdate = () => {
  this.props.initNav();
 };
 render() {
  return (
   <>
    <Switch>
     <Route exact path="/" component={Main} />
     <Route exact path="/auth/passwordRecovery" component={PasswordRecovery} />
     <Route exact path="/auth/:kind" component={Auth} />
     <Route exact path="/test" component={Test} />
     <Route exact path="/order/regist" component={OrderRegist} />
     <Route exact path="/order/manage" component={OrderManage} />
     <Route exact path="/order/applicant/manage" component={ApplicantManage} />
     <Route exact path="/order/black/list" component={BlackList} />
     <Route exact path="/auth/black/list" component={PasswordRecovery} />
     <Route exact path="/truckOwner/list" component={CarOwnerList} />
     <Route exact path="/dtStmn/list" component={DtStmnList} />
     <Route exact path="/elctrnTaxBill/list" component={ElctrnTaxBill} />

     <Route exact path="/400" component={Error400} />
     <Route exact path="/401" component={Error401} />
     <Route exact path="/403" component={Error403} />
     <Route exact path="/404" component={Error404} />
     <Route exact path="/500" component={Error500} />
     <Route exact path="/503" component={Error503} />
     <Route component={NotFound} />
    </Switch>
    <BaseContainer />
   </>
  );
 }
}

export default withRouter((props) => <App {...props} />);
