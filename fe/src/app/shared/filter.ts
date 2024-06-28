import { ICriteria } from "./criteria";

export interface IFilter {
	id?: number;
	name: string;
	criteria: ICriteria[]
}
