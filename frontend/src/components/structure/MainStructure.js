import React from "react";
import "./MainStructure.css";
import HeaderContainer from "containers/HeaderContainer";
import { Collapse } from "reactstrap";
import $ from "jquery";
import { Component } from "react";
import ReactStore from "ReactStore";
import { Link } from "react-router-dom";
import SideNav from "components/structure/SideNav";

class MainStructure extends Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {}

  render() {
    return (
      <div id="wrapper">
        <SideNav></SideNav>

        <div id="content-wrapper" className="d-flex flex-column">
          <div id="content">
            <HeaderContainer />
            {this.props.children}
          </div>
        </div>
      </div>
    );
  }
}

export default MainStructure;
