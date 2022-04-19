import dayjs from 'dayjs';
import { IProductFeed } from 'app/shared/model/product-feed.model';

export interface IComments {
  id?: number;
  product?: string | null;
  date?: string | null;
  comment?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  productFeed?: IProductFeed | null;
}

export const defaultValue: Readonly<IComments> = {};
