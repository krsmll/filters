import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FilterListComponent } from "./components/filter-list/filter-list.component";
import { FilterDialogComponent } from "./components/filter-dialog/filter-dialog.component";

@Component({
	selector: 'app-root',
	standalone: true,
	imports: [RouterOutlet, FilterListComponent, FilterDialogComponent],
	templateUrl: './app.component.html',
	styleUrl: './app.component.css',
})
export class AppComponent {
	title = 'filters';
}
