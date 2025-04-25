import { TestBed } from '@angular/core/testing';

import { TraderlistService } from './traderlist.service';

describe('TraderlistService', () => {
  let service: TraderlistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TraderlistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
