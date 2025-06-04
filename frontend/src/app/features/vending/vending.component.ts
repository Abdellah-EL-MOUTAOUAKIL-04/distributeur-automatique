import { Component, OnInit } from '@angular/core';
import { VendingService } from '../../core/services/vending.service';
import { ProductDTO } from './models/product.dto';
import { TransactionDTO, TransactionItemDTO } from './models/transaction.dto';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CoinInserterComponent } from './components/coin-inserter/coin-inserter.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { SelectedProductsComponent } from './components/selected-products/selected-products.component';
import { TransactionSummaryComponent } from './components/transaction-summary/transaction-summary.component';

@Component({
  standalone: true,
  selector: 'app-vending',
  templateUrl: './vending.component.html',
  styleUrls: ['./vending.component.css'],
  imports: [
    CommonModule,
    FontAwesomeModule,
    CoinInserterComponent,
    ProductListComponent,
    SelectedProductsComponent,
    TransactionSummaryComponent
  ]
})
export class VendingComponent implements OnInit {
  balance: number = 0;
  products: ProductDTO[] = [];
  selectedItems: TransactionItemDTO[] = [];
  validCoins = [0.5, 1, 2, 5, 10]; // This might move to CoinInserterComponent or be fed by VendingService if dynamic
  transactionId: number | null = null;

  constructor(private vendingService: VendingService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.vendingService.getProducts()
      .subscribe({
        next: (products) => {
          this.products = products;
        },
        error: (error) => {
          console.error('Error loading products:', error);
        },
      });
  }

  onCoinInserted(event: { amount: number, transaction: TransactionDTO }): void {
      console.log('VendingComponent: onCoinInserted event received:', event);
    this.transactionId = event.transaction.id;
    this.balance = event.transaction.insertedAmount;
    // The actual coin insertion/transaction creation is now handled by CoinInserterComponent
  }

  onProductSelected(product: ProductDTO): void {
    if (!this.transactionId) {
      alert('Veuillez d\'abord insérer une pièce pour démarrer une transaction.');
      return;
    }
    // Client-side check, though backend also validates
    if (!this.canPurchase(product)) {
       alert('Fonds insuffisants pour sélectionner ce produit.');
       return;
    }

    this.vendingService.selectProduct(this.transactionId, product.id, 1) // Assuming quantity 1 for selection
      .subscribe({
        next: (selectedItemDTO) => {
          // Assuming selectedItemDTO.product is populated by the backend mapper.
          const existingItemIndex = this.selectedItems.findIndex(
            item => item.product.id === selectedItemDTO.product.id
          );

          if (existingItemIndex > -1) {
            // Product already exists in cart, update its quantity
            this.selectedItems[existingItemIndex].quantity = selectedItemDTO.quantity;
            console.log('Product quantity updated:', this.selectedItems[existingItemIndex]);
          } else {
            // Product not in cart, add it
            this.selectedItems.push(selectedItemDTO);
            console.log('Product selected and added:', selectedItemDTO);
          }
        },
        error: (httpErrorResponse) => {
          console.error('Error selecting product:', httpErrorResponse);
          if (httpErrorResponse.error && httpErrorResponse.error.error) {
            alert(`Erreur: ${httpErrorResponse.error.error}`); // Specific error from backend like 'Fonds insuffisants'
          } else {
            alert('Erreur lors de la sélection du produit. Veuillez réessayer.');
          }
        },
      });
  }

  canPurchase(product: ProductDTO): boolean {
    return this.balance >= product.price;
  }

  get totalSelected(): number {
    return this.selectedItems.reduce((sum, item) => sum + item.product.price * item.quantity, 0);
  }

  removeFromCart(item: TransactionItemDTO): void {
    const index = this.selectedItems.indexOf(item);
    if (index > -1) {
      this.selectedItems.splice(index, 1);
    }
  }

  // Called when TransactionSummaryComponent emits transactionCanceled
  handleTransactionCanceled(): void {
    this.balance = 0;
    this.selectedItems = [];
    this.transactionId = null;
    // Potentially refresh products if stock changed and wasn't updated by other means
    // this.loadProducts(); 
  }

  // Called when TransactionSummaryComponent emits transactionFinalized
  handleTransactionFinalized(event?: { change?: number }): void { // Event might carry change info
    this.balance = 0;
    this.selectedItems = [];
    this.transactionId = null;
    // The alert is now handled by TransactionSummaryComponent
    // If you need to display change here or do other UI updates, use the event data.
    // Potentially refresh products
    // this.loadProducts();
  }
}
