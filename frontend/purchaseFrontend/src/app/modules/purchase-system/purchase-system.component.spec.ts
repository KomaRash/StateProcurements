import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchaseSystemComponent } from './purchase-system.component';

describe('PurchaseSystemComponent', () => {
  let component: PurchaseSystemComponent;
  let fixture: ComponentFixture<PurchaseSystemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PurchaseSystemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PurchaseSystemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
