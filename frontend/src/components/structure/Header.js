import React from "react";
import "./Header.css";
import { Link } from "react-router-dom";
import { MdLock } from "react-icons/md";

const Header = ({ onLogout }) => {
 const userNm = localStorage.getItem("userInfo")
  ? JSON.parse(localStorage.getItem("userInfo")).userNm
  : null;
 return (
  <nav className="navbar navbar-expand navbar-light bg-white topbar static-top shadow">
   <button
    id="sidebarToggleTop"
    className="btn btn-link d-md-none rounded-circle mr-3"
   >
    <i className="fa fa-bars"></i>
   </button>
   {/*
    <form className="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
      <div className="input-group">
        <input
          type="text"
          className="form-control bg-light border-0 small"
          placeholder="Search for..."
          aria-label="Search"
          aria-describedby="basic-addon2"
        />
        <div className="input-group-append">
          <button className="btn btn-primary" type="button">
            <i className="fas fa-search fa-sm"></i>
          </button>
        </div>
      </div>
    </form>
*/}
   <ul className="navbar-nav ml-auto">
    <li className="nav-item dropdown no-arrow d-sm-none">
     <a
      className="nav-link dropdown-toggle"
      href="#"
      id="searchDropdown"
      role="button"
      data-toggle="dropdown"
      aria-haspopup="true"
      aria-expanded="false"
     >
      <i className="fas fa-search fa-fw"></i>
     </a>

     <div
      className="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in"
      aria-labelledby="searchDropdown"
     >
      <form className="form-inline mr-auto w-100 navbar-search">
       <div className="input-group">
        <input
         type="text"
         className="form-control bg-light border-0 small"
         placeholder="Search for..."
         aria-label="Search"
         aria-describedby="basic-addon2"
        />
        <div className="input-group-append">
         <button className="btn btn-primary" type="button">
          <i className="fas fa-search fa-sm"></i>
         </button>
        </div>
       </div>
      </form>
     </div>
    </li>

    <li className="nav-item dropdown no-arrow mx-1">
     <a
      className="nav-link dropdown-toggle"
      href="#"
      id="alertsDropdown"
      role="button"
      data-toggle="dropdown"
      aria-haspopup="true"
      aria-expanded="false"
     >
      <i className="fas fa-bell fa-fw"></i>

      <span className="badge badge-danger badge-counter">3+</span>
     </a>

     <div
      className="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in"
      aria-labelledby="alertsDropdown"
     >
      <h6 className="dropdown-header">공지사항</h6>
      <a className="dropdown-item d-flex align-items-center" href="#">
       <div className="mr-3">
        <div className="icon-circle bg-primary">
         <i className="fas fa-file-alt text-white"></i>
        </div>
       </div>
       <div>
        <div className="small text-gray-500">2020.06.15</div>
        <span className="font-weight-bold">
         용차블루 운송사1.0이 오픈되었습니다.
        </span>
       </div>
      </a>
      <a className="dropdown-item d-flex align-items-center" href="#">
       <div className="mr-3">
        <div className="icon-circle bg-success">
         <i className="fas fa-donate text-white"></i>
        </div>
       </div>
       <div>
        <div className="small text-gray-500">2020.06.15</div>
        정기점검 공지
       </div>
      </a>
      <a className="dropdown-item d-flex align-items-center" href="#">
       <div className="mr-3">
        <div className="icon-circle bg-warning">
         <i className="fas fa-exclamation-triangle text-white"></i>
        </div>
       </div>
       <div>
        <div className="small text-gray-500">2020.06.15</div>
        서비스 내역 공지
       </div>
      </a>
      <a className="dropdown-item text-center small text-gray-500" href="#">
       전체 공지사항 보기
      </a>
     </div>
    </li>
    {/*
      <li className="nav-item dropdown no-arrow mx-1">
        <a
          className="nav-link dropdown-toggle"
          href="#"
          id="messagesDropdown"
          role="button"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
        >
          <i className="fas fa-envelope fa-fw"></i>

          <span className="badge badge-danger badge-counter">7</span>
        </a>

        <div
          className="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in"
          aria-labelledby="messagesDropdown"
        >
          <h6 className="dropdown-header">Message Center</h6>
          <a className="dropdown-item d-flex align-items-center" href="#">
            <div className="dropdown-list-image mr-3">
              <img
                className="rounded-circle"
                src="https://source.unsplash.com/fn_BT9fwg_E/60x60"
                alt=""
              />
              <div className="status-indicator bg-success"></div>
            </div>
            <div className="font-weight-bold">
              <div className="text-truncate">
                Hi there! I am wondering if you can help me with a problem I've
                been having.
              </div>
              <div className="small text-gray-500">Emily Fowler · 58m</div>
            </div>
          </a>
          <a className="dropdown-item d-flex align-items-center" href="#">
            <div className="dropdown-list-image mr-3">
              <img
                className="rounded-circle"
                src="https://source.unsplash.com/AU4VPcFN4LE/60x60"
                alt=""
              />
              <div className="status-indicator"></div>
            </div>
            <div>
              <div className="text-truncate">
                I have the photos that you ordered last month, how would you
                like them sent to you?
              </div>
              <div className="small text-gray-500">Jae Chun · 1d</div>
            </div>
          </a>
          <a className="dropdown-item d-flex align-items-center" href="#">
            <div className="dropdown-list-image mr-3">
              <img
                className="rounded-circle"
                src="https://source.unsplash.com/CS2uCrpNzJY/60x60"
                alt=""
              />
              <div className="status-indicator bg-warning"></div>
            </div>
            <div>
              <div className="text-truncate">
                Last month's report looks great, I am very happy with the
                progress so far, keep up the good work!
              </div>
              <div className="small text-gray-500">Morgan Alvarez · 2d</div>
            </div>
          </a>
          <a className="dropdown-item d-flex align-items-center" href="#">
            <div className="dropdown-list-image mr-3">
              <img
                className="rounded-circle"
                src="https://source.unsplash.com/Mv9hjnEUHR4/60x60"
                alt=""
              />
              <div className="status-indicator bg-success"></div>
            </div>
            <div>
              <div className="text-truncate">
                Am I a good boy? The reason I ask is because someone told me
                that people say this to all dogs, even if they aren't good...
              </div>
              <div className="small text-gray-500">Chicken the Dog · 2w</div>
            </div>
          </a>
          <a className="dropdown-item text-center small text-gray-500" href="#">
            Read More Messages
          </a>
        </div>
      </li>
*/}
    <div className="topbar-divider d-none d-sm-block"></div>

    <li className="nav-item dropdown no-arrow">
     <a
      className="nav-link dropdown-toggle"
      href="#"
      id="userDropdown"
      role="button"
      data-toggle="dropdown"
      aria-haspopup="true"
      aria-expanded="false"
     >
      <span className="mr-2 d-none d-lg-inline text-gray-600 small">
       {userNm}
      </span>
      <i className="fa fa-cog" aria-hidden="true"></i>
     </a>

     <div
      className="dropdown-menu dropdown-menu-right shadow animated--grow-in"
      aria-labelledby="userDropdown"
     >
      <a className="dropdown-item" href="#">
       <i className="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
       개인정보
      </a>
      <a className="dropdown-item" href="#">
       <i className="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
       설정
      </a>
      <a className="dropdown-item" href="#">
       <i className="fas fa-list fa-sm fa-fw mr-2 text-gray-400"></i>
       활동 로그
      </a>
      <div className="dropdown-divider"></div>
      <a
       className="dropdown-item"
       href="#"
       data-toggle="modal"
       data-target="#logoutModal"
      >
       <i className="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
       로그아웃
      </a>
     </div>
    </li>
   </ul>

   <div
    className="modal fade show"
    id="logoutModal"
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
        정말로 로그인하시겠습니까?
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
       <button className="btn btn-secondary" type="button" data-dismiss="modal">
        취소
       </button>
       <a className="btn btn-primary" onClick={onLogout}>
        로그아웃
       </a>
      </div>
     </div>
    </div>
   </div>
  </nav>
 );
};
export default Header;
