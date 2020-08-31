import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { UserService } from './_util/_service/user.service';
import { Router } from '@angular/router';
import { User } from './_util/_models/user';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  user: any;
  title: string;
  constructor(
    private userService: UserService
  ) {

  }
  // 側邊目錄寫這裡
  menu = [
    { key: 1, menu: '後台管理', icon: "menu-icon fa fa-pencil-square-o" },
    { key: 3, menu: 'Button', icon: "menu-icon fa fa-pencil-square-o" },
    { key: 2, menu: 'Form', icon: "menu-icon fa fa-pencil-square-o" },

  ]
  submenu = [
    { menu: 3, submenu: 'button 1', location: "button" },
    { menu: 2, submenu: 'button 2', location: "Dashboard" },
    { menu: 2, submenu: 'button 3', location: "button" },
    { menu: 3, submenu: 'button 4', location: "Dashboard" },
    { menu: 1, submenu: '帳號維護', location: "AuthorityManage" }
  ]
  ngOnInit(): void {

    // 塞入登入的值
    this.userService.user.subscribe(x => this.user = x);
    console.log(this.user)
  }
  onRouter(i) {
    console.log(i);
  }
  logout() {
    this.userService.logout();

  }
  test(e) {
    this.title = this.submenu[e].submenu;
    console.log(this.submenu[e].submenu);
  }
}
