import {
  Component,
  computed,
  inject,
  signal
} from '@angular/core';

import { Router, RouterLink } from '@angular/router';

import { GameService } from '../service/game';

import { TeamCard } from './team-card/team-card';
import { PlayerCard } from './player-card/player-card';
import { SelectedTeam } from './selected-team/selected-team';

import { POSITION_ORDER } from '../type/position';


type SortType =
  | 'alphabetical'
  | 'overall'
  | 'position';


@Component({
  selector: 'app-draft',

  imports: [
    RouterLink,
    TeamCard,
    PlayerCard,
    SelectedTeam
  ],

  templateUrl: './draft.html',
  styleUrl: './draft.css'
})
export class Draft {

  readonly gameService = inject(GameService);

  private readonly router = inject(Router);

  readonly gameState = this.gameService.gameState;

  readonly loading = signal(false);

  readonly error = signal<string | null>(null);

  readonly sortType = signal<SortType>('overall');


  readonly sortedPlayers = computed(() => {

    const state = this.gameState();

    if (!state) {
      return [];
    }

    const players = [...state.availablePlayers];


    switch (this.sortType()) {

      case 'alphabetical':

        return players.sort(
          (a, b) =>
            a.name.localeCompare(b.name, 'fr')
        );


      case 'overall':

        return players.sort(
          (a, b) =>
            b.overall - a.overall
        );


      case 'position':

        return players.sort(
          (a, b) => {

            const indexA =
              POSITION_ORDER.indexOf(a.position);

            const indexB =
              POSITION_ORDER.indexOf(b.position);


            if (indexA !== indexB) {
              return indexA - indexB;
            }


            return b.overall - a.overall;
          }
        );


      default:

        return players;
    }
  });


  changeSort(sort: SortType): void {

    this.sortType.set(sort);

  }


  respin(): void {

    if (this.loading()) {
      return;
    }

    this.loading.set(true);

    this.error.set(null);


    this.gameService.respin().subscribe({

      next: () => {

        this.loading.set(false);

      },

      error: () => {

        this.loading.set(false);

        this.error.set(
          'Impossible de tirer une nouvelle équipe.'
        );

      }

    });
  }


  selectPlayer(playerId: number): void {

    if (this.loading()) {
      return;
    }

    this.loading.set(true);

    this.error.set(null);


    this.gameService
      .pickPlayer(playerId)
      .subscribe({

        next: state => {

          this.loading.set(false);


          /*
           * Dès que les 15 postes sont remplis,
           * on passe au choix du buteur.
           */
          if (state.draftFinished) {

            this.router.navigate([
              '/kicker'
            ]);

          }

        },


        error: () => {

          this.loading.set(false);

          this.error.set(
            'Impossible de sélectionner ce joueur.'
          );

        }

      });

  }

}