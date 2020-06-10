import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContentField } from 'app/shared/model/content-field.model';

@Component({
  selector: 'jhi-content-field-detail',
  templateUrl: './content-field-detail.component.html',
})
export class ContentFieldDetailComponent implements OnInit {
  contentField: IContentField | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contentField }) => (this.contentField = contentField));
  }

  previousState(): void {
    window.history.back();
  }
}
