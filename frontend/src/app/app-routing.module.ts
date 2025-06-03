import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'vending',
    loadChildren: () =>
      import('./features/vending/vending.module').then(m => m.VendingModule),
  },
  { path: '', redirectTo: 'vending', pathMatch: 'full' },
  { path: '**', redirectTo: 'vending' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
