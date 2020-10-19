import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpansionPurchaseComponent } from './expansion-purchase.component';

describe('ExpansionPurchaseComponent', () => {
  let component: ExpansionPurchaseComponent;
  let fixture: ComponentFixture<ExpansionPurchaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpansionPurchaseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpansionPurchaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
