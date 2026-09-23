export type EventType =
  | 'ESSAI'
  | 'TRANSFORMATION'
  | 'PENALITE'
  | 'DROP';

export const EVENT_TYPE_LABELS: Record<EventType, string> = {
  ESSAI: 'Essai',
  TRANSFORMATION: 'Transformation',
  PENALITE: 'Pénalité',
  DROP: 'Drop'
};