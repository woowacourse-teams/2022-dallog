import { ScheduleResponseType, ScheduleType } from '@/@types/schedule';

import { DATE_TIME } from '@/constants/date';

import dallogApi from './';

const scheduleApi = {
  endpoint: {
    get: '/api/members/me/schedules',
    post: (categoryId: number) => `/api/categories/${categoryId}/schedules`,
    patch: (scheduleId: number) => `/api/schedules/${scheduleId}`,
    delete: (scheduleId: number) => `/api/schedules/${scheduleId}`,
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string, startDate: string, endDate: string) => {
    const response = await dallogApi.get<ScheduleResponseType>(
      `${scheduleApi.endpoint.get}?startDateTime=${startDate}T${DATE_TIME.START}&endDateTime=${endDate}T${DATE_TIME.END}`,
      {
        headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
      }
    );

    return response;
  },

  post: async (
    accessToken: string,
    categoryId: number,
    body: Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode'>
  ) => {
    const response = await dallogApi.post(scheduleApi.endpoint.post(categoryId), body, {
      headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  patch: async (
    accessToken: string,
    scheduleId: number,
    body: Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode'>
  ) => {
    const response = await dallogApi.patch(scheduleApi.endpoint.patch(scheduleId), body, {
      headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  delete: async (accessToken: string, scheduleId: number) => {
    const response = await dallogApi.delete(scheduleApi.endpoint.delete(scheduleId), {
      headers: { Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default scheduleApi;
