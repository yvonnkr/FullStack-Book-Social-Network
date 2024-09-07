import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {NgForOf} from "@angular/common";
import {BookCardComponent} from "../../components/book-card/book-card.component";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    NgForOf,
    BookCardComponent
  ],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  pages: any = [];
  size = 5;

  constructor(private bookService: BookService, private router: Router) {
  }

  ngOnInit(): void {
    this.findAllBooks();
  }


  private findAllBooks() {
    this.bookService.findAllBooks({page: this.page, size: this.size})
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

  // Pagination

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks()
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooks()
  }

  goToPage(page: number) {
    this.page = page
    this.findAllBooks()
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks()
  }

  goToLastPage() {
    this.page = this.lastPage
    this.findAllBooks()
  }

  get isLastPage(): boolean {
    return this.page === this.lastPage
  }

  get lastPage(): number {
    return this.bookResponse.totalPages as number - 1
  }

}
