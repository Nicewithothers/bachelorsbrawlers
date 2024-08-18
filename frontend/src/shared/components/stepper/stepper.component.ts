import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-stepper',
  standalone: true,
  imports: [],
  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss'
})
export class StepperComponent {
  @Input() label!: string;
  @Input() value!: number;
  @Output() valueChange = new EventEmitter<number>();

  increment() {
    this.value = Math.min(++this.value, 4);
    this.valueChange.emit(this.value);
  }

  decrement() {
    this.value = Math.max(--this.value, 1);
    this.valueChange.emit(this.value);
  }
}
