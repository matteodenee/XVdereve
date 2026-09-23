import { Player, Position } from './player.model';
import { Team } from './team.model';

export interface GameState {
  currentTeam: Team;
  availablePlayers: Player[];
  selectedPlayers: Player[];
  remainingPositions: Position[];
  remainingRespins: number;
  draftFinished: boolean;
  kicker: Player | null;
}