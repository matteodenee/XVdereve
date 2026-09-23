import {
  Component,
  computed,
  input
} from '@angular/core';

import { PlayerDto } from '../../type/player-dto';
import {
  Position,
  POSITION_ORDER
} from '../../type/position';

import { PositionLabelPipe } from '../../pipe/position-label';

@Component({
  selector: 'app-selected-team',

  imports: [
    PositionLabelPipe
  ],

  templateUrl: './selected-team.html',
  styleUrl: './selected-team.css'
})
export class SelectedTeam {

  readonly players =
    input.required<PlayerDto[]>();

  readonly remainingPositions =
    input.required<Position[]>();


  readonly sortedPlayers = computed(() => {

    return [...this.players()].sort(
      (a, b) => {

        const indexA =
          POSITION_ORDER.indexOf(a.position);

        const indexB =
          POSITION_ORDER.indexOf(b.position);

        return indexA - indexB;
      }
    );

  });
}