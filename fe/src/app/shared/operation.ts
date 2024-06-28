export enum Operation {
	LARGER_THAN = "LARGER_THAN",
	LESS_THAN = "LESS_THAN",
	EQUALS = "EQUALS",
	STARTS_WITH = "STARTS_WITH",
	ENDS_WITH = "ENDS_WITH",
	CONTAINS = "CONTAINS",
	FROM = "FROM",
	TO = "TO",
	ON = "ON"
}

export function operationToString(operation: Operation): string {
	return Operation[operation]
}
