import {Component, OnInit} from '@angular/core';
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {BookService} from "../../../../services/services/book.service";
import {Router, RouterLink} from "@angular/router";
import {BookResponse} from "../../../../services/models/book-response";
import {BookCardComponent} from "../../components/book-card/book-card.component";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-my-books',
  standalone: true,
  imports: [
    BookCardComponent,
    NgForOf,
    NgIf,
    RouterLink
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
    this.findAllBooksByOwner();
  }


  private findAllBooksByOwner() {

    this.bookService.findAllBooksByOwner({page: this.page, size: this.size})
      .subscribe({
        next: (books) => {
          this.bookResponse = books;
          this.pages = Array(this.bookResponse.totalPages)
            .fill(0)
            .map((x, i) => i)
        },
        error: (err) => {
        }
      })
  }

  //Pagination Start
  goToFirstPage() {
    this.page = 0;
    this.findAllBooksByOwner()
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooksByOwner()
  }

  goToPage(page: number) {
    this.page = page
    this.findAllBooksByOwner()
  }

  goToNextPage() {
    this.page++;
    this.findAllBooksByOwner()
  }

  goToLastPage() {
    this.page = this.lastPage
    this.findAllBooksByOwner()
  }

  get isLastPage(): boolean {
    return this.page === this.lastPage
  }

  get lastPage(): number {
    return this.bookResponse.totalPages as number - 1
  }

  //Pagination End


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
