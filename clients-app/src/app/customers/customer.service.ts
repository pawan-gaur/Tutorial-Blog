import {Injectable} from '@angular/core';
import {formatDate, DatePipe} from '@angular/common';
import {CUSTOMERS} from './customer.json';
import {Customer} from './customer';
import {of, Observable, throwError} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {map, catchError, tap} from 'rxjs/operators';
import swal from 'sweetalert2';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private urlEndPoint: string = 'http://localhost:8080/api/customers';
  private httpHeaders = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private router: Router) {
  }

  getCustomers(page: number): Observable<any> {
    // return of(CUSTOMERS);
    return this.http.get<Customer[]>(this.urlEndPoint + '/page/' + page).pipe(
      tap((response: any) => {
        console.log('CustomerService: tap 1');
        (response.content as Customer[]).forEach(customer => {
          console.log(customer.firstName);
        });
      }),
      map((response: any) => {
        (response.content as Customer[]).map(customer => {
          customer.firstName = customer.firstName.toUpperCase();
          // const datePipe = new DatePipe('es');
          // customer.createAt = datePipe.transform(customer.createAt, 'EEEE dd, MMMM yyyy');
          // customer.createAt = formatDate(customer.createAt, 'dd-MM-yyyy', 'en-US');
          return customer;
        });
        return response;
      }),
      tap(response => {
        console.log('CustomerService: tap 2');
        (response.content as Customer[]).forEach(customer => {
          console.log(customer.firstName);
        });
      })
    );
  }

  create(customer: Customer): Observable<Customer> {
    return this.http.post<any>(this.urlEndPoint, customer, {headers: this.httpHeaders}).pipe(
      map((response: any) => response.customer as Customer),
      catchError(e => {

        if (e.status === 400) {
          return throwError(e);
        }
        console.error(e.error.message);
        swal.fire('error in creating customer', e.error.message, 'error');
        return throwError(e);
      })
    );
  }

  getCustomer(id): Observable<Customer> {
    return this.http.get<Customer>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/customers']);
        console.error(e.error.message);
        swal.fire('error in editing', e.error.message, 'error');
        return throwError(e);
      })
    );
  }

  updateCustomer(customer: Customer): Observable<any> {
    return this.http.put<any>(`${this.urlEndPoint}/${customer.id}`, customer, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        if (e.status === 400) {
          return throwError(e);
        }
        console.error(e.error.message);
        swal.fire('error in updating customer', e.error.message, 'error');
        return throwError(e);
      })
    );
  }

  delete(id: number): Observable<Customer> {
    return this.http.delete<Customer>(`${this.urlEndPoint}/${id}`, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        this.router.navigate(['/customers']);
        console.error(e.error.message);
        swal.fire('error in removing customer', e.error.message, 'error');
        return throwError(e);
      })
    );
  }
}
