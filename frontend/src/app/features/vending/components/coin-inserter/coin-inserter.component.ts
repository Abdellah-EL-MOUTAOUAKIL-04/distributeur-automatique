import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { TransactionDTO } from '../../models/transaction.dto';

@Component({
  standalone: true,
  selector: 'app-coin-inserter',
  imports: [CommonModule],
  templateUrl: './coin-inserter.component.html',
  styleUrls: ['./coin-inserter.component.css']
})
export class CoinInserterComponent {
  @Input() balance: number = 0;
  @Output() coinInserted = new EventEmitter<{ amount: number, transaction: TransactionDTO }>();
  @Input() transactionId: number | null = null;
  validCoins = [0.5, 1, 2, 5, 10];
  apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  insertCoin(amount: number): void {
    console.log('CoinInserterComponent: insertCoin called with amount:', amount, '| Current transactionId:', this.transactionId);

    if (this.transactionId) {
      // Active transaction exists, add coin to it
      const params = new HttpParams().set('coin', amount.toString());
      this.http.post<TransactionDTO>(`${this.apiUrl}/transactions/${this.transactionId}/insert`, null, { params })
        .subscribe({
          next: (updatedTransaction) => {
            console.log('CoinInserterComponent: Coin inserted into existing transaction. Response:', updatedTransaction);
            this.coinInserted.emit({ amount, transaction: updatedTransaction });
          },
          error: (error) => {
            console.error('CoinInserterComponent: Error inserting coin into existing transaction:', error);
            alert('Erreur lors de l\'ajout de la pièce à la transaction existante.');
          }
        });
    } else {
      // No active transaction, create a new one
      const params = new HttpParams().set('insertedAmount', amount.toString());
      this.http.post<TransactionDTO>(`${this.apiUrl}/transactions`, null, { params })
        .subscribe({
          next: (newTransaction) => {
            console.log('CoinInserterComponent: New transaction created. Response:', newTransaction);
            this.coinInserted.emit({ amount, transaction: newTransaction });
          },
          error: (error) => {
            console.error('CoinInserterComponent: Error creating new transaction:', error);
            alert('Erreur lors de la création de la nouvelle transaction.');
          }
        });
    }
  }
}
