import { ProfileType } from './profile';

interface SubscriptionType {
  id: number;
  category: {
    id: number;
    name: string;
    creator: ProfileType;
    createdAt: string;
  };
  colorCode: string;
  checked: boolean;
}

export { SubscriptionType };
