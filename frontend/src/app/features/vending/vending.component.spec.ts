import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendingComponent } from './vending.component';

describe('VendingComponent', () => {
  let component: VendingComponent;
  let fixture: ComponentFixture<VendingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VendingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
