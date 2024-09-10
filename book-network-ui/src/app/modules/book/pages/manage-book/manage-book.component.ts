import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {BookRequest} from "../../../../services/models/book-request";
import {FormsModule} from "@angular/forms";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BookService} from "../../../../services/services/book.service";

@Component({
  selector: 'app-manage-book',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    RouterLink
  ],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit {
  bookRequest: BookRequest = {authorName: "", isbn: "", synopsis: "", title: ""};
  errorMsg: Array<string> = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;

  constructor(private bookService: BookService, private router: Router, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.params['bookId'];
    if (bookId) {
      this.bookService.findBookById({'book-id': bookId})
        .subscribe({
          next: (book) => {
            this.bookRequest = {
              id: bookId,
              title: book.title as string,
              authorName: book.authorName as string,
              isbn: book.isbn as string,
              synopsis: book.synopsis as string,
              shareable: book.shareable,
            };

            this.selectedPicture = book.cover && 'data:image/jpg;base64,' + book.cover;
          },
          error: (err) => {
            console.log(err)
          }
        })

    }
  }

  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover)
    if (this.selectedBookCover) {
      const fileReader = new FileReader();
      fileReader.onload = () => {
        this.selectedPicture = fileReader.result as string;
      }
      fileReader.readAsDataURL(this.selectedBookCover);
    }

  }

  saveBook() {
    this.errorMsg = [];
    this.bookService
      .saveBook({body: this.bookRequest})
      .subscribe({
        next: (bookId) => {
          this.uploadBookCover(bookId);
        },
        error: (err) => {
          console.log(err.error);
          if (err.error.validationErrors) {
            this.errorMsg = this.mapValidationErrorCodeToMsg(err.error.validationErrors)
            // this.errorMsg = err.error.validationErrors;
          } else if (err.error.businessErrorDescription) {
            this.errorMsg = Array.of(err.error.businessErrorDescription);
          } else {
            this.errorMsg = Array.of(err.error.errors.message)
          }
        }
      })
  }

  private uploadBookCover(bookId: number) {
    this.bookService.uploadBookCoverPicture({
      "book-id": bookId,
      body: {
        file: this.selectedBookCover
      }
    }).subscribe({
      next: (bookId) => {
        this.router.navigate(['/books/my-books']);
      }
    })
  }

  private mapValidationErrorCodeToMsg(errors: Array<string>): Array<string> {
    return errors.map(err => {
      switch (err) {
        case '100' :
          return "Title is invalid"
        case '101' :
          return "Author name is invalid"
        case '102' :
          return "ISBN is invalid"
        case '103' :
          return "Synopsis is invalid"
        default:
          return err;
      }
    })

  }
}
