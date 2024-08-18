import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Client, Message} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { environment } from '../../environments/environment';
import { Attack } from '../models/attack.model';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  stompClient: Client;
  forintSubject: BehaviorSubject<any> = new BehaviorSubject<number|null>(null);
  cryptoSubject: BehaviorSubject<any> = new BehaviorSubject<number|null>(null);
  healthSubject: BehaviorSubject<any> = new BehaviorSubject<number|null>(null);
  scheduleSubject: BehaviorSubject<any> = new BehaviorSubject<Attack|null>(null);
  public forint$ = this.forintSubject.asObservable();
  public crypto$ = this.cryptoSubject.asObservable();
  public health$ = this.healthSubject.asObservable();
  public schedule$ = this.scheduleSubject.asObservable();

  constructor() {
    this.stompClient = this.initialize()
    this.stompClient.onConnect = () => {
      this.subscribeToForintWS();
      this.subscribeToCryptoWS();
      this.subscribeToHealthWS();
      this.subscribeToScheduleWS();
    }
    this.stompClient.activate();
  }

  private subscribeToForintWS(): void {
    this.stompClient.subscribe('/topic/forint-updates', (message: Message) => {
      const forintChange = parseInt(message.body);
      this.forintSubject.next({ type: 'forint', value: forintChange });
    });
  }

  private subscribeToCryptoWS(): void {
    this.stompClient.subscribe('/topic/crypto-updates', (message: Message) => {
      const cryptoChange = parseInt(message.body);
      this.cryptoSubject.next({ type: 'crypto', value: cryptoChange });
    });
  }

  private subscribeToHealthWS(): void {
    this.stompClient.subscribe('/topic/health-updates', (message: Message) => {
      const healthChange = parseInt(message.body);
      this.healthSubject.next({ type: 'health', value: healthChange });
    });
  }

  public subscribeToScheduleWS(): void {
    this.stompClient.subscribe('/topic/schedule', (message: Message) => {
      const scheduleChange = JSON.parse(message.body) as Attack;
      this.scheduleSubject.next({ type: 'schedule', value: scheduleChange });
    });
  }

  private initialize(): Client {
    const socketUrl = environment.WS_URL;
    return new Client({
      brokerURL: socketUrl,
      connectHeaders: {},
      debug: function (str) {
        console.debug(str);
      },
      reconnectDelay: 5000,
      webSocketFactory: function () {
        return new SockJS(socketUrl);
      },
      onConnect: function () {
        // console.log('Connected to WebSocket');
      },
    });
  };
}
