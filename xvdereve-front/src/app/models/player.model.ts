export type Position =
  | 'Pilier'
  | 'Talonneur'
  | 'DeuxiemeLigne'
  | 'TroisiemeLigneAile'
  | 'TroisiemeLigneCentre'
  | 'DemiDeMelee'
  | 'DemiOuverture'
  | 'Centre'
  | 'Ailier'
  | 'Arriere';

export interface Player {
  id: number;
  name: string;
  position: Position;
  overall: number;
  canKick: boolean;
  country: string;
  year: number;
}