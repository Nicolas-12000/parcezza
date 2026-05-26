import { ServerRoute, RenderMode } from '@angular/ssr';

// Explicit server routing entries so server-side renderer recognizes application
// routes during build. We avoid setting a renderMode here to let the server
// decide per-request behavior and to prevent prerender attempts for param routes.
export const serverRoutes: ServerRoute[] = [
	{ path: '', renderMode: RenderMode.Server },
	{ path: 'catalog', renderMode: RenderMode.Server },
	{ path: 'product/:id', renderMode: RenderMode.Server },
	{ path: 'order/:id', renderMode: RenderMode.Server },
	{ path: 'login', renderMode: RenderMode.Server },
	{ path: 'register', renderMode: RenderMode.Server },
	{ path: 'checkout', renderMode: RenderMode.Server },
	{ path: 'profile', renderMode: RenderMode.Server },
	{ path: 'admin/returns', renderMode: RenderMode.Server },
	{ path: 'admin/shipments', renderMode: RenderMode.Server },
	{ path: '**', renderMode: RenderMode.Server }
];
