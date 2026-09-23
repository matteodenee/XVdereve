import { Component, input, output } from '@angular/core';

import { PlayerDto } from '../../type/player-dto';
import { PositionLabelPipe } from '../../pipe/position-label';

@Component({
  selector: 'app-player-card',

  imports: [
    PositionLabelPipe
  ],

  templateUrl: './player-card.html',
  styleUrl: './player-card.css'
})
export class PlayerCard {

  readonly player = input.required<PlayerDto>();

  readonly playerSelected = output<number>();
}