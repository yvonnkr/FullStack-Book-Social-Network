import { TestBed } from '@angular/core/testing';
import { HttpInterceptorFn } from '@angular/common/http';

import { HttpTokenInterceptor } from './http-token.interceptor';

describe('HttpTokenInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      HttpTokenInterceptor
    ]
  }));

  it('should be created', () => {
    const interceptor: HttpTokenInterceptor = TestBed.inject(HttpTokenInterceptor);
    expect(interceptor).toBeTruthy();
  });
});

// importing as a function was causing an error hence above changes!
//import { httpTokenInterceptor } from './http-token.interceptor';

// describe('httpTokenInterceptor', () => {
//   const interceptor: HttpInterceptorFn = (req, next) =>
//     TestBed.runInInjectionContext(() => httpTokenInterceptor(req, next));
//
//   beforeEach(() => {
//     TestBed.configureTestingModule({});
//   });
//
//   it('should be created', () => {
//     expect(interceptor).toBeTruthy();
//   });
// });

