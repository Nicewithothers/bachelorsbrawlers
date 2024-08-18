import { Entity } from "./entity.model";
import { Item } from "./item.model";

export interface Hero extends Entity {
    diligence: number;
    xp: bigint;
    maxXp: bigint;
    level: number;
    adventure: number;
    forint: number;
    crypto: number;
    boosterItem: any;
    equipmentItems: Item[];
    backpackItems: Item[];
    dungeonLevel: number;
    nextDungeonTry: Date;
    picture: string;
}
