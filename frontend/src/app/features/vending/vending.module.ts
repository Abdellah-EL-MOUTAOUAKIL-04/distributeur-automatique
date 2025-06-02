import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VendingRoutingModule } from './vending-routing.module';
import { VendingComponent } from './vending.component';


@NgModule({
  declarations: [
    VendingComponent
  ],
  imports: [
    CommonModule,
    VendingRoutingModule
  ]
})
export class VendingModule { }
