export interface IPost {
    title: string
    content: string
    createdAt: string
    id: number
    author: IAuthor
}

export interface IAuthor {
    name: string
    id: number
    profilePicture: string
    description: string
    posts: IPost[]
}