import './App.css';
import React from "react";
import { BrowserRouter, Route, Routes} from "react-router-dom";
import {createBrowserHistory} from "history";

import NavigationBar from "./components/NavigationBarClass";
import Home from "./components/Home";

export default function App() {
  return (
      <div className="App">
          <BrowserRouter>
              <NavigationBar/>
              <div className="container-fluid">
                  <Routes>
                      <Route path="home" element={<Home/>}/>
                  </Routes>
              </div>
          </BrowserRouter>
      </div>
  );
}