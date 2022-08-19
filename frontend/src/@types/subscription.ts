import { CategoryType } from './category';

interface SubscriptionType {
  id: number;
  category: CategoryType;
  colorCode: string;
  checked: boolean;
}

export { SubscriptionType };
