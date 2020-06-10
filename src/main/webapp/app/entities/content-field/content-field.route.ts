import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IContentField, ContentField } from 'app/shared/model/content-field.model';
import { ContentFieldService } from './content-field.service';
import { ContentFieldComponent } from './content-field.component';
import { ContentFieldDetailComponent } from './content-field-detail.component';
import { ContentFieldUpdateComponent } from './content-field-update.component';

@Injectable({ providedIn: 'root' })
export class ContentFieldResolve implements Resolve<IContentField> {
  constructor(private service: ContentFieldService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContentField> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((contentField: HttpResponse<ContentField>) => {
          if (contentField.body) {
            return of(contentField.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContentField());
  }
}

export const contentFieldRoute: Routes = [
  {
    path: '',
    component: ContentFieldComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'cmsGatewayApp.contentField.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContentFieldDetailComponent,
    resolve: {
      contentField: ContentFieldResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.contentField.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContentFieldUpdateComponent,
    resolve: {
      contentField: ContentFieldResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.contentField.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContentFieldUpdateComponent,
    resolve: {
      contentField: ContentFieldResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'cmsGatewayApp.contentField.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
