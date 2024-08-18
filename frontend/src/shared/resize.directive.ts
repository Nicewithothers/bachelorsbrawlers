
import { Directive, ElementRef, Renderer2, OnInit } from '@angular/core';
import { fromEvent } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

@Directive({
    standalone: true,
    selector: '[appDynamicCanvas]'
})
export class DynamicCanvasDirective implements OnInit {
    constructor(private elementRef: ElementRef, private renderer: Renderer2) { }

    ngOnInit() {
        // Adjust canvas position and size initially and on window resize
        this.adjustCanvasPosition();
        fromEvent(window, 'resize')
            .pipe(debounceTime(200))
            .subscribe(() => this.adjustCanvasPosition());
    }

    adjustCanvasPosition() {
        const canvas = this.elementRef.nativeElement;
        const player = document.querySelector('.player');
        const enemy = document.querySelector('.enemy');

        if (player && enemy) {
            const playerPosition = player.getBoundingClientRect();
            const enemyPosition = enemy.getBoundingClientRect();

            const canvasWidth = enemyPosition.left - playerPosition.right + 80;

            // Set canvas dimensions directly as attributes
            canvas.width = canvasWidth;
        }
    }
}
