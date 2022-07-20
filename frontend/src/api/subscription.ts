import axios from 'axios';

import { SubscriptionType } from '@/@types/subscription';

const subscriptionApi = {
  getEndpoint: '/api/members/me/subscriptions',

  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string | null) => {
    const response = await axios.get<SubscriptionType[]>(subscriptionApi.getEndpoint, {
      headers: { ...subscriptionApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => {
        return JSON.parse(res).subscriptions;
      },
    });

    return response;
  },
};

export default subscriptionApi;
