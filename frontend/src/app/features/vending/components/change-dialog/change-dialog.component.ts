import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChangeBreakdownDTO } from '../../models/transaction.dto';

@Component({
  selector: 'app-change-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './change-dialog.component.html',
  styleUrls: ['./change-dialog.component.css']
})
export class ChangeDialogComponent {
  @Input() changeBreakdown: ChangeBreakdownDTO | null = null;
  @Output() closeDialog = new EventEmitter<void>();

  constructor() { }

  getCoinEntries(): { coinValue: string, quantity: number }[] {
    if (!this.changeBreakdown) {
      return [];
    }
    // Ensure coinValue is treated as a string for consistent key access if needed later
    return Object.entries(this.changeBreakdown).map(([originalCoinValue, quantity]) => {
      const numValue = parseFloat(originalCoinValue);
      let formattedCoinValue = originalCoinValue; // Default to original string
      // Check if the number is an integer (e.g., 2.0, 5.0 but not 0.5)
      if (numValue % 1 === 0) {
        formattedCoinValue = numValue.toString(); // Converts 2.0 to "2"
      }
      return {
        coinValue: formattedCoinValue,
        quantity
      };
    });
  }

  public get totalChangeAmount(): number {
    if (!this.changeBreakdown) {
      return 0;
    }
    return Object.entries(this.changeBreakdown).reduce((sum, [coinValueStr, quantity]) => {
      const coinValue = parseFloat(coinValueStr);
      return sum + (coinValue * quantity);
    }, 0);
  }

  onClose(): void {
    this.closeDialog.emit();
  }
}
