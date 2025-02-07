import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterListComponent } from './filter-list.component';

describe('FilterListComponent', () => {
	let component: FilterListComponent;
	let fixture: ComponentFixture<FilterListComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [FilterListComponent],
		}).compileComponents();

		fixture = TestBed.createComponent(FilterListComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
