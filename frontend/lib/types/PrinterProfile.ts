export interface IPrinterProfile {
    description: string
    kitURL?: string
    learnMoreURL: string
    id: number
    printer: IPrinter
    medias: IPrinterMedia[]
}

export interface IPrinter {
    name: string
    id: number
    profile?: IPrinterProfile[]
}

interface IPrinterMedia {
    location: string
    id: number
}