import {
	APP_INITIALIZER,
	ApplicationConfig,
	provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { ConfigLoaderService } from './services/config-loader.service';
import { provideHttpClient } from '@angular/common/http';
import { Config } from './shared/config';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

function configLoader(configLoader: ConfigLoaderService) {
	return () => configLoader.load();
}

function configFactory(configLoader: ConfigLoaderService) {
	return configLoader.getConfig();
}

export const appConfig: ApplicationConfig = {
	providers: [
		{
			provide: APP_INITIALIZER,
			useFactory: configLoader,
			deps: [ConfigLoaderService],
			multi: true,
		},
		{
			provide: Config,
			useFactory: configFactory,
			deps: [ConfigLoaderService],
		},
		provideHttpClient(),
		provideZoneChangeDetection({ eventCoalescing: true }),
		provideRouter(routes), provideAnimationsAsync(),
	],
};
