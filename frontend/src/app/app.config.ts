import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations'

import { routes } from './app.routes';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MessageService } from 'primeng/api';
import { RiveCanvas, RiveModule, RiveService } from 'ng-rive';

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(LeafletModule),
    MessageService,
    provideAnimations(),
    provideRouter(routes), 
    provideHttpClient(),
    importProvidersFrom(RiveModule),
  ]
};
