import { CATEGORY_TYPE, ROLE } from '@/constants/category';

import { ProfileType } from './profile';

type CategoryRoleType = typeof ROLE[keyof typeof ROLE];

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

interface SingleCategoryType extends CategoryType {
  subscriberCount: number;
}

export {
  CategoryType,
  CategoryRoleType,
  CategorySubscriberType,
  CategoriesGetResponseType,
  SingleCategoryType,
};
