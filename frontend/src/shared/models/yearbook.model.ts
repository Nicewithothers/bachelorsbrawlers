import { Entity } from "./entity.model";

export interface YearBook extends Entity {
    heroLevel: number;
    defeatTime: string;
    heroId: string;
    picture: string;
}
