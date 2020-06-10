import { Moment } from 'moment';

export interface IContentField {
  id?: string;
  name?: string;
  slug?: string;
  state?: number;
  type?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  createdBy?: string;
  updatedBy?: string;
}

export class ContentField implements IContentField {
  constructor(
    public id?: string,
    public name?: string,
    public slug?: string,
    public state?: number,
    public type?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public createdBy?: string,
    public updatedBy?: string
  ) {}
}
