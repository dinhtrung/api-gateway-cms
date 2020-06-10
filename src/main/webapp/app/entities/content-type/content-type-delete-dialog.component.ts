import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContentType } from 'app/shared/model/content-type.model';
import { ContentTypeService } from './content-type.service';

@Component({
  templateUrl: './content-type-delete-dialog.component.html',
})
export class ContentTypeDeleteDialogComponent {
  contentType?: IContentType;

  constructor(
    protected contentTypeService: ContentTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.contentTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('contentTypeListModification');
      this.activeModal.close();
    });
  }
}
