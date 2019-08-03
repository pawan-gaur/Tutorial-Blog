import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Welcome to angular';
  course: string = 'Spring course 5 with angular 7';
  creater: string = 'Pawan Gaur';
}
