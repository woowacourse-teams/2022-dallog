import { CATEGORY_TYPE } from '@/constants/category';

import { ProfileType } from './profile';

export type CategoryRoleType = 'NONE' | 'ADMIN';

interface CategoryType {
  id: number;
  name: string;
  creator: ProfileType;
  createdAt: string;
  categoryType: typeof CATEGORY_TYPE[keyof typeof CATEGORY_TYPE];
}

interface CategorySubscriberType {
  member: ProfileType;
  categoryRoleType: CategoryRoleType;
}

interface CategoriesGetResponseType {
  page: number;
  categories: CategoryType[];
}

export { CategoryType, CategorySubscriberType, CategoriesGetResponseType };
