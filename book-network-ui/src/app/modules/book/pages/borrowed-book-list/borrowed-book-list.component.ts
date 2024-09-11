import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {NgForOf, NgIf} from "@angular/common";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {PaginationComponent} from "../../components/pagination/pagination.component";

@Component({
  selector: 'app-borrowed-book-list',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    PaginationComponent
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
    this.findAllBorrowedBooks(this.page);
  }

  onPageChanged(page: number) {
    this.findAllBorrowedBooks(page)
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    console.log("returnBorrowedBook", book);
  }

  private findAllBorrowedBooks(page:number) {
    this.bookService.findAllBorrowedBooks({page: page, size: this.size})
      .subscribe({
        next: result => {
          this.borrowedBooks = result
          this.pages = PaginationComponent
            .getPages(this.borrowedBooks.totalPages as number)
        }
      })
  }



}
