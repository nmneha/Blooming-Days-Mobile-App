import { IProductFeed } from 'app/shared/model/product-feed.model';
import { IUser } from 'app/shared/model/user.model';
import { IProductDirectory } from 'app/shared/model/product-directory.model';

export interface ICabinet {
  id?: number;
  product?: string | null;
  productId?: number | null;
  productfeed?: IProductFeed | null;
  user?: IUser | null;
  productdirectories?: IProductDirectory[] | null;
}

export const defaultValue: Readonly<ICabinet> = {};
