import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { TransactionItemDTO } from '../../models/transaction.dto';

@Component({
  standalone: true,
  selector: 'app-transaction-summary',
  imports: [CommonModule],
  templateUrl: './transaction-summary.component.html',
  styleUrls: ['./transaction-summary.component.css']
})
export class TransactionSummaryComponent {
  @Input() balance: number = 0;
  @Input() selectedItems: TransactionItemDTO[] = [];
  @Input() transactionId: number | null = null;
  @Output() transactionCanceled = new EventEmitter<void>();
  @Output() transactionFinalized = new EventEmitter<void>();
  apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  cancelTransaction(): void {
    if (this.transactionId) {
      this.http.post(`${this.apiUrl}/transactions/${this.transactionId}/cancel`, {})
        .subscribe({
          next: () => {
            this.transactionCanceled.emit();
          },
          error: (error) => {
            console.error('Error canceling transaction:', error);
            alert('Erreur lors de l\'annulation de la transaction');
          }
        });
    }
  }

  finalizeTransaction(): void {
    if (this.transactionId) {
      this.http.post(`${this.apiUrl}/transactions/${this.transactionId}/confirm`, {})
        .subscribe({
          next: (response: any) => {
            this.transactionFinalized.emit();
            alert('Transaction réussie!\n' + 
                  'Monnaie rendue: ' + response.change + '€');
          },
          error: (error) => {
            console.error('Error confirming transaction:', error);
            alert('Erreur lors de la finalisation de la transaction');
          }
        });
    }
  }
}
