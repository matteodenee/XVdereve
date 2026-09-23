export type Position =
  | 'Pilier'
  | 'Talonneur'
  | 'DeuxiemeLigne'
  | 'TroisiemeLigneAile'
  | 'TroisiemeLigneCentre'
  | 'DemiDeMelee'
  | 'DemiOuverture'
  | 'Ailier'
  | 'Centre'
  | 'Arriere';


export const POSITION_ORDER: Position[] = [
  'Pilier',
  'Talonneur',
  'DeuxiemeLigne',
  'TroisiemeLigneAile',
  'TroisiemeLigneCentre',
  'DemiDeMelee',
  'DemiOuverture',
  'Ailier',
  'Centre',
  'Arriere'
];


export const POSITION_LABELS: Record<Position, string> = {
  Pilier: 'Pilier',
  Talonneur: 'Talonneur',
  DeuxiemeLigne: 'Deuxième ligne',
  TroisiemeLigneAile: 'Troisième ligne aile',
  TroisiemeLigneCentre: 'Troisième ligne centre',
  DemiDeMelee: 'Demi de mêlée',
  DemiOuverture: 'Demi d’ouverture',
  Ailier: 'Ailier',
  Centre: 'Centre',
  Arriere: 'Arrière'
};