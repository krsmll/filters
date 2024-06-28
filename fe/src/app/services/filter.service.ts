import { Injectable } from '@angular/core';
import { Config } from '../shared/config';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IFilterList } from '../shared/filter-list';
import { IFilter } from '../shared/filter';

@Injectable({
	providedIn: 'root',
})
export class FilterService {
	constructor(
		private config: Config,
		private http: HttpClient,
	) {}

	getFilters(): Observable<IFilterList> {
		return this.http.get<IFilterList>(`${this.config.API_URL}/filters`);
	}

	getFilter(id: number): Observable<IFilter> {
		return this.http.get<IFilter>(`${this.config.API_URL}/filters/${id}`);
	}

	addFilter(filter: IFilter): Observable<IFilter> {
		return this.http.post<IFilter>(
			`${this.config.API_URL}/filters`,
			filter,
		);
	}

	updateFilter(filter: IFilter): Observable<IFilter> {
		if (!filter.id) {
			throw new Error('Filter ID is required');
		}
		return this.http.put<IFilter>(`${this.config.API_URL}/filters`, filter);
	}

	deleteFilter(id: number): Observable<void> {
		return this.http.delete<void>(`${this.config.API_URL}/filters/${id}`);
	}
}
