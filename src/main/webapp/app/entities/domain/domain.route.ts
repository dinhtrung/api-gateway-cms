import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDomain, Domain } from 'app/shared/model/domain.model';
import { DomainService } from './domain.service';
import { DomainComponent } from './domain.component';
import { DomainDetailComponent } from './domain-detail.component';
import { DomainUpdateComponent } from './domain-update.component';

@Injectable({ providedIn: 'root' })
export class DomainResolve implements Resolve<IDomain> {
  constructor(private service: DomainService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDomain> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((domain: HttpResponse<Domain>) => {
          if (domain.body) {
            return of(domain.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Domain());
  }
}

export const domainRoute: Routes = [
  {
    path: '',
    component: DomainComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'cmsGatewayApp.domain.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DomainDetailComponent,
    resolve: {
      domain: DomainResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.domain.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DomainUpdateComponent,
    resolve: {
      domain: DomainResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.domain.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DomainUpdateComponent,
    resolve: {
      domain: DomainResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.domain.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
