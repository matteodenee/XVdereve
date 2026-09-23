import {
  Component,
  computed,
  inject,
  signal
} from '@angular/core';

import {
  RouterLink
} from '@angular/router';

import {
  GameService
} from '../service/game';

import {
  MatchCard
} from './match-card/match-card';


@Component({
  selector: 'app-tournament',

  imports: [
    RouterLink,
    MatchCard
  ],

  templateUrl: './tournament.html',
  styleUrl: './tournament.css'
})
export class Tournament {

  readonly gameService =
    inject(GameService);


  readonly gameState =
    this.gameService.gameState;


  readonly result =
    this.gameService.tournamentResult;


  readonly loading =
    signal(false);


  readonly error =
    signal<string | null>(null);


  readonly statusTitle = computed(() => {

    const result = this.result();

    if (!result) {
      return '';
    }

    if (result.champion) {
      return 'Champion du monde !';
    }

    if (result.qualified) {
      return 'Éliminé en phase finale';
    }

    return 'Éliminé en phase de poules';

  });


  simulate(): void {

    if (this.loading()) {
      return;
    }

    this.loading.set(true);

    this.error.set(null);


    this.gameService
      .simulateTournament()
      .subscribe({

        next: () => {

          this.loading.set(false);

        },

        error: () => {

          this.loading.set(false);

          this.error.set(
            'Impossible de simuler le tournoi.'
          );

        }

      });

  }

}