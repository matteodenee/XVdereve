export type MatchName =
  | 'POULE'
  | 'QUART_DE_FINALE'
  | 'DEMI_FINALE'
  | 'FINALE';

export const MATCH_NAME_LABELS: Record<MatchName, string> = {
  POULE: 'Match de poule',
  QUART_DE_FINALE: 'Quart de finale',
  DEMI_FINALE: 'Demi-finale',
  FINALE: 'Finale'
};