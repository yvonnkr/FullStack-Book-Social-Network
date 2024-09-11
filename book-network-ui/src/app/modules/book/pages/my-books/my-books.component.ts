import {Component, OnInit} from '@angular/core';
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {BookService} from "../../../../services/services/book.service";
import {Router, RouterLink} from "@angular/router";
import {BookResponse} from "../../../../services/models/book-response";
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {NgForOf, NgIf} from "@angular/common";
import {PaginationComponent} from "../../components/pagination/pagination.component";

@Component({
  selector: 'app-my-books',
  standalone: true,
  imports: [
    BookCardComponent,
    NgForOf,
    NgIf,
    RouterLink,
    PaginationComponent
  ],
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  pages: any = [];
  size = 5;

  constructor(private bookService: BookService, private router: Router) {
  }

  ngOnInit(): void {
    this.findAllBooksByOwner(this.page);
  }

  onPageChanged(page: number) {
    this.findAllBooksByOwner(page)
  }

  private findAllBooksByOwner(page: number) {
    this.bookService.findAllBooksByOwner({page: page, size: this.size})
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

  archiveBook(book: BookResponse) {
    this.bookService.updateArchivedStatus({'book-id': book.id as number})
      .subscribe({
        next: () => {
          book.archived = !book.archived
        }
      })
  }

  shareBook(book: BookResponse) {
    this.bookService.updateShareableStatus({'book-id': book.id as number})
      .subscribe({
        next: () => {
          book.shareable = !book.shareable
        }
      })
  }

  editBook(book: BookResponse) {
    this.router.navigate(['/books/manage/', book.id])
  }
}
