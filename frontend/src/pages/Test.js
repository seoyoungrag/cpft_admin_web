// @flow

import * as React from "react";
import axios from "axios";

import logo from "../img/logo_indigo_teamfresh.png";

type Props = {||};

class Test extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      id: "",
      password: "",
    };
  }
  singin = (event) => {
    event.preventDefault();
    const params = new URLSearchParams();
    params.append("id", this.state.id);
    params.append("password", this.state.password);

    axios.post("/v1/signin", params).then((res) => {
      console.log(res);
      console.log(res.data);
      window.location = "/";
    });
  };
  render() {
    return (
      <body className="login-body">
        <div
          className="container col-3 login-container"
          style={{ "margin-top": "10rem" }}
        >
          <img
            src={logo}
            className="rounded mx-auto d-block"
            alt="..."
            style={{ width: "109px" }}
          />
          <div className="row">
            <div className="col-sm">
              <form className="login-form" onSubmit={this.singin}>
                <p className="login-p">로그인 해주세요.</p>
                <div className="form-group">
                  <label for="exampleInputEmail1">ID</label>
                  <input
                    type="email"
                    className="form-control"
                    id="exampleInputEmail1"
                    aria-describedby="emailHelp"
                    onChange={(event) => {
                      this.setState({
                        id: event.target.value,
                      });
                    }}
                  />
                </div>
                <div className="form-group">
                  <label for="exampleInputPassword1">Password</label>
                  <input
                    type="password"
                    className="form-control"
                    id="exampleInputPassword1"
                    onChange={(event) => {
                      this.setState({
                        password: event.target.value,
                      });
                    }}
                  />
                </div>
                <div className="form-group form-check">
                  <a href="#">아이디 / 비밀번호 찾기</a>
                </div>
                <button type="submit" className="btn btn-primary">
                  Login
                </button>
              </form>
            </div>
          </div>
        </div>
      </body>
    );
  }
}

export default Test;
