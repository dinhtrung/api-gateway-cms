import { Moment } from 'moment';

export interface IDomain {
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

export class Domain implements IDomain {
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
