import { PlayerDto } from './player-dto';

export interface TeamDto {
  id: number;
  country: string;
  year: number;
  description: string | null;
  logoUrl: string | null;
  players: PlayerDto[];
}