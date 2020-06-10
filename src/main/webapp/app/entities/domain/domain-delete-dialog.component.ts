import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDomain } from 'app/shared/model/domain.model';
import { DomainService } from './domain.service';

@Component({
  templateUrl: './domain-delete-dialog.component.html',
})
export class DomainDeleteDialogComponent {
  domain?: IDomain;

  constructor(protected domainService: DomainService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.domainService.delete(id).subscribe(() => {
      this.eventManager.broadcast('domainListModification');
      this.activeModal.close();
    });
  }
}
