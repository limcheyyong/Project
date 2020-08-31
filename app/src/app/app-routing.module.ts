import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ButtonComponent } from './button/button.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { LoginComponent } from './login/login/login.component';
import { AuthGuard } from './_util/_service/auth.guard';
import { AuthorityManageComponent } from './AuthorityMenu/authority-manage/authority-manage.component';


const routes: Routes = [
  { path: '', redirectTo: '/AuthorityManage', pathMatch: 'full', canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'Dashboard', component: DashboardComponent},
  { path: 'button', component: ButtonComponent},
  { path: 'AuthorityManage', component: AuthorityManageComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    useHash: true
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
