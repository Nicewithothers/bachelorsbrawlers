export interface FaraoDTO {
    serial: string,
    playerNumbers: number[],
    pyramid1Numbers: number[],
    pyramid2Numbers: number[],
    pyramid3Numbers: number[],
    allWinnings: number
}

export interface Farao {
    serial: string,
    playerNumbers: number[],
    pyramid1: {
        [key: string]: number[]
    }
    pyramid2: {
        [key: string]: number[]
    }
    pyramid3: {
        [key: string]: number[]
    }
    allWinnings: number
}