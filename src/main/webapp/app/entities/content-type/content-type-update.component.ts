import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IContentType, ContentType } from 'app/shared/model/content-type.model';
import { ContentTypeService } from './content-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-content-type-update',
  templateUrl: './content-type-update.component.html',
})
export class ContentTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
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
    protected contentTypeService: ContentTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contentType }) => {
      if (!contentType.id) {
        const today = moment().startOf('day');
        contentType.createdAt = today;
        contentType.updatedAt = today;
      }

      this.updateForm(contentType);
    });
  }

  updateForm(contentType: IContentType): void {
    this.editForm.patchValue({
      id: contentType.id,
      name: contentType.name,
      slug: contentType.slug,
      state: contentType.state,
      description: contentType.description,
      createdAt: contentType.createdAt ? contentType.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: contentType.updatedAt ? contentType.updatedAt.format(DATE_TIME_FORMAT) : null,
      createdBy: contentType.createdBy,
      updatedBy: contentType.updatedBy,
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
    const contentType = this.createFromForm();
    if (contentType.id !== undefined) {
      this.subscribeToSaveResponse(this.contentTypeService.update(contentType));
    } else {
      this.subscribeToSaveResponse(this.contentTypeService.create(contentType));
    }
  }

  private createFromForm(): IContentType {
    return {
      ...new ContentType(),
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContentType>>): void {
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
