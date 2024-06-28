import { Directive, HostListener } from '@angular/core';

@Directive({
	selector: '[numericInput]',
	standalone: true,
})
export class NumericInputDirective {

	private allowedKeys: string[] = ['Backspace', 'ArrowLeft', 'ArrowRight', 'Tab'];


	@HostListener('keypress', ['$event'])
	onKeyPress(event: KeyboardEvent): void {
		if (!/\d/.test(event.key) && !this.allowedKeys.includes(event.key)) {
			event.preventDefault();
		}
	}

	@HostListener('paste', ['$event'])
	onPaste(event: ClipboardEvent): void {
		const pasteData = event.clipboardData?.getData('text') || '';
		if (!/^\d+$/.test(pasteData)) {
			event.preventDefault();
		}
	}
}
