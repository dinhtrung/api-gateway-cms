import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IContentType, ContentType } from 'app/shared/model/content-type.model';
import { ContentTypeService } from './content-type.service';
import { ContentTypeComponent } from './content-type.component';
import { ContentTypeDetailComponent } from './content-type-detail.component';
import { ContentTypeUpdateComponent } from './content-type-update.component';

@Injectable({ providedIn: 'root' })
export class ContentTypeResolve implements Resolve<IContentType> {
  constructor(private service: ContentTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContentType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((contentType: HttpResponse<ContentType>) => {
          if (contentType.body) {
            return of(contentType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContentType());
  }
}

export const contentTypeRoute: Routes = [
  {
    path: '',
    component: ContentTypeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'cmsGatewayApp.contentType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContentTypeDetailComponent,
    resolve: {
      contentType: ContentTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.contentType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContentTypeUpdateComponent,
    resolve: {
      contentType: ContentTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.contentType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContentTypeUpdateComponent,
    resolve: {
      contentType: ContentTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.contentType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
