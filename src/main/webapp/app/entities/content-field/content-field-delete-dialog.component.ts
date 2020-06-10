import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContentField } from 'app/shared/model/content-field.model';
import { ContentFieldService } from './content-field.service';

@Component({
  templateUrl: './content-field-delete-dialog.component.html',
})
export class ContentFieldDeleteDialogComponent {
  contentField?: IContentField;

  constructor(
    protected contentFieldService: ContentFieldService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.contentFieldService.delete(id).subscribe(() => {
      this.eventManager.broadcast('contentFieldListModification');
      this.activeModal.close();
    });
  }
}
