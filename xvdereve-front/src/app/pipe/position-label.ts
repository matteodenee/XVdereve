import { Pipe, PipeTransform } from '@angular/core';

import {
  Position,
  POSITION_LABELS
} from '../type/position';

@Pipe({
  name: 'positionLabel',
  standalone: true
})
export class PositionLabelPipe implements PipeTransform {

  transform(position: Position): string {
    return POSITION_LABELS[position];
  }
}