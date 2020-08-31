import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl } from '@angular/forms';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.css']
})
export class ButtonComponent implements OnInit {
  skills = new FormArray([]);
  constructor(
  ) {
  }
  addSkill() {
    this.skills.push(new FormControl(''));
  }
  removeSkill(i){
    this.skills.removeAt(i)
  }
  replace() {
    this.skills.setControl(0, new FormControl(''));
  }
  ngOnInit(): void {
  }
  
}
