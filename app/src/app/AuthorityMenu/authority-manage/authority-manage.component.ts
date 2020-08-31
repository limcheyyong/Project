import { AdminInfo } from './../../_util/_models/adminInfo';
import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { AdminService } from 'src/app/_util/_service/hr01.service';
import { Subject } from 'rxjs';
import { DataTableDirective } from 'angular-datatables';

@Component({
  selector: 'app-authority-manage',
  templateUrl: './authority-manage.component.html',
  styleUrls: ['./authority-manage.component.css']
})
export class AuthorityManageComponent implements OnInit, OnDestroy {
  @ViewChild(DataTableDirective, { static: false })
  dtElement: DataTableDirective;
  dtOptions: any = {};
  dtTrigger: Subject<any> = new Subject();
  subscribeEvent: any;
  adminInfo = {
    userName: null
  };
  adminList: AdminInfo[];
  constructor(
    public adminService: AdminService
  ) { }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    if (this.subscribeEvent) {
      this.subscribeEvent.unsubscribe();
    }
  }
  ngOnInit(): void {
    this.submit();
    this.dtOptions = {
      pagingType: 'full_numbers',
      order: [[0, 'asc']],
    };
  }
  ngAfterViewInit(): void {
    this.dtTrigger.next();
  }
  submit() {
    this.subscribeEvent = this.adminService.adminList(this.adminInfo).subscribe(data => {
      if (data.result) {
        this.adminList = data.data;
        this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          //結束datatable
          dtInstance.destroy();
          //刷新
          this.dtTrigger.next();
        });
      }
    })
  }

}
