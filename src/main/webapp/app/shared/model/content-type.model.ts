import { Moment } from 'moment';

export interface IContentType {
  id?: string;
  name?: string;
  slug?: string;
  state?: number;
  description?: any;
  createdAt?: Moment;
  updatedAt?: Moment;
  createdBy?: string;
  updatedBy?: string;
}

export class ContentType implements IContentType {
  constructor(
    public id?: string,
    public name?: string,
    public slug?: string,
    public state?: number,
    public description?: any,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public createdBy?: string,
    public updatedBy?: string
  ) {}
}
