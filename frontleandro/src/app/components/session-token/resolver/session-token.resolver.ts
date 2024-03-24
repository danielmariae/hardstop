import { Injectable } from '@angular/core';
import { Router, Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { SessionTokenService } from '../../../services/session-token.service'; 

@Injectable({
  providedIn: 'root'
})
export class AuthResolver implements Resolve<boolean> {
  constructor(private sessionTokenService: SessionTokenService, private router: Router) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.sessionTokenService.isAuthenticated()
      .pipe(
        map(isAuthenticated => {
          if (isAuthenticated) {
            return true;
          } else {
            // Se o usuário não estiver autenticado, redirecione-o para a página de login
            this.router.navigate(['/login']);
            return false;
          }
        }),
        catchError(error => {
          // Se ocorrer algum erro, também redirecione-o para a página de login
          this.router.navigate(['/login']);
          return of(false);
        })
      );
  }
}
