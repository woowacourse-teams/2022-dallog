import { ProfileType } from './profile';

interface CategoryType {
  id: number;
  name: string;
  creator: ProfileType;
  createdAt: string;
}

interface CategoriesGetResponseType {
  page: number;
  categories: CategoryType[];
}

export { CategoryType, CategoriesGetResponseType };
