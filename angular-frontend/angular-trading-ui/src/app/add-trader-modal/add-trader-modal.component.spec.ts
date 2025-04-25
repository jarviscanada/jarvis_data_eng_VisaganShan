import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTraderModalComponent } from './add-trader-modal.component';

describe('AddTraderModalComponent', () => {
  let component: AddTraderModalComponent;
  let fixture: ComponentFixture<AddTraderModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddTraderModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddTraderModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
