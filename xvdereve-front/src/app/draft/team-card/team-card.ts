import { Component, input, output } from '@angular/core';
import { TeamDto } from '../../type/team-dto';
import { DisplayNamePipe } from '../../pipe/display-name';

@Component({
  selector: 'app-team-card',
  imports: [DisplayNamePipe],
  templateUrl: './team-card.html',
  styleUrl: './team-card.css'
})
export class TeamCard {

  readonly team = input.required<TeamDto>();
  readonly remainingRespins = input.required<number>();

  readonly respin = output<void>();
}