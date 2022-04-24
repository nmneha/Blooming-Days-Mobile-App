import { IComments } from 'app/shared/model/comments.model';
import { IUser } from 'app/shared/model/user.model';
import { ICabinet } from 'app/shared/model/cabinet.model';

export interface IProductFeed {
  id?: number;
  product?: string | null;
  productId?: number | null;
  target?: string | null;
  primaryConcern?: string | null;
  comments?: IComments[] | null;
  user?: IUser | null;
  cabinet?: ICabinet | null;
}

export const defaultValue: Readonly<IProductFeed> = {};
