import { EventType } from './event-type';
import { PlayerDto } from './player-dto';

export interface MatchEventDto {
  type: EventType;

  player: PlayerDto;

  points: number;

  scoreMyTeam: number;
  scoreAdverseTeam: number;

  myTeamEvent: boolean;
}