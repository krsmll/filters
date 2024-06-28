import { Injectable } from '@angular/core';
import { Config } from "../shared/config";
import { HttpClient } from "@angular/common/http";
import { map, Observable, of } from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class ConfigLoaderService {
	private config: Config;

	constructor(private http: HttpClient) {
		this.config = new Config();
	}

	getConfig(): Config {
		return this.config;
	}

	load(): Observable<Config> {
		if (Object.keys(this.config).length !== 0) {
			return of(this.config);
		}

		return this.http.get('/settings.json').pipe(
			map((data: Config) => {
				this.config = data;
				return this.config;
			}),
		);
	}
}
