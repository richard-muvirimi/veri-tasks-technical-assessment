import {inject} from '@angular/core';
import {ActivatedRouteSnapshot, Route, RouterStateSnapshot, UrlSegment, UrlTree, Router} from '@angular/router';
import {Auth as AuthService} from '../services/auth.service';

export function IsNotLoggedGuard(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    const auth: AuthService = inject(AuthService);
    const router: Router = inject(Router);

    const authenticated = auth.isAuthenticated();

    if (authenticated) {
        return router.createUrlTree(['/dashboard']);
    }

    return true;
}

export function IsNotLoggedMatch(route: Route, segments: UrlSegment[]): boolean | UrlTree {
    const auth: AuthService = inject(AuthService);
    const router: Router = inject(Router);

    const authenticated = auth.isAuthenticated();

    if (authenticated) {
        return router.createUrlTree(['/dashboard']);
    }

    return true;
}

export function IsLoggedGuard(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    const auth: AuthService = inject(AuthService);
    const router: Router = inject(Router);

    const authenticated = auth.isAuthenticated();

    if (!authenticated) {
        return router.createUrlTree(['/login']);
    }

    return true;
}

export function IsLoggedMatch(route: Route, segments: UrlSegment[]): boolean | UrlTree {
    const auth: AuthService = inject(AuthService);
    const router: Router = inject(Router);

    const authenticated = auth.isAuthenticated();

    if (!authenticated) {
        return router.createUrlTree(['/login']);
    }

    return true;
}
