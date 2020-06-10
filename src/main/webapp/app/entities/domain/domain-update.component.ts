import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IDomain, Domain } from 'app/shared/model/domain.model';
import { DomainService } from './domain.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-domain-update',
  templateUrl: './domain-update.component.html',
})
export class DomainUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    slug: [null, [Validators.required, Validators.maxLength(40)]],
    state: [null, [Validators.required]],
    description: [],
    createdAt: [],
    updatedAt: [],
    createdBy: [],
    updatedBy: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected domainService: DomainService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domain }) => {
      if (!domain.id) {
        const today = moment().startOf('day');
        domain.createdAt = today;
        domain.updatedAt = today;
      }

      this.updateForm(domain);
    });
  }

  updateForm(domain: IDomain): void {
    this.editForm.patchValue({
      id: domain.id,
      name: domain.name,
      slug: domain.slug,
      state: domain.state,
      description: domain.description,
      createdAt: domain.createdAt ? domain.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: domain.updatedAt ? domain.updatedAt.format(DATE_TIME_FORMAT) : null,
      createdBy: domain.createdBy,
      updatedBy: domain.updatedBy,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('cmsGatewayApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const domain = this.createFromForm();
    if (domain.id !== undefined) {
      this.subscribeToSaveResponse(this.domainService.update(domain));
    } else {
      this.subscribeToSaveResponse(this.domainService.create(domain));
    }
  }

  private createFromForm(): IDomain {
    return {
      ...new Domain(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      slug: this.editForm.get(['slug'])!.value,
      state: this.editForm.get(['state'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      updatedBy: this.editForm.get(['updatedBy'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomain>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
