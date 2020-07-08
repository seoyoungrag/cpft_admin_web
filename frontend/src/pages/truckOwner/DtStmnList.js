import React from "react";
import MainStructure from "components/structure/MainStructure";

import $ from "jquery";
$.DataTable = require("datatables.net");
import "datatables.net-dt";
import { Component } from "react";

import exampleImg from "img/spec_example1.jpg";
const columns = [
 {
  title: "Name",
  width: 120,
  data: "name",
 },
 {
  title: "Nickname",
  width: 180,
  data: "nickname",
 },
];

class DtStmnList extends Component {
 constructor(props) {
  super(props);
  this.state = {
   names: [],
  };
 }
 componentDidMount() {
  attachJiraIssueColletor();
  $(this.refs.main).DataTable({
   dom: '<"data-table-wrapper"t>',
   data: this.props.names,
   columns,
   ordering: false,
  });
  // Activate Bootstrap scrollspy for the sticky nav component
  $("body").scrollspy({
   target: "#stickyNav",
   offset: 0,
  });
  // Scrolls to an offset anchor when a sticky nav link is clicked
  $('.nav-sticky a.nav-link[href*="#"]:not([href="#"])').click(function () {
   if (
    location.pathname.replace(/^\//, "") == this.pathname.replace(/^\//, "") &&
    location.hostname == this.hostname
   ) {
    var target = $(this.hash);
    target = target.length ? target : $("[name=" + this.hash.slice(1) + "]");
    if (target.length) {
     $("html, body").animate(
      {
       scrollTop: target.offset().top - 81,
      },
      200
     );
     return false;
    }
   }
  });
 }
 componentWillUnmount() {
  $(".data-table-wrapper").find("table").DataTable().destroy(true);
 }
 reloadTableData = (names) => {
  const table = $(".data-table-wrapper").find("table").DataTable();
  table.clear();
  table.rows.add(names);
  table.draw();
 };

 updateTable = (names) => {
  const table = $(".data-table-wrapper").find("table").DataTable();
  let dataChanged = false;
  table.rows().every(function () {
   const oldNameData = this.data();
   const newNameData = names.find((nameData) => {
    return nameData.name === oldNameData.name;
   });
   if (oldNameData.nickname !== newNameData.nickname) {
    dataChanged = true;
    this.data(newNameData);
   }
   return true; // RCA esLint configuration wants us to
   // return something
  });

  if (dataChanged) {
   table.draw();
  }
 };

 shouldComponentUpdate(nextProps, nextState) {
  console.log(this.state.names);
  console.log(nextState.names);
  if (nextState.names.length !== this.state.names.length) {
   this.reloadTableData(nextState.names);
  } else {
   this.updateTable(nextState.names);
  }
  return false;
 }

 onAddClick(name, nickname) {
  let updated = false;
  const result = this.state.names.map((nameData) => {
   if (nameData.name === name) {
    updated = true;
    return { name, nickname };
   }
   return nameData;
  });
  if (!updated) {
   result.push({ name, nickname });
  }

  this.setState({
   names: result,
  });
 }

 render() {
  return (
   <MainStructure>
    <main>
     <div className="page-header pb-10 page-header-dark bg-gradient-primary-to-secondary">
      <div className="container-fluid">
       <div className="page-header-content">
        <h1 className="page-header-title">
         <div className="page-header-icon">
          <svg
           xmlns="http://www.w3.org/2000/svg"
           width="24"
           height="24"
           viewBox="0 0 24 24"
           fill="none"
           stroke="currentColor"
           strokeWidth="2"
           strokeLinecap="round"
           strokeLinejoin="round"
           className="feather feather-edit-3"
          >
           <path d="M12 20h9"></path>
           <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"></path>
          </svg>
         </div>
         <span>명세서</span>
        </h1>
        <div className="page-header-subtitle">
         명세서와 이의제기 내용을 확인합니다.
        </div>
       </div>
      </div>
     </div>
     <div className="container-fluid mt-n10">
      <div className="row">
       <div className="col-lg-12">
        <div className="card">
         <div className="card-header row">
          <div className="col-6">명세서</div>
          <div class="col-sm-12 col-md-6 row"></div>
         </div>
         {/*
         <div className="card-header card-header-tabs card-header-primary">
          <div className="nav-tabs-navigation">
           <div className="nav-tabs-wrapper">
            <span className="nav-tabs-title">Tasks:</span>
            <ul className="nav nav-tabs" data-tabs="tabs">
             <li className="nav-item">
              <a className="nav-link active" href="#profile" data-toggle="tab">
               <i className="fas fa-fw fa-hourglass-start"></i> 명세서
               <div className="ripple-container"></div>
              </a>
             </li>
             <li className="nav-item">
              <a className="nav-link" href="#messages" data-toggle="tab">
               <i className="fa fa-fw fa-hourglass-half"></i> 이의제기
               <div className="ripple-container"></div>
              </a>
             </li>
            </ul>
           </div>
          </div>
         </div>
*/}
         <div className="card-body">
          <div className="tab-content">
           <div className="tab-pane active" id="profile">
            <div className="datatable table-responsive">
             <div
              id="dataTable_wrapper"
              className="dataTables_wrapper dt-bootstrap4"
             >
              <div className="row">
               <div className="col-sm-12">
                <table
                 className="table table-bordered table-hover dataTable"
                 id="dataTable"
                 width="100%"
                 cellSpacing="0"
                 role="grid"
                 aria-describedby="dataTable_info"
                 style={{ width: "100%" }}
                >
                 <thead>
                  <tr role="row">
                   <th
                    className="sorting_asc"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-sort="ascending"
                    aria-label="Name: activate to sort column descending"
                    style={{ width: "15%" }}
                   >
                    운송그룹
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort-up"
                     className="svg-inline--fa fa-sort-up fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M279 224H41c-21.4 0-32.1-25.9-17-41L143 64c9.4-9.4 24.6-9.4 33.9 0l119 119c15.2 15.1 4.5 41-16.9 41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Position: activate to sort column ascending"
                    style={{ width: "15%" }}
                   >
                    담당자
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Office: activate to sort column ascending"
                    style={{ width: "15%" }}
                   >
                    차주명
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Age: activate to sort column ascending"
                    style={{ width: "15%" }}
                   >
                    연락처
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Start date: activate to sort column ascending"
                   >
                    명세서
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Salary: activate to sort column ascending"
                    style={{ width: "15%" }}
                   >
                    명세서 확인
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                  </tr>
                 </thead>
                 <tfoot>
                  <tr>
                   <th rowSpan="1" colSpan="1">
                    운송그룹
                   </th>
                   <th rowSpan="1" colSpan="1">
                    담당자
                   </th>
                   <th rowSpan="1" colSpan="1">
                    차주명
                   </th>
                   <th rowSpan="1" colSpan="1">
                    연락처
                   </th>
                   <th rowSpan="1" colSpan="1">
                    명세서
                   </th>
                   <th rowSpan="1" colSpan="1">
                    명세서 확인
                   </th>
                  </tr>
                 </tfoot>
                 <tbody>
                  <tr role="row" className="odd">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                   </td>
                   <td>
                    <div className="badge badge-info badge-pill">미확인</div>
                   </td>
                  </tr>
                  <tr role="row" className="even">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                   </td>
                   <td>
                    <div className="badge badge-info badge-pill">미확인</div>
                   </td>
                  </tr>
                  <tr role="row" className="odd">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차삼</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button
                     class="btn btn-outline-primary shadow-sm mr-2 my-1"
                     data-toggle="modal"
                     data-target="#exampleModalCenter2"
                    >
                     5월_명세서_김차삼.jpg
                     <button className="btn btn-outline-danger shadow-sm ml-3">
                      X
                     </button>
                    </button>
                   </td>
                   <td data-toggle="modal" data-target="#exampleModalCenter">
                    <div className="badge badge-danger badge-pill">재요청</div>
                   </td>
                  </tr>
                  <tr role="row" className="even">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차사</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button
                     class="btn btn-outline-primary shadow-sm mr-2 my-1"
                     data-toggle="modal"
                     data-target="#exampleModalCenter2"
                    >
                     5월_명세서_김차사.jpg
                     <button className="btn btn-outline-danger shadow-sm ml-3">
                      X
                     </button>
                    </button>
                   </td>
                   <td>
                    <div className="badge badge-success badge-pill">확인됨</div>
                   </td>
                  </tr>
                  <tr role="row" className="odd">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                   </td>
                   <td data-toggle="modal" data-target="#exampleModalCenter">
                    <div className="badge badge-info badge-pill">미확인</div>
                   </td>
                  </tr>
                  <tr role="row" className="even">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                   </td>
                   <td>
                    <div className="badge badge-info badge-pill">미확인</div>
                   </td>
                  </tr>
                  <tr role="row" className="odd">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button
                     class="btn btn-outline-primary shadow-sm mr-2 my-1"
                     data-toggle="modal"
                     data-target="#exampleModalCenter2"
                    >
                     5월_명세서_김차삼.jpg
                     <button className="btn btn-outline-danger shadow-sm ml-3">
                      X
                     </button>
                    </button>
                   </td>
                   <td data-toggle="modal" data-target="#exampleModalCenter">
                    <div className="badge badge-danger badge-pill">재요청</div>
                   </td>
                  </tr>
                  <tr role="row" className="even">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                   </td>
                   <td>
                    <div className="badge badge-info badge-pill">미확인</div>
                   </td>
                  </tr>
                  <tr role="row" className="odd">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                   </td>
                   <td>
                    <div className="badge badge-info badge-pill">미확인</div>
                   </td>
                  </tr>
                  <tr role="row" className="even">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>010-1234-5678</td>
                   <td>
                    <button
                     class="btn btn-outline-primary shadow-sm mr-2 my-1"
                     data-toggle="modal"
                     data-target="#exampleModalCenter2"
                    >
                     5월_명세서_김차삼.jpg
                     <button className="btn btn-outline-danger shadow-sm ml-3">
                      X
                     </button>
                    </button>
                   </td>
                   <td>
                    <div className="badge badge-success badge-pill">확인됨</div>
                   </td>
                  </tr>
                 </tbody>
                </table>
               </div>
              </div>
              <div className="row">
               <div className="col-sm-12 col-md-5">
                <div
                 className="dataTables_info"
                 id="dataTable_info"
                 role="status"
                 aria-live="polite"
                >
                 1 ~ 10 총 57
                </div>
               </div>
               <div className="col-sm-12 col-md-7">
                <div
                 className="dataTables_paginate paging_simple_numbers"
                 id="dataTable_paginate"
                >
                 <ul className="pagination">
                  <li
                   className="paginate_button page-item previous disabled"
                   id="dataTable_previous"
                  >
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="0"
                    tabIndex="0"
                    className="page-link"
                   >
                    Previous
                   </a>
                  </li>
                  <li className="paginate_button page-item active">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="1"
                    tabIndex="0"
                    className="page-link"
                   >
                    1
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="2"
                    tabIndex="0"
                    className="page-link"
                   >
                    2
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="3"
                    tabIndex="0"
                    className="page-link"
                   >
                    3
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="4"
                    tabIndex="0"
                    className="page-link"
                   >
                    4
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="5"
                    tabIndex="0"
                    className="page-link"
                   >
                    5
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="6"
                    tabIndex="0"
                    className="page-link"
                   >
                    6
                   </a>
                  </li>
                  <li
                   className="paginate_button page-item next"
                   id="dataTable_next"
                  >
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="7"
                    tabIndex="0"
                    className="page-link"
                   >
                    Next
                   </a>
                  </li>
                 </ul>
                </div>
               </div>
              </div>
             </div>
            </div>
           </div>
           <div className="tab-pane" id="messages">
            <div className="datatable table-responsive">
             <div
              id="dataTable_wrapper"
              className="dataTables_wrapper dt-bootstrap4"
             >
              <div className="row">
               <div className="col-sm-12">
                <table
                 className="table table-bordered table-hover dataTable"
                 id="dataTable"
                 width="100%"
                 cellSpacing="0"
                 role="grid"
                 aria-describedby="dataTable_info"
                 style={{ width: "100%" }}
                >
                 <thead>
                  <tr role="row">
                   <th
                    className="sorting_asc"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-sort="ascending"
                    aria-label="Name: activate to sort column descending"
                    style={{ width: "152px" }}
                   >
                    운송그룹
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort-up"
                     className="svg-inline--fa fa-sort-up fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M279 224H41c-21.4 0-32.1-25.9-17-41L143 64c9.4-9.4 24.6-9.4 33.9 0l119 119c15.2 15.1 4.5 41-16.9 41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Position: activate to sort column ascending"
                    style={{ width: "230px" }}
                   >
                    담당자
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Office: activate to sort column ascending"
                    style={{ width: "107px" }}
                   >
                    차주명
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Age: activate to sort column ascending"
                    style={{ width: "49px" }}
                   >
                    신청일
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Start date: activate to sort column ascending"
                    style={{ width: "100px" }}
                   >
                    사유제목
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                   <th
                    className="sorting"
                    tabIndex="0"
                    aria-controls="dataTable"
                    rowSpan="1"
                    colSpan="1"
                    aria-label="Salary: activate to sort column ascending"
                    style={{ width: "88px" }}
                   >
                    승인 상태
                    <svg
                     data-fa-pseudo-element=":after"
                     data-prefix="fas"
                     data-icon="sort"
                     className="svg-inline--fa fa-sort fa-w-10"
                     role="img"
                     xmlns="http://www.w3.org/2000/svg"
                     viewBox="0 0 320 512"
                     data-fa-i2svg=""
                    >
                     <path
                      fill="currentColor"
                      d="M41 288h238c21.4 0 32.1 25.9 17 41L177 448c-9.4 9.4-24.6 9.4-33.9 0L24 329c-15.1-15.1-4.4-41 17-41zm255-105L177 64c-9.4-9.4-24.6-9.4-33.9 0L24 183c-15.1 15.1-4.4 41 17 41h238c21.4 0 32.1-25.9 17-41z"
                     ></path>
                    </svg>
                   </th>
                  </tr>
                 </thead>
                 <tfoot>
                  <tr>
                   <th rowSpan="1" colSpan="1">
                    운송그룹
                   </th>
                   <th rowSpan="1" colSpan="1">
                    담당자
                   </th>
                   <th rowSpan="1" colSpan="1">
                    차주명
                   </th>
                   <th rowSpan="1" colSpan="1">
                    신청일
                   </th>
                   <th rowSpan="1" colSpan="1">
                    사유 제목
                   </th>
                   <th rowSpan="1" colSpan="1">
                    승인상태
                   </th>
                  </tr>
                 </tfoot>
                 <tbody>
                  <tr role="row" className="odd">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                    승인
                   </td>
                  </tr>
                  <tr role="row" className="even">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                    승인
                   </td>
                  </tr>
                  <tr role="row" className="odd">
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>
                    반려
                   </td>
                  </tr>
                  <tr
                   role="row"
                   className="even"
                   data-toggle="modal"
                   data-target="#exampleModalCenter"
                  >
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>{" "}
                    승인
                   </td>
                  </tr>
                  <tr
                   role="row"
                   className="odd"
                   data-toggle="modal"
                   data-target="#exampleModalCenter"
                  >
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>{" "}
                    반려
                   </td>
                  </tr>
                  <tr
                   role="row"
                   className="even"
                   data-toggle="modal"
                   data-target="#exampleModalCenter"
                  >
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>{" "}
                    승인
                   </td>
                  </tr>
                  <tr
                   role="row"
                   className="odd"
                   data-toggle="modal"
                   data-target="#exampleModalCenter"
                  >
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>{" "}
                    반려
                   </td>
                  </tr>
                  <tr
                   role="row"
                   className="even"
                   data-toggle="modal"
                   data-target="#exampleModalCenter"
                  >
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>{" "}
                    승인
                   </td>
                  </tr>
                  <tr
                   role="row"
                   className="odd"
                   data-toggle="modal"
                   data-target="#exampleModalCenter"
                  >
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>{" "}
                    반려
                   </td>
                  </tr>
                  <tr
                   role="row"
                   className="even"
                   data-toggle="modal"
                   data-target="#exampleModalCenter"
                  >
                   <td className="sorting_1">TS</td>
                   <td>유아름</td>
                   <td>김차일</td>
                   <td>20.06.05</td>
                   <td>사유 제목</td>
                   <td>
                    <button class="btn btn-outline-primary shadow-sm mr-2 my-1">
                     첨부파일 등록
                    </button>{" "}
                    승인
                   </td>
                  </tr>
                 </tbody>
                </table>
               </div>
              </div>
              <div className="row">
               <div className="col-sm-12 col-md-5">
                <div
                 className="dataTables_info"
                 id="dataTable_info"
                 role="status"
                 aria-live="polite"
                >
                 1 ~ 10 총 57
                </div>
               </div>
               <div className="col-sm-12 col-md-7">
                <div
                 className="dataTables_paginate paging_simple_numbers"
                 id="dataTable_paginate"
                >
                 <ul className="pagination">
                  <li
                   className="paginate_button page-item previous disabled"
                   id="dataTable_previous"
                  >
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="0"
                    tabIndex="0"
                    className="page-link"
                   >
                    Previous
                   </a>
                  </li>
                  <li className="paginate_button page-item active">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="1"
                    tabIndex="0"
                    className="page-link"
                   >
                    1
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="2"
                    tabIndex="0"
                    className="page-link"
                   >
                    2
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="3"
                    tabIndex="0"
                    className="page-link"
                   >
                    3
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="4"
                    tabIndex="0"
                    className="page-link"
                   >
                    4
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="5"
                    tabIndex="0"
                    className="page-link"
                   >
                    5
                   </a>
                  </li>
                  <li className="paginate_button page-item ">
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="6"
                    tabIndex="0"
                    className="page-link"
                   >
                    6
                   </a>
                  </li>
                  <li
                   className="paginate_button page-item next"
                   id="dataTable_next"
                  >
                   <a
                    href="#"
                    aria-controls="dataTable"
                    data-dt-idx="7"
                    tabIndex="0"
                    className="page-link"
                   >
                    Next
                   </a>
                  </li>
                 </ul>
                </div>
               </div>
              </div>
             </div>
            </div>
           </div>
          </div>
         </div>
        </div>
       </div>
      </div>
      <div
       className="modal fade"
       id="exampleModalCenter2"
       tabIndex="-1"
       role="dialog"
       aria-labelledby="exampleModalCenterTitle"
       aria-hidden="true"
      >
       <div className="modal-dialog modal-dialog-centered" role="document">
        <div className="modal-content">
         <div className="modal-header">
          <h5 className="modal-title" id="exampleModalCenterTitle">
           명세서 내역
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
         <div className="modal-body">
          <div className="card mb-4">
           <div className="card-body">
            <img src={exampleImg} alt="" className="w-100" />
           </div>
          </div>
         </div>
        </div>
       </div>
      </div>
      <div
       className="modal fade"
       id="exampleModalCenter"
       tabIndex="-1"
       role="dialog"
       aria-labelledby="exampleModalCenterTitle"
       aria-hidden="true"
      >
       <div className="modal-dialog modal-dialog-centered" role="document">
        <div className="modal-content">
         <div className="modal-header">
          <h5 className="modal-title" id="exampleModalCenterTitle">
           명세서 재요청
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
         <div className="modal-body">
          <div className="card mb-4">
           <div className="card-body">
            <h5 className="card-title text-primary row m-auto p-auto">
             <dt>차주명:</dt>&nbsp; <dd>김차일</dd>
            </h5>
            <div className="card-text row">
             <dl className="col-6 row mb-auto">
              <dt className="col-4 h-25 d-flex justify-content-end">
               운송그룹:
              </dt>{" "}
              <dd className="col-8 h-25 ">TS</dd>
              <dt className="col-4 h-25 d-flex justify-content-end">
               요청일:
              </dt>{" "}
              <dd className="col-8 h-25 ">20.06.05</dd>
             </dl>
             <dl className="col-6 row mb-auto">
              <dt className="col-5 h-25 d-flex justify-content-end">
               매니저코드:
              </dt>{" "}
              <dd className="col-7 h-25 ">TS_0002</dd>
              <dt className="col-5 h-25 d-flex justify-content-end">
               담당자:
              </dt>{" "}
              <dd className="col-7 h-25 ">유아름</dd>
             </dl>
             <dl className="col-12 m-auto pt-5">
              <dt className="mb-3">요청 사유</dt>
              <dd>
               {" "}
               무단 결근 한 적이 없는데, 무단 결근처리 되서 패널티가
               적용되었습니다 .
              </dd>
             </dl>
            </div>
           </div>
          </div>
         </div>
        </div>
       </div>
      </div>
     </div>
    </main>
   </MainStructure>
  );
 }
}

export default DtStmnList;
