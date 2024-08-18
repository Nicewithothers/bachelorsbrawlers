import { FormControl, FormGroup, Validators } from '@angular/forms';

export function getLogInForm() {
  return new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
  });
}
