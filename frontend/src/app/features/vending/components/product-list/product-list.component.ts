import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ProductDTO } from '../../models/product.dto';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  @Input() products: ProductDTO[] = [];
  @Input() balance: number = 0;
  @Input() transactionId: number | null = null;
  @Input() totalSelectedItemsCost: number = 0;
  @Output() productSelected = new EventEmitter<ProductDTO>();

  constructor() { }

  ngOnInit(): void { }

  onProductSelected(product: ProductDTO): void {
    this.productSelected.emit(product);
  }

  canPurchase(product: ProductDTO): boolean {
    const remainingBalanceAfterCart = this.balance - this.totalSelectedItemsCost;
    return remainingBalanceAfterCart >= product.price;
  }

  selectProduct(product: ProductDTO): void {
    if (this.canPurchase(product)) {
      this.productSelected.emit(product);
    }
  }
}
