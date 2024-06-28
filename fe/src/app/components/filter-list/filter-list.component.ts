import { Component, OnInit } from '@angular/core';
import { IFilter } from '../../shared/filter';
import { FilterService } from '../../services/filter.service';
import { IFilterList } from '../../shared/filter-list';
import { RouterLink } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { FilterDialogComponent } from '../filter-dialog/filter-dialog.component';
import { FormsModule } from '@angular/forms';
import { FilterComponent } from '../filter/filter.component';
import { ValueType } from '../../shared/value-type';
import { Operation } from '../../shared/operation';

@Component({
	selector: 'filter-list',
	standalone: true,
	imports: [RouterLink, FilterDialogComponent, FormsModule, FilterComponent],
	templateUrl: './filter-list.component.html',
	styleUrl: './filter-list.component.css',
})
export class FilterListComponent implements OnInit {
	filters: IFilter[] = [];
	errors: string[] = [];
	modalMode: boolean = true;
	currentlyOpenFilter: IFilter | null = null;

	constructor(
		private filterService: FilterService,
		private dialog: MatDialog,
	) {}

	ngOnInit(): void {
		this.getFilters();
	}

	getFilters(): void {
		this.filterService.getFilters().subscribe({
			next: (filters: IFilterList) => (this.filters = filters.filters),
			error: (error: Error) => this.errors.push(error.message),
		});
	}

	addFilter(): void {
		const newFilter: IFilter = {
			name: 'New Filter',
			criteria: [
				{
					type: ValueType.AMOUNT,
					operation: Operation.EQUALS,
					value: '',
				},
			],
		};
		this.filters = [...this.filters, newFilter];
		this.openFilterObj(newFilter);
	}

	openFilter(index: number): void {
		this.nonModalCloseFilter();
		this.currentlyOpenFilter = structuredClone(this.filters[index]);
		if (this.modalMode) {
			this.openDialog();
		}
	}

	openFilterObj(filter: IFilter): void {
		this.nonModalCloseFilter();
		this.currentlyOpenFilter = structuredClone(filter);
		if (this.modalMode) {
			this.openDialog();
		}
	}

	nonModalCloseFilter(): void {
		if (!this.filters[this.filters.length - 1].id) {
			this.filters.pop();
		}
		this.currentlyOpenFilter = null;
	}

	saveFilter(filter: IFilter): void {
		if (!filter.id) {
			this.filterService.addFilter(filter).subscribe({
				next: (filters: IFilter) => {
					this.filters.push(filters)
					this.currentlyOpenFilter = null;
				},
				error: (error: Error) => this.errors.push(error.message),
			});
		} else {
			this.filterService.updateFilter(filter).subscribe({
				next: (filter: IFilter) => {
					this.filters[this.getIndexOfFilter(filter)] = filter;
					this.currentlyOpenFilter = null;
				},
				error: (error: Error) => this.errors.push(error.message),
			});
		}
	}

	deleteFilter() {
		if (!this.currentlyOpenFilter || !this.currentlyOpenFilter.id) {
			throw Error('No filter selected');
		}
		this.filterService.deleteFilter(this.currentlyOpenFilter.id!).subscribe();
		this.filters.splice(this.getIndexOfFilter(this.currentlyOpenFilter), 1);
		this.currentlyOpenFilter = null;
	}

	onModalModeChange() {
		this.currentlyOpenFilter = null;
	}

	private openDialog(): void {
		const component = this.dialog.open(FilterDialogComponent, {
			data: { filter: this.currentlyOpenFilter },
			width: '500px',
			maxWidth: '500px',
			minWidth: '500px',
			panelClass: 'vertically-resizable-dialog',
		});

		component.componentInstance.save.subscribe(() => {
			this.saveFilter(component.componentInstance.filter);
			component.close();
		});

		component.componentInstance.close.subscribe(() => {
			this.currentlyOpenFilter = null;
			component.close();
		});

		component.componentInstance.delete.subscribe(() => {
			this.deleteFilter();
			component.close();
		});
	}

	private getIndexOfFilter(filter: IFilter): number {
		return this.filters.findIndex((f) => f.id === filter.id);
	}
}
