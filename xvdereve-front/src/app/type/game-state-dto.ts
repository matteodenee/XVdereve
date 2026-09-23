import { PlayerDto } from './player-dto';
import { Position } from './position';
import { TeamDto } from './team-dto';

export interface GameStateDto {
  currentTeam: TeamDto;
  availablePlayers: PlayerDto[];
  selectedPlayers: PlayerDto[];
  remainingPositions: Position[];
  remainingRespins: number;
  draftFinished: boolean;
  kicker: PlayerDto | null;
}