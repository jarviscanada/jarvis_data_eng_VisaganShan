import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TraderlistComponent } from './traderlist.component';

describe('TraderlistComponent', () => {
  let component: TraderlistComponent;
  let fixture: ComponentFixture<TraderlistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TraderlistComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TraderlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
