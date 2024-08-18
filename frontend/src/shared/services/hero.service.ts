import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Hero} from '../models/hero.model';
import {BehaviorSubject, catchError, map, Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {AuthService} from './auth.service';
import {MessageService} from 'primeng/api';
import {Farao, FaraoDTO} from '../models/farao.model';
import { Item } from '../models/item.model';

@Injectable({ providedIn: 'root' })
export class HeroService {
  private heroSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  public hero$: Observable<Hero> = this.heroSubject.asObservable();

  constructor(private http: HttpClient, private auth: AuthService, private messageService: MessageService) { }

  getAll(): Observable<Hero[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'heroes', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a hallgatók lekérdezésekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      })
    )
  }

  getHero(){
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'heroes/getHero', { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          const hero = response.body as Hero;
          hero.picture = "data:image/png;base64," + hero.picture;
          hero.equipmentItems = this.sortItemsByCategory(hero.equipmentItems);
          this.heroSubject.next(hero);
          return {hero, picture: hero.picture};
        } else {
          throw new Error('Hiba történt a hallgató lekérdezésekor');
        }
      }),
      catchError((error) => {
        // this.messageService.add({
        //   severity: 'error',
        //   summary: 'Nem található karakter a profilodhoz!'
        // });
        return of(null);
      })
    )
  }

  sortItemsByCategory(items: any[]): any[] {
    const sortedItems = new Array(7).fill(null);
    const categories = ['HEAD', 'UPPER', 'LOWER', 'BELT', 'RING', 'SPECIAL', 'WEAPON'];

    items.forEach(item => {
      const index = categories.indexOf(item.itemCategory);
      if (index !== -1) {
        sortedItems[index] = item;
      }
    });

    return sortedItems;
  }


  create(hero: Hero): Observable<Hero | null> {
    const body = {
      name: hero.name,
      maxHp: hero.maxHp,
      intelligence: hero.intelligence,
      luck: hero.luck,
      endurance: hero.endurance,
      dexterity: hero.dexterity,
      diligence: hero.diligence,
      adventure: hero.adventure,
      forint: hero.forint,
      crypto: hero.crypto,
      xp: Number(hero.xp),
      maxXp: Number(hero.maxXp),
      level: Number(hero.level),
      boosterItem: null,
      equipmentItems: null,
      backpackItems: null,
      profileName: sessionStorage.getItem('profileName') as string,
      dungeonLevel: 1,
      nextDungeonTry: new Date().toISOString(),
      image: hero.image
    };

    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'heroes/createHero', body, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          const hero = response.body as Hero;
          this.heroSubject.next(hero);
          return hero;
        } else {
          throw new Error('Hiba történt a hallgató létrehozásakor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  update(hero: Hero): Observable<Hero | null> {
    const body = {
      name: hero.name,
      maxHp: hero.maxHp,
      intelligence: hero.intelligence,
      luck: hero.luck,
      endurance: hero.endurance,
      dexterity: hero.dexterity,
      diligence: hero.diligence,
      forint: hero.forint,
      crypto: hero.crypto,
      boosterItem: JSON.stringify(hero.boosterItem),
      equipmentItems: JSON.stringify(hero.equipmentItems),
      backpackItems: JSON.stringify(hero.backpackItems),
      // image: JSON.stringify(hero.image)
    }

    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.put(environment.API_URL + 'heroes/' + hero.id, body, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          const hero = response.body as Hero;
          this.heroSubject.next(hero);
          return hero;
        } else {
          throw new Error('Hiba történt a hallgató módosításakor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  delete(id: string): Observable<boolean> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.delete(environment.API_URL + 'heroes/' + id, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        if (response.ok) {
          this.heroSubject.next(null);
          return true;
        } else {
          throw new Error('Hiba történt a hallgató törlésekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(false);
      }),
    );
  }

  equipItem(itemName: string) {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'heroes/equip', { }, { headers, observe: 'response', params: { itemName } }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          const hero = response.body as Hero;
          hero.equipmentItems = this.sortItemsByCategory(hero.equipmentItems);
          return {equipmentItems: hero.equipmentItems, backpackItems: hero.backpackItems};
        } else {
          throw new Error('Hiba történt a tárgy felvételekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  unEquipItem(itemName: string) {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'heroes/unequip', { }, { headers, observe: 'response', params: { itemName } }).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          const hero = response.body as Hero;
          hero.equipmentItems = this.sortItemsByCategory(hero.equipmentItems);
          return {equipmentItems: hero.equipmentItems, backpackItems: hero.backpackItems};
        } else {
          throw new Error('Hiba történt a tárgy levételekor');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  increaseHeroStat(statName: string) {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'heroes/' + statName + '/increase', {},
      { headers, observe: 'response' }).pipe(
        map((response: any) => {
          if (response.ok && response.body) {
            const hero = response.body as Hero;
            switch (statName) {
              case "endurance": return hero.endurance;
              case "diligence": return hero.diligence;
              case "intelligence": return hero.intelligence;
              case "dexterity": return hero.dexterity;
              case "luck": return hero.luck;
              default: throw new Error("Nem létező stat!");
            }
          } else {
            throw new Error('Hiba történt a hallgató statjának növelésekor');
          }
        }),
        catchError((error) => {
          console.error(error);
          return of(null);
        }),
      );
  }

  getNextStatIncreaseCost(statName: string): Observable<number | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'heroes/' + statName + '/nextCost',
      { headers, observe: 'response', responseType: 'text' }).pipe(
        map((response: any) => {
          if (response.ok && response.body) {
            return response.body;
          } else {
            throw new Error('Hiba történt a statnövelés lekérdezésekor');
          }
        }),
        catchError((error) => {
          console.error(error);
          return of(null);
        }),
      );
  }

  getHeroesByXpDesc() {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'heroes/leaderboard', {headers, observe: 'response'}).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body.map((hero: Hero) => {
            const imageURL = "data:image/png;base64," + hero.picture;
            hero.equipmentItems = this.sortItemsByCategory(hero.equipmentItems);
            return {...hero, picture: imageURL};
          });
        } else {
          throw new Error('Hiba történt a ranglista lekérdezésekor!');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

  startGambling(): Observable<Farao | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'heroes/startGambling', {},
      { headers, observe: 'response' }).pipe(
        map((response: any) => {
          if (response.ok && response.body) {
            return this.convertFaraoDTOToFarao(response.body);
          } else {
            throw new Error('Hiba történt a hallgató szerencse játékozásakor');
          }
        }),
        catchError((error) => {
          console.error(error);
          return of(null);
        }),
      );
  }

  private convertFaraoDTOToFarao(dto: FaraoDTO): Farao {
    return {
      serial: dto.serial,
      playerNumbers: dto.playerNumbers,
      pyramid1: {
        level1: Array.of(dto.pyramid1Numbers[0]),
        level2: dto.pyramid1Numbers.slice(1, 3),
        level3: dto.pyramid1Numbers.slice(3, 6),
        level4: dto.pyramid1Numbers.slice(6, 10),
        level5: dto.pyramid1Numbers.slice(10, 15),
      },
      pyramid2: {
        level1: Array.of(dto.pyramid2Numbers[0]),
        level2: dto.pyramid2Numbers.slice(1, 3),
        level3: dto.pyramid2Numbers.slice(3, 6),
        level4: dto.pyramid2Numbers.slice(6, 10),
      },
      pyramid3: {
        level1: Array.of(dto.pyramid3Numbers[0]),
        level2: dto.pyramid3Numbers.slice(1, 3),
        level3: dto.pyramid3Numbers.slice(3, 6),
        level4: dto.pyramid3Numbers.slice(6, 10),
        level5: dto.pyramid3Numbers.slice(10, 15),
        level6: dto.pyramid3Numbers.slice(15, 21),
      },
      allWinnings: dto.allWinnings
    }
  }

  getLastWin(): Observable<number | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.get(environment.API_URL + 'heroes/lastWin',
      { headers, observe: 'response', responseType: 'text' }).pipe(
        map((response: any) => {
          if (response.ok && response.body) {
            return response.body;
          } else {
            throw new Error('Hiba történt a lekérdezéskor');
          }
        }),
        catchError((error) => {
          console.error(error);
          return of(null);
        }),
      );
  }

  claimVictory(): Observable<string | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    return this.http.post(environment.API_URL + 'heroes/claimVictory', {},
      { headers, observe: 'response', responseType: 'text' }).pipe(
        map((response: any) => {
          if (response.ok && response.body) {
            return response.body;
          } else {
            throw new Error('Hiba történt a nyeremény átvételekor');
          }
        }),
        catchError((error) => {
          console.error(error);
          return of(null);
        }),
      );
  }

  getHeroesByIds(ids: string[]): Observable<Hero[] | null> {
    const token = sessionStorage.getItem('token') as string;
    const headers = new HttpHeaders().set('Authorization', token);
    let params = new HttpParams();
    ids.forEach(id => {
      params = params.append('heroNames', id.toString());
    });
    return this.http.get(environment.API_URL + 'heroes/getHeroesByIds', {headers, observe: 'response', params}).pipe(
      map((response: any) => {
        if (response.ok && response.body) {
          return response.body;
        } else {
          throw new Error('Hiba történt a ranglista lekérdezésekor!');
        }
      }),
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
    );
  }

}
