import { inject } from '@angular/core';
import { HttpRequest, HttpEvent, HttpHandlerFn } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from '../services/auth.service';

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  const auth = inject(Auth);
  const isAuthEndpoint = req.url.includes('/auth/register') || req.url.includes('/auth/login');
  if (isAuthEndpoint) {
    return next(req);
  }

  const token = auth.getToken();
  if (token && req.url.includes('/api/')) {
    const authReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    return next(authReq);
  }

  return next(req);
}
