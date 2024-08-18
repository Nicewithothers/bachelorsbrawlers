import { FormControl, FormGroup, Validators } from '@angular/forms';

export function getHeroFrom() {
  return new FormGroup({
    heroName: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
  });
}
