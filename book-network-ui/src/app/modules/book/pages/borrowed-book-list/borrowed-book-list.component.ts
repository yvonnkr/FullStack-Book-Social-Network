import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {NgForOf, NgIf} from "@angular/common";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";
import {Router, RouterLink} from "@angular/router";
import {PaginationComponent} from "../../components/pagination/pagination.component";
import {FeedbackRequest} from "../../../../services/models/feedback-request";
import {FormsModule} from "@angular/forms";
import {RatingComponent} from "../../components/rating/rating.component";
import {FeedbackService} from "../../../../services/services/feedback.service";

@Component({
  selector: 'app-borrowed-book-list',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    PaginationComponent,
    FormsModule,
    RatingComponent,
    RouterLink
  ],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit {
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  feedbackRequest: FeedbackRequest = {bookId: 0, comment: '', note: 0}
  page = 0;
  size = 5;
  pages: any = [];
  selectedBook: BorrowedBookResponse | undefined = undefined;

  constructor(private bookService: BookService,
              private feedbackService: FeedbackService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.findAllBorrowedBooks(this.page);
  }

  onPageChanged(page: number) {
    this.findAllBorrowedBooks(page)
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;

  }

  returnBook(withFeedback: boolean) {
    this.bookService.returnBorrowedBook({'book-id': this.selectedBook?.id as number})
      .subscribe({
        next: () => {
          if (withFeedback) {
            this.giveFeedback()
          }
          this.selectedBook = undefined;
          this.findAllBorrowedBooks(this.page);
        }
      })

  }

  private findAllBorrowedBooks(page: number) {
    this.bookService.findAllBorrowedBooks({page: page, size: this.size})
      .subscribe({
        next: result => {
          this.borrowedBooks = result
          this.pages = PaginationComponent
            .getPages(this.borrowedBooks.totalPages as number)
        }
      })
  }

  private giveFeedback() {
    this.feedbackService.saveFeedback({body: this.feedbackRequest})
      .subscribe({
        next: () => {}
      })
  }
}
