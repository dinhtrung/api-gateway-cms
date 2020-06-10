import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CmsGatewaySharedModule } from 'app/shared/shared.module';
import { ContentFieldComponent } from './content-field.component';
import { ContentFieldDetailComponent } from './content-field-detail.component';
import { ContentFieldUpdateComponent } from './content-field-update.component';
import { ContentFieldDeleteDialogComponent } from './content-field-delete-dialog.component';
import { contentFieldRoute } from './content-field.route';

@NgModule({
  imports: [CmsGatewaySharedModule, RouterModule.forChild(contentFieldRoute)],
  declarations: [ContentFieldComponent, ContentFieldDetailComponent, ContentFieldUpdateComponent, ContentFieldDeleteDialogComponent],
  entryComponents: [ContentFieldDeleteDialogComponent],
})
export class CmsGatewayContentFieldModule {}
