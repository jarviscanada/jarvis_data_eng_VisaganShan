import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraderdetailsComponent } from './traderdetails.component';

describe('TraderdetailsComponent', () => {
  let component: TraderdetailsComponent;
  let fixture: ComponentFixture<TraderdetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TraderdetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TraderdetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
