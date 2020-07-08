import React from "react";
import App from "./components/App";
import { Provider } from "react-redux";
import store from "./store/configure";
import { BrowserRouter as Router } from "react-router-dom";
import ReactStore from "ReactStore";
import { Component } from "react";

class Root extends Component {
 constructor(props) {
  super(props);
  this._initButton = () => {
   this.state.buttonIdsArray.forEach((button) => {
    document.getElementById(button).classList.remove("active");
   });
  };
  this._handleClick = (id) => {
   var isClicked = false;
   if (document.getElementById(id).classList.contains("active")) {
    isClicked = true;
   }
   this._initButton();
   if (isClicked) {
    document.getElementById(id).classList.remove("active");
    this.setState({
     clickedButton: "",
    });
    localStorage.setItem("clickedButton", "");
   } else {
    document.getElementById(id).classList.add("active");
    this.setState({
     clickedButton: id,
    });
    localStorage.setItem("clickedButton", id);
   }

   this.setState({ active: !this.state.active });
  };
  this._initNav = () => {
   var isInitNav = true;
   this.state.buttonIdsArray.forEach((button) => {
    if (!document.getElementById(button)) {
     isInitNav = false;
    }
   });
   if (!isInitNav) {
    return false;
   }

   this._initButton();

   this.state.buttonIdsArray.forEach((button) => {
    document.getElementById(button).classList.remove("active");
    var collapseDiv = document.getElementById(button).querySelector("a").dataset
     .target;
    if (this.state.clickedButton == button) {
     document.getElementById(button).classList.add("active");
     document.querySelector(collapseDiv).classList.add("show");
    } else {
     document.getElementById(button).classList.remove("active");
     document.querySelector(collapseDiv).classList.remove("show");
    }
   });
   var aTags = document.querySelectorAll("a.collapse-item");
   for (var i = 0; i < aTags.length; i++) {
    if (aTags[i].pathname == location.pathname) {
     aTags[i].classList.add("active");
    } else {
     aTags[i].classList.remove("active");
    }
   }
  };
  this.state = {
   active: true,
   buttonIdsArray: ["button1", "button2", "button3"],
   clickedButton: localStorage.getItem("clickedButton") || "",
   initButton: this._initButton,
   handleClick: this._handleClick,
   initNav: this._initNav,
  };
 }

 componentDidMount = () => {
  this._initNav();
 };
 componentDidUpdate = () => {
  /*
    first = location.pathname;
    first.indexOf(1);
    first.toLowerCase();
    first = first.split("/")[1];
    */
 };

 render() {
  return (
   <Provider store={store}>
    <ReactStore.Provider value={this.state}>
     <React.StrictMode>
      <Router>
       <App initNav={this._initNav} />
      </Router>
     </React.StrictMode>
    </ReactStore.Provider>
   </Provider>
  );
 }
}
export default Root;
