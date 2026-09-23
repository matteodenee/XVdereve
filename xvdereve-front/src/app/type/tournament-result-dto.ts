import { MatchResultDto } from './match-result-dto';

export interface TournamentResultDto {
  qualified: boolean;

  champion: boolean;

  points: number;

  wins: number;
  losses: number;

  matches: MatchResultDto[];
}