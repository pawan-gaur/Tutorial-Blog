import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {DirectiveComponent} from './directive/directive.component';
import {CustomersComponent} from './customers/customers.component';
import {FormComponent} from './customers/form.component';
import { PaginatorComponent } from './paginator/paginator.component';
import {CustomerService} from './customers/customer.service';
import {RouterModule, Routes} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {registerLocaleData} from '@angular/common';
import localeES from '@angular/common/locales/es';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatDatepickerModule} from '@angular/material';
import {MatMomentDateModule} from '@angular/material-moment-adapter';

registerLocaleData(localeES, 'es');

const routes: Routes = [
  {path: '', redirectTo: '/customers', pathMatch: 'full'},
  {path: 'directive', component: DirectiveComponent},
  {path: 'customers', component: CustomersComponent},
  {path: 'customers/page/:page', component: CustomersComponent},
  {path: 'customers/form', component: FormComponent},
  {path: 'customers/form/:id', component: FormComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    DirectiveComponent,
    CustomersComponent,
    FormComponent,
    PaginatorComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(routes),
    BrowserAnimationsModule,
    MatDatepickerModule,
    MatMomentDateModule
  ],
  providers: [CustomerService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
