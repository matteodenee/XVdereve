import { MatchResultDto } from './match-result-dto';
import { TeamDto } from './team-dto';

export interface PoolStandingDto {
  team: TeamDto;
  playerTeam: boolean;

  played: number;
  wins: number;
  draws: number;
  losses: number;

  pointsFor: number;
  pointsAgainst: number;
  pointsDifference: number;

  points: number;
}

export interface TournamentResultDto {
  qualified: boolean;
  champion: boolean;

  points: number;
  wins: number;
  losses: number;

  matches: MatchResultDto[];

  poolStandings: PoolStandingDto[];
  poolMatches: MatchResultDto[];
}