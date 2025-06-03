import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VendingComponent } from './vending.component';

const routes: Routes = [
  {
    path: '',
    component: VendingComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class VendingRoutingModule {}
