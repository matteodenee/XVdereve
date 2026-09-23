import { Player } from './player.model';

export interface Team {
  id: number;
  country: string;
  year: number;
  description: string | null;
  logoUrl: string | null;
  players: Player[];
}