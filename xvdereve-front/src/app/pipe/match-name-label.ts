import {
  Pipe,
  PipeTransform
} from '@angular/core';

import {
  MatchName,
  MATCH_NAME_LABELS
} from '../type/match-name';

@Pipe({
  name: 'matchNameLabel',
  standalone: true
})
export class MatchNameLabelPipe
  implements PipeTransform {

  transform(matchName: MatchName): string {
    return MATCH_NAME_LABELS[matchName];
  }
}