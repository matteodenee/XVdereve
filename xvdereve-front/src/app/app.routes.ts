import { Routes } from '@angular/router';

import { Home } from './home/home';
import { Draft } from './draft/draft';
import { Kicker } from './kicker/kicker';
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
    path: 'kicker',
    component: Kicker
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