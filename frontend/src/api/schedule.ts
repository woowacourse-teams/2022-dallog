import { ScheduleResponseType, ScheduleType } from '@/@types/schedule';

import { DATE_TIME } from '@/constants/date';

import dallogApi from './';

const scheduleApi = {
  endpoint: {
    get: '/api/members/me/schedules',
    post: (categoryId: number) => `/api/categories/${categoryId}/schedules`,
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string | null, startDate: string, endDate: string) => {
    const response = await dallogApi.get<ScheduleResponseType>(
      `${scheduleApi.endpoint.get}?startDate=${startDate}T${DATE_TIME.START}&endDate=${endDate}T${DATE_TIME.END}`,
      {
        headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
      }
    );

    return response;
  },

  post: async (
    accessToken: string | null,
    categoryId: number,
    body: Omit<ScheduleType, 'id' | 'categoryId'>
  ) => {
    const response = await dallogApi.post(scheduleApi.endpoint.post(categoryId), body, {
      headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default scheduleApi;
