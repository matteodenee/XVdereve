import {
  Component,
  inject,
  signal
} from '@angular/core';

import { Router } from '@angular/router';

import { GameService } from '../service/game';


@Component({
  selector: 'app-home',

  imports: [],

  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  private readonly gameService =
    inject(GameService);

  private readonly router =
    inject(Router);


  readonly loading =
    signal(false);

  readonly error =
    signal<string | null>(null);


  startGame(): void {

    if (this.loading()) {
      return;
    }


    this.loading.set(true);

    this.error.set(null);


    this.gameService
      .startGame()
      .subscribe({

        next: () => {

          this.loading.set(false);

          this.router.navigate([
            '/draft'
          ]);

        },


        error: error => {

          console.error(error);

          this.loading.set(false);

          this.error.set(
            'Impossible de démarrer la partie.'
          );

        }

      });

  }

}