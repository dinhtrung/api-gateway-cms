import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDomain } from 'app/shared/model/domain.model';

type EntityResponseType = HttpResponse<IDomain>;
type EntityArrayResponseType = HttpResponse<IDomain[]>;

@Injectable({ providedIn: 'root' })
export class DomainService {
  public resourceUrl = SERVER_API_URL + 'api/domains';

  constructor(protected http: HttpClient) {}

  create(domain: IDomain): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(domain);
    return this.http
      .post<IDomain>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(domain: IDomain): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(domain);
    return this.http
      .put<IDomain>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IDomain>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDomain[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(domain: IDomain): IDomain {
    const copy: IDomain = Object.assign({}, domain, {
      createdAt: domain.createdAt && domain.createdAt.isValid() ? domain.createdAt.toJSON() : undefined,
      updatedAt: domain.updatedAt && domain.updatedAt.isValid() ? domain.updatedAt.toJSON() : undefined,
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
      res.body.forEach((domain: IDomain) => {
        domain.createdAt = domain.createdAt ? moment(domain.createdAt) : undefined;
        domain.updatedAt = domain.updatedAt ? moment(domain.updatedAt) : undefined;
      });
    }
    return res;
  }
}
