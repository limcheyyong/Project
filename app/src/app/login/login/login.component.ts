import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { UserService } from 'src/app/_util/_service/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { first } from 'rxjs/operators';
import { DOCUMENT } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private authenticationService: UserService,
    private fb: FormBuilder,
    @Inject(DOCUMENT) private _document

  ) {
    if (this.authenticationService.userValue) {
      this.router.navigate(['/']);
    }
  }
  ngOnInit(): void {
    this._document.body.classList.add('bodybg-color');
    this.showForm();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }
  showForm() {
    this.loginForm = this.fb.group({
      userCode: ['', Validators.required],
      password: ['', Validators.required],
      rememberMe: [true]
    })
  }
  // get username() { return this.loginForm.get('userCode'); }
  // get password() { return this.loginForm.get('password'); }
  get rememberMe() { return this.loginForm.get('rememberMe') }
  onSubmit() {
    this.submitted = true;

    // reset alerts on submit
    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }
    this.loading = true;
    console.log("sdsd");
    this.userService.login(this.loginForm.value).pipe(first()).subscribe(res => {
      if (res.result) {
        this.router.navigate([this.returnUrl]);
        // window.location.reload();
      } else {

        alert(res.message);
        this.loading = false;
      }
    }, error => {
      this.loading = false;
    })
  }
  ngOnDestroy() {
    // remove the class form body tag
    this._document.body.classList.add('bodybg-color');
  }
}
