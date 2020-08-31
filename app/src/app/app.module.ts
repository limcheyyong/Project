import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ButtonComponent } from './button/button.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { LoginComponent } from './login/login/login.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AuthorityManageComponent } from './AuthorityMenu/authority-manage/authority-manage.component';
import { DataTablesModule } from 'angular-datatables';
import { JwtInterceptor } from './_util/_helpers/jwt.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    ButtonComponent,
    DashboardComponent,
    LoginComponent,
    AuthorityManageComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    DataTablesModule

  ],
  providers: [

    { provide: LocationStrategy, useClass: HashLocationStrategy },
    // { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
