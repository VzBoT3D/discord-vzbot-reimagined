import type {IAuthor} from "~/lib/types/Blog";

export interface ITeam {
    team: IAuthor[]
    moderators: IAuthor[]
    contributor: IAuthor[]
}