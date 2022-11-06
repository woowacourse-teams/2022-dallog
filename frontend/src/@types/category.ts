import { ProfileType } from '@/@types/profile';
import { ValueOf } from '@/@types/util';

import { CATEGORY_TYPE, ROLE } from '@/constants/category';

type CategoryRoleType = ValueOf<typeof ROLE>;

interface CategoryType {
  id: number;
  name: string;
  creator: ProfileType;
  createdAt: string;
  categoryType: ValueOf<typeof CATEGORY_TYPE>;
}

interface CategorySubscriberType {
  member: ProfileType;
  categoryRoleType: CategoryRoleType;
}

interface SingleCategoryType extends CategoryType {
  subscriberCount: number;
}

export { CategoryType, CategoryRoleType, CategorySubscriberType, SingleCategoryType };
