import {Component, EventEmitter, Input, numberAttribute, Output} from '@angular/core';
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.scss'
})
export class PaginationComponent {
  @Input() page!: number;
  @Input() size!: number;
  @Input() pages: any = [];
  @Input({transform: numberAttribute}) totalPages!: number;
  @Output() pageChanged = new EventEmitter<number>();

  goToFirstPage() {
    this.page = 0;
    this.pageChanged.emit(this.page)
  }

  goToPreviousPage() {
    this.page--;
    this.pageChanged.emit(this.page)
  }

  goToPage(page: number) {
    this.page = page
    this.pageChanged.emit(this.page)
  }

  goToNextPage() {
    this.page++;
    this.pageChanged.emit(this.page)
  }

  goToLastPage() {
    this.page = this.lastPage
    this.pageChanged.emit(this.page)
  }

  get isLastPage(): boolean {
    return this.page === this.lastPage
  }

  get lastPage(): number {
    return this.totalPages as number - 1
  }

  static getPages(totalPages: number) {
    return Array(totalPages)
      .fill(0)
      .map((x, i) => i);
  }
}
