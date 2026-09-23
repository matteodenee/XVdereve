import { Routes } from '@angular/router';

import { Home } from './home/home';
import { Draft } from './draft/draft';
import { Tournament } from './tournament/tournament';

export const routes: Routes = [
  {
    path: '',
    component: Home
  },
  {
    path: 'draft',
    component: Draft
  },
  {
    path: 'tournament',
    component: Tournament
  },
  {
    path: '**',
    redirectTo: ''
  }
];