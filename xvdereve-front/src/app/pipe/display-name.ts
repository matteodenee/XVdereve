import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'displayName',
  standalone: true
})
export class DisplayNamePipe implements PipeTransform {

  transform(value: string): string {
    return value.replaceAll('_', ' ');
  }
}