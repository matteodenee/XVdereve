import {
  Component,
  computed,
  inject,
  signal
} from '@angular/core';

import {
  Router,
  RouterLink
} from '@angular/router';

import { GameService } from '../service/game';

import {
  POSITION_ORDER
} from '../type/position';

import {
  PositionLabelPipe
} from '../pipe/position-label';


@Component({
  selector: 'app-kicker',

  imports: [
    RouterLink,
    PositionLabelPipe
  ],

  templateUrl: './kicker.html',
  styleUrl: './kicker.css'
})
export class Kicker {

  readonly gameService =
    inject(GameService);

  private readonly router =
    inject(Router);


  readonly gameState =
    this.gameService.gameState;


  readonly selectedKickerId =
    signal<number | null>(null);


  readonly loading =
    signal(false);


  readonly error =
    signal<string | null>(null);


  /*
   * On affiche le XV dans l'ordre rugby,
   * peu importe l'ordre dans lequel
   * les joueurs ont été sélectionnés.
   */
  readonly sortedPlayers = computed(() => {

    const state = this.gameState();

    if (!state) {
      return [];
    }


    return [...state.selectedPlayers].sort(
      (a, b) => {

        const indexA =
          POSITION_ORDER.indexOf(
            a.position
          );

        const indexB =
          POSITION_ORDER.indexOf(
            b.position
          );

        return indexA - indexB;
      }
    );

  });


  selectKicker(playerId: number): void {

    this.selectedKickerId.set(
      playerId
    );

  }


  confirmKicker(): void {

    const playerId =
      this.selectedKickerId();


    if (playerId === null) {
      return;
    }


    if (this.loading()) {
      return;
    }


    this.loading.set(true);

    this.error.set(null);


    this.gameService
      .chooseKicker(playerId)
      .subscribe({

        next: () => {

          this.loading.set(false);

          this.router.navigate([
            '/tournament'
          ]);

        },


        error: () => {

          this.loading.set(false);

          this.error.set(
            'Impossible de sélectionner ce buteur.'
          );

        }

      });

  }

}