import {
  Pipe,
  PipeTransform
} from '@angular/core';

import {
  EventType,
  EVENT_TYPE_LABELS
} from '../type/event-type';

@Pipe({
  name: 'eventTypeLabel',
  standalone: true
})
export class EventTypeLabelPipe
  implements PipeTransform {

  transform(eventType: EventType): string {
    return EVENT_TYPE_LABELS[eventType];
  }
}