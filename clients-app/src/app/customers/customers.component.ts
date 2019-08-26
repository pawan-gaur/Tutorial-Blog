import {Component, OnInit} from '@angular/core';
import {Customer} from './customer';
import {CustomerService} from './customer.service';
import swal from 'sweetalert2';
import {tap} from 'rxjs/operators';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html'
})
export class CustomersComponent implements OnInit {

  customers: Customer[];
  pager: any;

  constructor(private customerService: CustomerService, private activatedRoute: ActivatedRoute) {

  }

  ngOnInit() {

    this.activatedRoute.paramMap.subscribe(params => {
      let page: number = +params.get('page');

      if (!page) {
        page = 0;
      }

      this.customerService.getCustomers(page).pipe(
        tap(response => {
          console.log('CustomersComponent: tap 3');
          (response.content as Customer[]).forEach(customer => {
            console.log(customer.firstName);
          });
        })
      ).subscribe(response => {
        this.customers = response.content as Customer[];
        this.pager = response;
      });
    });
  }

  delete(customer: Customer): void {
    swal.fire({
      title: 'Are you sure?',
      text: `Are you sure you want to delete the customer ${customer.firstName} ${customer.lastName}?`,
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel!',
      confirmButtonClass: 'btn btn-success',
      cancelButtonClass: 'btn btn-danger',
      buttonsStyling: false,
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.customerService.delete(customer.id).subscribe(
          response => {
            this.customers = this.customers.filter(cli => cli !== customer)
            swal.fire(
              'Customer Removed!',
              `Customer ${customer.firstName} successfully deleted.`,
              'success'
            );
          }
        );
      }
    });
  }

}
