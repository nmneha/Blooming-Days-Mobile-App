import { ICabinet } from 'app/shared/model/cabinet.model';

export interface IProductDirectory {
  id?: number;
  product?: string | null;
  productId?: string | null;
  productBrand?: string | null;
  primaryIngredient?: string | null;
  cabinets?: ICabinet[] | null;
}

export const defaultValue: Readonly<IProductDirectory> = {};
