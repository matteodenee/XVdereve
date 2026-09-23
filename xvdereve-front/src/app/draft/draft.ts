import { Component, inject } from '@angular/core';

import { GameService } from '../service/game';

@Component({
  selector: 'app-draft',
  imports: [],
  templateUrl: './draft.html',
  styleUrl: './draft.css'
})
export class Draft {

  readonly gameService = inject(GameService);

  readonly gameState = this.gameService.gameState;
}