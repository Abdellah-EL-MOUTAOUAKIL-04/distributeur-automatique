import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionItemDTO } from '../../models/transaction.dto';

@Component({
  standalone: true,
  selector: 'app-selected-products',
  imports: [CommonModule],
  templateUrl: './selected-products.component.html',
  styleUrls: ['./selected-products.component.css']
})
export class SelectedProductsComponent {
  @Input() selectedItems: TransactionItemDTO[] = [];
  @Input() total: number = 0;
  @Output() itemRemoved = new EventEmitter<TransactionItemDTO>();
  @Output() anotherItemRemoved = new EventEmitter<TransactionItemDTO>();

  getQuantity(item: TransactionItemDTO): number {
    return item.quantity;
  }

  getProductName(item: TransactionItemDTO): string {
    return item.product.name;
  }

  getProductPrice(item: TransactionItemDTO): number {
    return item.product.price;
  }

  onRemoveItem(item: TransactionItemDTO): void {
    this.itemRemoved.emit(item);
  }

  onRemoveAnotherItem(item: TransactionItemDTO): void {
    this.anotherItemRemoved.emit(item);
  }
}
