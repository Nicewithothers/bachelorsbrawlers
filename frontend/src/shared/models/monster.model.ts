import { Entity } from "./entity.model";

export interface Monster extends Entity {
    damageRange: number;
    dungeonLevel: number
    motto: string
    picture: String
}
