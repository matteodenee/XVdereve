import { Position } from './position';

export interface PlayerDto {
  id: number;
  name: string;
  position: Position;
  overall: number;
  canKick: boolean;
  country: string;
  year: number;
}