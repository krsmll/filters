import { Operation } from "./operation";
import { ValueType } from "./value-type";

export interface ICriteria {
	id?: number;
	type: ValueType;
	operation: Operation;
	value: string;
}
