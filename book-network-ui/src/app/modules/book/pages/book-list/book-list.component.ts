import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {NgForOf, NgIf} from "@angular/common";
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {BookResponse} from "../../../../services/models/book-response";
import {HttpErrorResponse} from "@angular/common/http";
import {PaginationComponent} from "../../components/pagination/pagination.component";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    NgForOf,
    BookCardComponent,
    NgIf,
    PaginationComponent
  ],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  pages: any = [];
  size = 5;
  message = "";
  level = "";

  constructor(private bookService: BookService, private router: Router) {

  }

  ngOnInit(): void {
    this.findAllBooks(this.page);
  }

  onPageChanged(page: number) {
    this.findAllBooks(page);
  }

  private findAllBooks(page: number) {
    this.bookService.findAllBooks({page: page, size: this.size})
      .subscribe({
        next: (books) => {
          this.bookResponse = books;
          this.pages = PaginationComponent
            .getPages(this.bookResponse.totalPages as number)
        },
        error: (err) => {
        }
      })
  }

  borrowBook(book: BookResponse) {
    this.bookService.borrowBook({'book-id': book.id as number})
      .subscribe({
        next: () => {
          this.showMessage("Book successfully added to your list", "success")
        },
        error: (err: HttpErrorResponse) => {
          if (err.error.errors) {
            this.showMessage(err.error.errors.message, "error")
          }
        }
      })
  }

  showMessage(msg: string, level: 'success' | 'error') {
    this.message = msg;
    this.level = level;

    // Automatically clear the message after 5 seconds (5000 ms)
    setTimeout(() => {
      this.message = '';
      this.level = '';
    }, 5000);
  }

}
