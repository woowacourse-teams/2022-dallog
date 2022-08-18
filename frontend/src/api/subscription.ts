import { AxiosResponse } from 'axios';

import { SubscriptionType } from '@/@types/subscription';

import dallogApi from './';

const subscriptionApi = {
  endpoint: {
    get: '/api/members/me/subscriptions',
    post: (categoryId: number) => `/api/members/me/categories/${categoryId}/subscriptions`,
    patch: (subscriptionId: number) => `/api/members/me/subscriptions/${subscriptionId}`,
    delete: (subscriptionId: number) => `/api/members/me/subscriptions/${subscriptionId}`,
  },

  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string) => {
    const response = await dallogApi.get<SubscriptionType[]>(subscriptionApi.endpoint.get, {
      headers: { ...subscriptionApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => {
        return JSON.parse(res).subscriptions;
      },
    });

    return response;
  },

  post: async (
    accessToken: string,
    categoryId: number,
    body: Pick<SubscriptionType, 'colorCode'>
  ) => {
    const response = await dallogApi.post(subscriptionApi.endpoint.post(categoryId), body, {
      headers: { ...subscriptionApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  patch: async (
    accessToken: string,
    subscriptionId: number,
    body: Pick<SubscriptionType, 'colorCode'> | Pick<SubscriptionType, 'checked'>
  ) => {
    const response = await dallogApi.patch(subscriptionApi.endpoint.patch(subscriptionId), body, {
      headers: { ...subscriptionApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  delete: async (accessToken: string, subscriptionId: number): Promise<AxiosResponse<null>> => {
    const response = await dallogApi.delete<null>(subscriptionApi.endpoint.delete(subscriptionId), {
      headers: { ...subscriptionApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default subscriptionApi;
