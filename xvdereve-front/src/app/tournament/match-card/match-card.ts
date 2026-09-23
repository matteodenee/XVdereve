import {
  Component,
  input
} from '@angular/core';

import {
  MatchResultDto
} from '../../type/match-result-dto';

import {
  MatchNameLabelPipe
} from '../../pipe/match-name-label';

import {
  EventTypeLabelPipe
} from '../../pipe/event-type-label';

import {
  DisplayNamePipe
} from '../../pipe/display-name';


@Component({
  selector: 'app-match-card',

  imports: [
    MatchNameLabelPipe,
    EventTypeLabelPipe,
    DisplayNamePipe
  ],

  templateUrl: './match-card.html',
  styleUrl: './match-card.css'
})
export class MatchCard {

  readonly match =
    input.required<MatchResultDto>();


  isDraw(): boolean {

    return (
      this.match().scoreMyTeam ===
      this.match().scoreAdverseTeam
    );

  }

}