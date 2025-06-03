import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { VendingComponent } from './vending.component';
import { CoinInserterComponent } from './components/coin-inserter/coin-inserter.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { SelectedProductsComponent } from './components/selected-products/selected-products.component';
import { TransactionSummaryComponent } from './components/transaction-summary/transaction-summary.component';

const routes: Routes = [
  {
    path: '',
    component: VendingComponent
  }
];

@NgModule({
  declarations: [
    VendingComponent,
    CoinInserterComponent,
    ProductListComponent,
    SelectedProductsComponent,
    TransactionSummaryComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    RouterModule.forChild(routes)
  ],
  exports: [VendingComponent]
})
export class VendingModule {}
