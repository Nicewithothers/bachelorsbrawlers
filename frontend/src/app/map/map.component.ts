import { Component, NgZone, ViewEncapsulation } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MapOptions, Marker, divIcon, latLng, latLngBounds, marker, tileLayer } from 'leaflet';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [LeafletModule, RouterModule],
  templateUrl: './map.component.html',
  styleUrl: './map.component.scss',
  encapsulation: ViewEncapsulation.None,
})
export class MapComponent {
  options: MapOptions = {
    layers: [
      tileLayer('https://{s}.google.com/vt/lyrs=s&x={x}&y={y}&z={z}', {
        subdomains: ['mt0', 'mt1', 'mt2', 'mt3'],
        maxZoom: 17,
        minZoom: 15,
        attribution: 'Google Maps',
      }),
    ],
    zoom: 16,
    center: [46.251542568672384, 20.146727639801824],
    maxBounds: latLngBounds(
      latLng(46.234448, 20.124015),
      latLng(46.278720, 20.186500)
    )
  };

  markers: Marker[] = [];

  constructor(
    private router: Router,
    private ngZone: NgZone,
    private soundService: SoundService,
  ) {}

  ngOnInit(): void {
    window.dispatchEvent(new Event('resize'));
    this.options.layers?.push(
      marker([46.25709056339844, 20.140673679569595], {
        icon: divIcon({
          html: `<img class="image" alt="Mars-tér" src="../../assets/images/marster.webp"/>
                   <div class="tooltip">Mars-tér</div>`,
          className: 'custom-marker',
          iconSize: [64, 64],
          iconAnchor: [16, 32],
        }),
      }).on('click', () =>
        this.ngZone.run(() => {
          this.router.navigateByUrl('/tavern');
          this.onButtonClicked('map_button.mp3');
        }),
      ),
    );
    this.options.layers?.push(
      marker([46.252027, 20.139124], {
        icon: divIcon({
          html: `<img class="image" alt="Négylépcsős söröző" src="../../assets/images/negylepcsos.webp"/>
                   <div class="tooltip">Négylépcsős söröző</div>`,
          className: 'custom-marker',
          iconSize: [64, 64],
          iconAnchor: [16, 32],
        }),
      }).on('click', () =>
        this.ngZone.run(() => {
          this.router.navigateByUrl('/arena');
          this.onButtonClicked('map_button.mp3');
        })
      ),
    );
    this.options.layers?.push(
      marker([46.24697477023571, 20.146933912202137], {
        icon: divIcon({
          html: `<img class="image" alt="Vizsgák" src="../../assets/images/irinyi.webp"/>
                   <div class="tooltip">Vizsgák</div>`,
          className: 'custom-marker',
          iconSize: [64, 64],
          iconAnchor: [16, 32],
        }),
      }).on('click', () =>
        this.ngZone.run(() => {
          this.router.navigateByUrl('/kazamata');
          this.onButtonClicked('map_button.mp3');
        }),
      ),
    );
    this.options.layers?.push(
      marker([46.24881783679295, 20.146568693692718], {
        icon: divIcon({
          html: `<img class="image" alt="Dohánybolt" src="../../assets/images/dohi.webp"/>
                   <div class="tooltip">Nemzeti Dohánybolt</div>`,
          className: 'custom-marker',
          iconSize: [64, 64],
          iconAnchor: [16, 32],
        }),
      }).on('click', () =>
        this.ngZone.run(() => {
          this.router.navigateByUrl('/tobaccoshop');
          this.onButtonClicked('map_button.mp3');
        }),
      ),
    );
    this.options.layers?.push(
      marker([46.25472604857611, 20.138966055147577], {
        icon: divIcon({
          html: `<img class="image" alt="Árkád" src="../../assets/images/arkad.webp"/>
                   <div class="tooltip">Árkád</div>`,
          className: 'custom-marker',
          iconSize: [64, 64],
          iconAnchor: [16, 32],
        }),
      }).on('click', () =>
        this.ngZone.run(() => {
          this.router.navigateByUrl('/weaponshop');
          this.onButtonClicked('map_button.mp3');
        }),
      ),
    );
    this.options.layers?.push(
      marker([46.2552457, 20.1469535], {
        icon: divIcon({
          html: `<img class="image" alt="Lottózó" src="../../assets/images/lottozo.webp"/>
                   <div class="tooltip">Szerencsejáték</div>`,
          className: 'custom-marker',
          iconSize: [64, 64],
          iconAnchor: [16, 32],
        }),
      }).on('click', () =>
        this.ngZone.run(() => {
          this.router.navigateByUrl('/gambling');
          this.onButtonClicked('map_button.mp3');
        }),
      ),
    );
    this.options.layers?.push(
      marker([46.2499529, 20.1438351], {
        icon: divIcon({
          html: `<img class="image" alt="Egyetem" src="../../assets/images/egyetem.webp"/>
                   <div class="tooltip">Ranglista</div>`,
          className: 'custom-marker',
          iconSize: [64, 64],
          iconAnchor: [16, 32],
        }),
      }).on('click', () =>
        this.ngZone.run(() => {
          this.router.navigateByUrl('/leaderboard');
          this.onButtonClicked('map_button.mp3');
        }),
      ),
    );
    this.options.layers?.push(
      marker([46.252561, 20.149511], {
        icon: divIcon({
          html: `<img class="image" alt="Posta" src="../../assets/images/posta.webp"/>
                   <div class="tooltip">Posta</div>`,
          className: 'custom-marker',
          iconSize: [64, 64],
          iconAnchor: [16, 32],
        }),
      }).on('click', () =>
        this.ngZone.run(() => {
          this.router.navigateByUrl('/mail');
          this.onButtonClicked('map_button.mp3');
        }),
      ),
    );
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
