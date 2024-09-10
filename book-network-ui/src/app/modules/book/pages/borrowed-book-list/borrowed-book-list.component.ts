import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {NgForOf, NgIf} from "@angular/common";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-borrowed-book-list',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit {
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  page = 0;
  size = 5;
  pages: any=  [];

  constructor(private bookService: BookService, private router: Router) {
  }

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    console.log("returnBorrowedBook", book);
  }

  private findAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({page: this.page, size: this.size})
      .subscribe({
        next: result => {
          this.borrowedBooks = result
          this.pages = Array(this.borrowedBooks.totalPages)
            .fill(0)
            .map((x, i) => i)
        }
      })
  }

  //Pagination Start
  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks()
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks()
  }

  goToPage(page: number) {
    this.page = page
    this.findAllBorrowedBooks()
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks()
  }

  goToLastPage() {
    this.page = this.lastPage
    this.findAllBorrowedBooks()
  }

  get isLastPage(): boolean {
    return this.page === this.lastPage
  }

  get lastPage(): number {
    return this.borrowedBooks.totalPages as number - 1
  }

  //Pagination End

}
