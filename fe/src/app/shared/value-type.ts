export enum ValueType {
	AMOUNT = "AMOUNT",
	TEXT = "TEXT",
	DATE = "DATE"
}

export function valueTypeToString(type: ValueType): string {
	return ValueType[type]
}
