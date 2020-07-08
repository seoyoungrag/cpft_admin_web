import React from "react";
import $ from "jquery";
import { Component } from "react";
import { Link } from "react-router-dom";
import ReactStore from "ReactStore";

class SideNav extends Component {
 constructor(props) {
  super(props);
 }

 componentDidMount() {
  // Toggle the side navigation
  $("#sidebarToggle, #sidebarToggleTop").on("click", function (e) {
   $("body").toggleClass("sidebar-toggled");
   $(".sidebar").toggleClass("toggled");
   if ($(".sidebar").hasClass("toggled")) {
    $(".sidebar .collapse").collapse("hide");
   }
  });

  // Close any open menu accordions when window is resized below 768px
  $(window).resize(function () {
   if ($(window).width() < 768) {
    $(".sidebar .collapse").collapse("hide");
   }
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  $("body.fixed-nav .sidebar").on("mousewheel DOMMouseScroll wheel", function (
   e
  ) {
   if ($(window).width() > 768) {
    var e0 = e.originalEvent,
     delta = e0.wheelDelta || -e0.detail;
    this.scrollTop += (delta < 0 ? 1 : -1) * 30;
    e.preventDefault();
   }
  });

  // Scroll to top button appear
  $(document).on("scroll", function () {
   var scrollDistance = $(this).scrollTop();
   if (scrollDistance > 100) {
    $(".scroll-to-top").fadeIn();
   } else {
    $(".scroll-to-top").fadeOut();
   }
  });

  // Smooth scrolling using jQuery easing
  $(document).on("click", "a.scroll-to-top", function (e) {
   var $anchor = $(this);
   $("html, body")
    .stop()
    .animate(
     {
      scrollTop: $($anchor.attr("href")).offset().top,
     },
     1000,
     "easeInOutExpo"
    );
   e.preventDefault();
  });
 }
 //}= ({ children, props }) => {

 render() {
  return (
   <ReactStore.Consumer>
    {(store) => (
     <ul
      className="navbar-nav bg-gradient-light sidebar sidebar-dark accordion"
      id="accordionSidebar"
     >
      <a
       className="sidebar-brand d-flex align-items-center justify-content-center"
       href="/"
      >
       <div className="sidebar-brand-icon rotate-n-15">
        <i className="fas fa-truck-moving"></i>
       </div>
       <div className="sidebar-brand-text mx-1">
        용차블루 <sup>운송사1.0</sup>
       </div>
      </a>

      <hr className="sidebar-divider my-0" />

      <li className="nav-item" id="button1">
       <a
        className="nav-link collapsed"
        href="#"
        data-toggle="collapse"
        data-target="#collapseOne"
        aria-expanded="true"
        aria-controls="collapseOne"
        onClick={() => store.handleClick("button1")}
       >
        <i className="fas fa-fw fa-address-card"></i>
        <span>오더, 지원자 관리</span>
       </a>
       <div
        id="collapseOne"
        className="collapse"
        aria-labelledby="headingOne"
        data-parent="#accordionSidebar"
       >
        <div className="bg-white py-2 collapse-inner rounded">
         <h6 className="collapse-header">오더, 지원자 관리</h6>
         <div className="forMobileMenu">
          <Link to="/order/regist" className="collapse-item">
           오더 등록
          </Link>
          <Link to="/order/manage" className="collapse-item">
           오더 관리
          </Link>
          <Link to="/order/applicant/manage" className="collapse-item">
           지원자 관리
          </Link>
          {/*
         <Link to="/order/black/list" className="collapse-item">
          차단 내역
         </Link>
          */}
         </div>
        </div>
       </div>
      </li>
      <li className="nav-item" id="button2">
       <a
        className="nav-link collapsed"
        href="#"
        data-toggle="collapse"
        data-target="#collapseTwo"
        aria-expanded="true"
        aria-controls="collapseTwo"
        onClick={() => store.handleClick("button2")}
       >
        <i className="fas fa-fw fa-truck"></i>
        <span>근무 차주관리</span>
       </a>
       <div
        id="collapseTwo"
        className="collapse"
        aria-labelledby="headingTwo"
        data-parent="#accordionSidebar"
       >
        <div className="bg-white py-2 collapse-inner rounded">
         <h6 className="collapse-header">근무 차주 관리</h6>
         <div className="forMobileMenu">
          <Link to="/truckOwner/list" className="collapse-item">
           전체 차주 리스트
          </Link>
          {/*
         <a className="collapse-item">차주 근태(수기)</a>
         <a className="collapse-item">차주 정산(수기)</a>

         <a className="collapse-item">차주 정산 완료 내역</a>
          */}
          <Link to="/dtStmn/list" className="collapse-item">
           명세서
          </Link>
         </div>
        </div>
       </div>
      </li>

      <li className="nav-item" id="button3">
       <a
        className="nav-link collapsed"
        href="#"
        data-toggle="collapse"
        data-target="#collapseUtilities"
        aria-expanded="true"
        aria-controls="collapseUtilities"
        onClick={() => store.handleClick("button3")}
       >
        <i className="fas fa-fw fa-krw"></i>
        <span>정산 관리</span>
       </a>
       <div
        id="collapseUtilities"
        className="collapse"
        aria-labelledby="headingUtilities"
        data-parent="#accordionSidebar"
       >
        <div className="bg-white py-2 collapse-inner rounded">
         <h6 className="collapse-header">전자세금계산서</h6>

         <div className="forMobileMenu">
          <Link className="collapse-item" to="/elctrnTaxBill/list">
           전자세금계산서
          </Link>
         </div>
        </div>
       </div>
      </li>

      <hr className="sidebar-divider d-none d-md-block" />

      <div className="text-center d-none d-md-inline">
       <button className="rounded-circle border-0" id="sidebarToggle"></button>
      </div>
     </ul>
    )}
   </ReactStore.Consumer>
  );
 }
}

export default SideNav;
