import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VendingService } from '../../../../core/services/vending.service';
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

  constructor(private vendingService: VendingService) {}

  insertCoin(amount: number): void {
    console.log('CoinInserterComponent: insertCoin called with amount:', amount, '| Current transactionId:', this.transactionId);

    this.vendingService.insertCoin(amount, this.transactionId)
      .subscribe({
        next: (transaction) => {
          console.log('CoinInserterComponent: Coin inserted. Response:', transaction);
          this.coinInserted.emit({ amount, transaction });
        },
        error: (error) => {
          console.error('CoinInserterComponent: Error inserting coin:', error);
          // Determine if it was a new transaction or adding to existing for a more specific alert
          const action = this.transactionId ? 'ajout de la pièce' : 'création de la transaction';
          alert(`Erreur lors de l'${action}.`);
        }
      });
  }
}
