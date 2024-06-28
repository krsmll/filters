import {
	AfterViewInit,
	Component,
	ElementRef,
	EventEmitter,
	Input,
	Output,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogClose } from '@angular/material/dialog';
import { IFilter } from '../../shared/filter';
import { ValueType } from '../../shared/value-type';
import { Operation } from '../../shared/operation';
import { ICriteria } from "../../shared/criteria";
import { NumericInputDirective } from "../../directives/numeric-input.directive";

@Component({
	selector: 'filter',
	standalone: true,
	imports: [FormsModule, MatDialogClose, NumericInputDirective],
	templateUrl: './filter.component.html',
	styleUrl: './filter.component.css',
})
export class FilterComponent {
	@Output() close: EventEmitter<void> = new EventEmitter<void>();
	@Output() save: EventEmitter<void> = new EventEmitter<void>();
	@Output() delete: EventEmitter<void> = new EventEmitter<void>();

	@Input() filter!: IFilter;

	protected readonly ValueType = ValueType;
	protected readonly Object = Object;

	constructor(private elementRef: ElementRef) {}

	addCriterion() {
		this.filter.criteria.push({
			type: ValueType.AMOUNT,
			operation: Operation.EQUALS,
			value: '',
		});
	}

	getOperationsBasedOnValueType(valueType: ValueType) {
		switch (valueType) {
			case ValueType.AMOUNT:
				return [
					Operation.EQUALS,
					Operation.LARGER_THAN,
					Operation.LESS_THAN,
				];
			case ValueType.DATE:
				return [Operation.ON, Operation.FROM, Operation.TO];
			case ValueType.TEXT:
				return [Operation.EQUALS, Operation.CONTAINS];
			default:
				return [];
		}
	}

	removeCriterion(index: number) {
		this.filter.criteria.splice(index, 1);
	}

	saveFilter() {
		this.save.emit();
	}

	closeDialog() {
		this.close.emit();
	}

	deleteFilter() {
		this.delete.emit();
	}

	onNumberInputChange(event: Event) {
		const target = event.target as HTMLInputElement;
		target.value = target.value.replace(/[^0-9.]/g, '');
	}

	private getNumericalFields(): NodeListOf<HTMLInputElement> {
		return this.elementRef.nativeElement.querySelectorAll('.numeric-input');
	}
}
