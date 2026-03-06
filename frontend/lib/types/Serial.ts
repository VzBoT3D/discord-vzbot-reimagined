export interface ISerial {
    id: number,
    mediaURL: string,
    description: string
    country: string
    serialID: number
    printer: {
        name: string
    }
    attributes?: {
        latitude: number,
        longitude: number
    }
    createdAt: string
}