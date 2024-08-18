import { trigger, style, transition, animate, keyframes } from '@angular/animations';

export const shakeHero = trigger('shakeHero', [
    transition('* => *', [
        animate('0.5s', keyframes([
            style({ transform: 'translate3d(-1px, 0, 0)', offset: 0.1 }),
            style({ transform: 'translate3d(2px, 0, 0)', offset: 0.2 }),
            style({ transform: 'translate3d(-4px, 0, 0)', offset: 0.3 }),
            style({ transform: 'translate3d(4px, 0, 0)', offset: 0.4 }),
            style({ transform: 'translate3d(-4px, 0, 0)', offset: 0.5 }),
            style({ transform: 'translate3d(4px, 0, 0)', offset: 0.6 }),
            style({ transform: 'translate3d(-4px, 0, 0)', offset: 0.7 }),
            style({ transform: 'translate3d(4px, 0, 0)', offset: 0.8 }),
            style({ transform: 'translate3d(-2px, 0, 0)', offset: 0.9 }),
            style({ transform: 'translate3d(1px, 0, 0)', offset: 1.0 })
        ]))
    ])
]);

export const shakeEnemy = trigger('shakeEnemy', [
    transition('* => *', [
        animate('0.5s', keyframes([
            style({ transform: 'translate3d(-1px, 0, 0)', offset: 0.1 }),
            style({ transform: 'translate3d(2px, 0, 0)', offset: 0.2 }),
            style({ transform: 'translate3d(-4px, 0, 0)', offset: 0.3 }),
            style({ transform: 'translate3d(4px, 0, 0)', offset: 0.4 }),
            style({ transform: 'translate3d(-4px, 0, 0)', offset: 0.5 }),
            style({ transform: 'translate3d(4px, 0, 0)', offset: 0.6 }),
            style({ transform: 'translate3d(-4px, 0, 0)', offset: 0.7 }),
            style({ transform: 'translate3d(4px, 0, 0)', offset: 0.8 }),
            style({ transform: 'translate3d(-2px, 0, 0)', offset: 0.9 }),
            style({ transform: 'translate3d(1px, 0, 0)', offset: 1.0 })
        ]))
    ])
]);

export const heroHitText = trigger('heroHitText', [
    transition(':enter', [
        animate('300ms ease', keyframes([
            style({ opacity: 0, offset: 0 }),
            style({ opacity: 1, transform: 'translateY(-20px)', offset: 0.5 }),
            style({ opacity: 0, transform: 'translateY(-40px)', offset: 1.0 })
        ]))
    ]),
    transition(':leave', [
        animate('300ms ease', style({ opacity: 0 }))
    ])
]);

export const enemyHitText = trigger('enemyHitText', [
    transition(':enter', [
        animate('300ms ease', keyframes([
            style({ opacity: 0, offset: 0 }),
            style({ opacity: 1, transform: 'translateY(-20px)', offset: 0.5 }),
            style({ opacity: 0, transform: 'translateY(-40px)', offset: 1.0 })
        ]))
    ]),
    transition(':leave', [
        animate('300ms ease', style({ opacity: 0 }))
    ])
]);
