import {Component, OnInit} from '@angular/core';
import {Customer} from './customer';
import {CustomerService} from './customer.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html'
})
export class CustomersComponent implements OnInit {

  customers: Customer[];

  constructor(private customerService: CustomerService) {

  }

  ngOnInit() {
    this.customerService.getCustomers().subscribe(
      (customers) => {
        this.customers = customers;
      }
    );
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
            this.customers = this.customers.filter( cli => cli !== customer )
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
