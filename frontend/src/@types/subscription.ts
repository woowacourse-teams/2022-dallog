interface SubscriptionType {
  category: {
    id: number;
    name: string;
    creator: {
      displayName: string;
      email: string;
      id: number;
      profileImageUri: string;
    };
    createdAt: string;
  };
  color: string;
}

export { SubscriptionType };
