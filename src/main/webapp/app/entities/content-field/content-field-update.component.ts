import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IContentField, ContentField } from 'app/shared/model/content-field.model';
import { ContentFieldService } from './content-field.service';

@Component({
  selector: 'jhi-content-field-update',
  templateUrl: './content-field-update.component.html',
})
export class ContentFieldUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    slug: [null, [Validators.required, Validators.maxLength(40)]],
    state: [null, [Validators.required]],
    type: [],
    createdAt: [],
    updatedAt: [],
    createdBy: [],
    updatedBy: [],
  });

  constructor(protected contentFieldService: ContentFieldService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contentField }) => {
      if (!contentField.id) {
        const today = moment().startOf('day');
        contentField.createdAt = today;
        contentField.updatedAt = today;
      }

      this.updateForm(contentField);
    });
  }

  updateForm(contentField: IContentField): void {
    this.editForm.patchValue({
      id: contentField.id,
      name: contentField.name,
      slug: contentField.slug,
      state: contentField.state,
      type: contentField.type,
      createdAt: contentField.createdAt ? contentField.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: contentField.updatedAt ? contentField.updatedAt.format(DATE_TIME_FORMAT) : null,
      createdBy: contentField.createdBy,
      updatedBy: contentField.updatedBy,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contentField = this.createFromForm();
    if (contentField.id !== undefined) {
      this.subscribeToSaveResponse(this.contentFieldService.update(contentField));
    } else {
      this.subscribeToSaveResponse(this.contentFieldService.create(contentField));
    }
  }

  private createFromForm(): IContentField {
    return {
      ...new ContentField(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      slug: this.editForm.get(['slug'])!.value,
      state: this.editForm.get(['state'])!.value,
      type: this.editForm.get(['type'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      updatedBy: this.editForm.get(['updatedBy'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContentField>>): void {
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
