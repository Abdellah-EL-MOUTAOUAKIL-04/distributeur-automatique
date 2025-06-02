import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  // il faut AU MOINS UNE route ici (même vide mais structure présente)
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'vending', loadChildren: () => import('./features/vending/vending.module').then(m => m.VendingModule) },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
