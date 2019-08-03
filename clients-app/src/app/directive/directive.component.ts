import {Component} from '@angular/core';

@Component({
  selector: 'app-directive',
  templateUrl: './directive.component.html',
})
export class DirectiveComponent {

  listCourse: string[] = ['TypeScript', 'Java', 'Java SE', 'Spring', 'Hibernate'];

  enable: boolean = true;

  constructor() {
  }

  setEnable(): void {
    this.enable = (this.enable === true) ? false : true;
  }

}
