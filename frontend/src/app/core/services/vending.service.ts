import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDTO } from '../../features/vending/models/product.dto';
import { TransactionDTO, TransactionItemDTO, FinalizeTransactionResponseDTO } from '../../features/vending/models/transaction.dto';

@Injectable({
  providedIn: 'root'
})
export class VendingService {
  private apiUrl = 'http://localhost:8080'; // Base API URL

  constructor(private http: HttpClient) { }

  // Method to fetch products
  getProducts(): Observable<ProductDTO[]> {
    return this.http.get<ProductDTO[]>(`${this.apiUrl}/products`);
  }

  // Method to cancel a transaction
  cancelTransaction(transactionId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/transactions/${transactionId}/cancel`, {});
  }

  // Method to confirm/finalize a transaction
  confirmTransaction(transactionId: number): Observable<FinalizeTransactionResponseDTO> {
    return this.http.post<FinalizeTransactionResponseDTO>(`${this.apiUrl}/transactions/${transactionId}/confirm`, {});
  }


  selectProduct(transactionId: number, productId: number, quantity: number): Observable<TransactionItemDTO> {
    const params = { productId: productId.toString(), quantity: quantity.toString() }; // Params should be string
    return this.http.post<TransactionItemDTO>(`${this.apiUrl}/transactions/${transactionId}/select`, null, { params });
  }

  insertCoin(insertedAmount: number, currentTransactionId: number | null): Observable<TransactionDTO> {
    let url: string;
  
    if (currentTransactionId) {
      url = `${this.apiUrl}/transactions/${currentTransactionId}/insert?coin=${insertedAmount}`;
    } else {
      url = `${this.apiUrl}/transactions?insertedAmount=${insertedAmount}`;
    }
  
    return this.http.post<TransactionDTO>(url, {});
  }   
}

