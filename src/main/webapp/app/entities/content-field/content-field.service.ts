import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IContentField } from 'app/shared/model/content-field.model';

type EntityResponseType = HttpResponse<IContentField>;
type EntityArrayResponseType = HttpResponse<IContentField[]>;

@Injectable({ providedIn: 'root' })
export class ContentFieldService {
  public resourceUrl = SERVER_API_URL + 'api/content-fields';

  constructor(protected http: HttpClient) {}

  create(contentField: IContentField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contentField);
    return this.http
      .post<IContentField>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contentField: IContentField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contentField);
    return this.http
      .put<IContentField>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IContentField>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContentField[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(contentField: IContentField): IContentField {
    const copy: IContentField = Object.assign({}, contentField, {
      createdAt: contentField.createdAt && contentField.createdAt.isValid() ? contentField.createdAt.toJSON() : undefined,
      updatedAt: contentField.updatedAt && contentField.updatedAt.isValid() ? contentField.updatedAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? moment(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((contentField: IContentField) => {
        contentField.createdAt = contentField.createdAt ? moment(contentField.createdAt) : undefined;
        contentField.updatedAt = contentField.updatedAt ? moment(contentField.updatedAt) : undefined;
      });
    }
    return res;
  }
}
