import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoinInserterComponent } from './coin-inserter.component';

describe('CoinInserterComponent', () => {
  let component: CoinInserterComponent;
  let fixture: ComponentFixture<CoinInserterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CoinInserterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CoinInserterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
