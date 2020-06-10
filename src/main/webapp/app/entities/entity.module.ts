import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'domain',
        loadChildren: () => import('./domain/domain.module').then(m => m.CmsGatewayDomainModule),
      },
      {
        path: 'content-type',
        loadChildren: () => import('./content-type/content-type.module').then(m => m.CmsGatewayContentTypeModule),
      },
      {
        path: 'content-field',
        loadChildren: () => import('./content-field/content-field.module').then(m => m.CmsGatewayContentFieldModule),
      },
      {
        path: 'content',
        loadChildren: () => import('./content/content.module').then(m => m.CmsGatewayContentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class CmsGatewayEntityModule {}
