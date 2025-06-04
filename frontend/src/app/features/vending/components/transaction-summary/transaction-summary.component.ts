import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VendingService } from '../../../../core/services/vending.service';
import { TransactionItemDTO, ChangeBreakdownDTO, FinalizeTransactionResponseDTO } from '../../models/transaction.dto';
import { ChangeDialogComponent } from '../change-dialog/change-dialog.component';

@Component({
  standalone: true,
  selector: 'app-transaction-summary',
  imports: [CommonModule, ChangeDialogComponent],
  templateUrl: './transaction-summary.component.html',
  styleUrls: ['./transaction-summary.component.css']
})
export class TransactionSummaryComponent {

  calculateSubtotal(): number {
    return this.selectedItems.reduce((acc, item) => acc + (item.product.price * item.quantity), 0);
  }

  @Input() balance: number = 0;
  @Input() selectedItems: TransactionItemDTO[] = [];
  @Input() transactionId: number | null = null;
  @Output() transactionCanceled = new EventEmitter<void>();
  @Output() transactionFinalized = new EventEmitter<void>();

  showChangeDialog: boolean = false;
  currentChangeBreakdown: ChangeBreakdownDTO | null = null;

  constructor(private vendingService: VendingService) {}

  cancelTransaction(): void {
    if (this.transactionId) {
      this.vendingService.cancelTransaction(this.transactionId)
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
      this.vendingService.confirmTransaction(this.transactionId)
        .subscribe({
          next: (response: FinalizeTransactionResponseDTO) => {
            console.log('Transaction successful, change:', response.change);
            this.currentChangeBreakdown = response.change;
            this.showChangeDialog = true;
            // transactionFinalized will be emitted when the change dialog is closed
          },
          error: (error) => {
            console.error('Error confirming transaction:', error);
            alert('Erreur lors de la finalisation de la transaction');
          }
        });
    }
  }

  handleCloseChangeDialog(): void {
    this.showChangeDialog = false;
    this.currentChangeBreakdown = null;
    this.transactionFinalized.emit(); // Emit event after dialog is closed
  }
}
