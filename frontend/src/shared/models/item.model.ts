export interface Item {
    id: bigint,
    itemCategory: string,
    name: string,
    rarity: string,
    price: number,
    damageRange: number,
    fileName: string,
    armor: number,
    attributeModifiers: AttributeModfier[],
    picture: String,
    imageUrl: String
}

export interface AttributeModfier {
    id: bigint,
    maxHpModifier: number;
    intelligenceModifier: number;
    luckModifier: number;
    enduranceModifier: number;
    dexterityModifier: number;
}