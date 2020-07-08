// @flow

import React from "react";
import ReactDOM from "react-dom";
import "./include/bootstrap";

//import "./index.css";
//import "./c3jscustom.css";

//import App from "./components/App";
import Root from "./Root";
//import App from "./pages/Test.js";

//import 'bootstrap/dist/css/bootstrap.css'

//import "./styles/bootstrap.min.css";
import "./styles/font-awesome.min.css";
import "./styles/fonts.css";
import "./styles/sb-admin-2.css";
import "./styles/style.css";
import "./vendor/fontawesome-free/css/all.css";
//import "./styles/utils.css";
import ReactStore from "ReactStore";

const rootElement = document.getElementById("root");

if (rootElement) {
  //ReactDOM.render(<Root />, rootElement);
  ReactDOM.render(<Root />, rootElement);
} else {
  throw new Error("Could not find root element to mount to!");
}
