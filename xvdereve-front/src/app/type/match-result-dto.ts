import { MatchEventDto } from './match-event-dto';
import { MatchName } from './match-name';
import { TeamDto } from './team-dto';

export interface MatchResultDto {
  myTeam: TeamDto;
  adverseTeam: TeamDto;

  scoreMyTeam: number;
  scoreAdverseTeam: number;

  victory: boolean;

  bonusPoints: number;

  matchName: MatchName;

  idMatch: number;

  events: MatchEventDto[];
}