import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IContent, Content } from 'app/shared/model/content.model';
import { ContentService } from './content.service';

@Component({
  selector: 'jhi-content-update',
  templateUrl: './content-update.component.html',
})
export class ContentUpdateComponent implements OnInit {
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

  constructor(protected contentService: ContentService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ content }) => {
      if (!content.id) {
        const today = moment().startOf('day');
        content.createdAt = today;
        content.updatedAt = today;
      }

      this.updateForm(content);
    });
  }

  updateForm(content: IContent): void {
    this.editForm.patchValue({
      id: content.id,
      name: content.name,
      slug: content.slug,
      state: content.state,
      type: content.type,
      createdAt: content.createdAt ? content.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: content.updatedAt ? content.updatedAt.format(DATE_TIME_FORMAT) : null,
      createdBy: content.createdBy,
      updatedBy: content.updatedBy,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const content = this.createFromForm();
    if (content.id !== undefined) {
      this.subscribeToSaveResponse(this.contentService.update(content));
    } else {
      this.subscribeToSaveResponse(this.contentService.create(content));
    }
  }

  private createFromForm(): IContent {
    return {
      ...new Content(),
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContent>>): void {
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
