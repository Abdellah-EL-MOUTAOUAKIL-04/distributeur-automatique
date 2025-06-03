import { Routes } from '@angular/router';
import { VendingComponent } from './features/vending/vending.component';

export const routes: Routes = [
  {
    path: 'vending',
    component: VendingComponent,
    data: { title: 'Distributeur Automatique' }
  },
  {
    path: '',
    redirectTo: '/vending',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/vending'
  }
];

export const appRoutes = routes;
