import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CoinInserterComponent } from './components/coin-inserter/coin-inserter.component';
import { ProductListComponent } from './components/product-list/product-list.component';

interface ProductDTO {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

interface TransactionDTO {
  id: number;
  insertedAmount: number;
  changeGiven: number;
  items: TransactionItemDTO[];
}

interface TransactionItemDTO {
  id: number;
  productId: number;
  quantity: number;
  product: ProductDTO;
}

@Component({
  standalone: true,
  selector: 'app-vending',
  templateUrl: './vending.component.html',
  styleUrls: ['./vending.component.css'],
  imports: [
    CommonModule,
    FontAwesomeModule,
    CoinInserterComponent,
    ProductListComponent
  ]
})
export class VendingComponent implements OnInit {
  balance: number = 0;
  products: ProductDTO[] = [];
  selectedItems: TransactionItemDTO[] = [];
  validCoins = [0.5, 1, 2, 5, 10];
  apiUrl = 'http://localhost:8080';
  transactionId: number | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.http
      .get<ProductDTO[]>(`${this.apiUrl}/products`)
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

    const params = new HttpParams()
      .set('productId', product.id.toString())
      .set('quantity', '1'); // Default quantity to 1 as per backend

    this.http
      .post<TransactionItemDTO>(`${this.apiUrl}/transactions/${this.transactionId}/select`, null, { params })
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

  onTransactionCanceled(): void {
    if (this.transactionId) {
      this.http
        .post(`${this.apiUrl}/transactions/${this.transactionId}/cancel`, {})
        .subscribe({
          next: (response) => {
            this.balance = 0;
            this.selectedItems = [];
            this.transactionId = null;
          },
          error: (error) => {
            console.error('Error canceling transaction:', error);
          },
        });
    }
  }

  onTransactionFinalized(): void {
    if (this.transactionId) {
      this.http
        .post(`${this.apiUrl}/transactions/${this.transactionId}/confirm`, {})
        .subscribe({
          next: (response: any) => {
            this.balance = 0;
            this.selectedItems = [];
            this.transactionId = null;
            alert('Transaction réussie!\n' + 
                  'Monnaie rendue: ' + response.change + '€');
          },
          error: (error) => {
            console.error('Error confirming transaction:', error);
          },
        });
    }
  }
}
