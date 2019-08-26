import {Component, Input, OnInit, OnChanges, SimpleChanges} from '@angular/core';

@Component({
  selector: 'paginator-nav',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.css']
})
export class PaginatorComponent implements OnInit, OnChanges {

  @Input() pager: any;
  pages: number[];

  since: number;
  until: number;

  constructor() {
  }

  ngOnInit() {
    this.initPaginator();
  }

  ngOnChanges(changes: SimpleChanges) {
    let updatedPager = changes['pager'];

    if (updatedPager.previousValue) {
      this.initPaginator();
    }

  }

  private initPaginator(): void {
    this.since = Math.min(Math.max(1, this.pager.number - 4), this.pager.totalPages - 5);
    this.until = Math.max(Math.min(this.pager.totalPages, this.pager.number + 4), 6);
    if (this.pager.totalPages > 5) {
      this.pages = new Array(this.until - this.since + 1).fill(0).map((_value, index) => index + this.since);
    } else {
      this.pages = new Array(this.pager.totalPages).fill(0).map((_value, index) => index + 1);
    }
  }

}
