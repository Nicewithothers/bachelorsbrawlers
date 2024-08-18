import { Entity } from './entity.model';
import { Item } from './item.model';
import { Hero } from './hero.model';

export interface Quest extends Entity {
  reward: Item;
  xp: number;
  monsterType: string;
  monsterName: string;
  cost: number;
  hero: Hero;
  image: string;
}
