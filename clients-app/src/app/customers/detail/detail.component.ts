import { Component, OnInit } from '@angular/core';
import { Customer } from '../customer';
import { CustomerService } from '../customer.service';
import {ActivatedRoute} from '@angular/router';
import swal from 'sweetalert2';

@Component({
  selector: 'cutomer-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {
  customer: Customer;
  title: string ="Customer Photo Upload";
  private selectedPhoto: File;

  constructor(private customerService: CustomerService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params =>{
      let id:number = +params.get('id');
      if(id){
        this.customerService.getCustomer(id).subscribe(customer => {
          this.customer = customer;
        });
      }
    });
  }

  photoSelection(event){
    this.selectedPhoto = event.target.files[0];
    console.log(this.selectedPhoto);
  }

  uploadPhoto(){
    this.customerService.uploadPhoto(this.selectedPhoto, this.customer.id)
    .subscribe(customer => {
      this.customer = customer;
      swal.fire('The photo has been uploaded completely!', `The photo has been successfully uploaded: ${this.customer.photo}`, 'success');
    });
  } // 8th Completed

}
