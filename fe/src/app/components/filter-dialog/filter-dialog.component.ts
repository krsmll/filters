import {
	AfterViewInit,
	Component,
	ElementRef,
	EventEmitter,
	HostListener,
	Inject,
	Output,
	Renderer2,
} from '@angular/core';
import { IFilter } from '../../shared/filter';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogClose } from '@angular/material/dialog';
import { FilterComponent } from '../filter/filter.component';

@Component({
	selector: 'filter-dialog',
	standalone: true,
	imports: [FormsModule, MatDialogClose, FilterComponent],
	templateUrl: './filter-dialog.component.html',
	styleUrl: './filter-dialog.component.css',
})
export class FilterDialogComponent implements AfterViewInit {
	@Output() close: EventEmitter<void> = new EventEmitter<void>();
	@Output() save: EventEmitter<void> = new EventEmitter<void>();
	@Output() delete: EventEmitter<void> = new EventEmitter<void>();

	filter: IFilter;
	isResizing = false;
	resizeHandle!: HTMLElement;
	dialogContent!: HTMLElement

	constructor(
		@Inject(MAT_DIALOG_DATA)
		private data: {
			filter: IFilter;
		},
		private el: ElementRef,
		private renderer: Renderer2,
	) {
		this.filter = this.data.filter;
	}

	ngAfterViewInit() {
		this.resizeHandle = this.el.nativeElement.querySelector('.resize-handle');
		this.dialogContent = this.el.nativeElement.querySelector('.dialog-content');
		this.renderer.listen(this.resizeHandle, 'mousedown', (event: MouseEvent) =>
			this.onMouseDown(event),
		);
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

	onMouseDown(event: MouseEvent) {
		this.isResizing = true;
		event.preventDefault();
	}

	@HostListener('document:mousemove', ['$event'])
	onMouseMove(event: MouseEvent) {
		if (this.isResizing) {
			const newHeight =
				event.clientY - this.dialogContent.getBoundingClientRect().top;
			this.renderer.setStyle(this.dialogContent, 'height', `${newHeight}px`);
		}
	}

	@HostListener('document:mouseup')
	onMouseUp() {
		this.isResizing = false;
	}
}
