import { ScheduleResponseType, ScheduleType } from '@/@types/schedule';

import { getDayOffsetDateTime } from '@/utils/date';

import dallogApi from './';

const scheduleApi = {
  endpoint: {
    get: '/api/members/me/schedules',
    post: (categoryId: number) => `/api/categories/${categoryId}/schedules`,
    patch: (scheduleId: string) => `/api/schedules/${scheduleId}`,
    delete: (scheduleId: string) => `/api/schedules/${scheduleId}`,
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string, startDateTime: string, endDateTime: string) => {
    const response = await dallogApi.get<ScheduleResponseType>(
      `${
        scheduleApi.endpoint.get
      }?startDateTime=${startDateTime}&endDateTime=${getDayOffsetDateTime(endDateTime, 1)}`,
      {
        headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
      }
    );

    return response;
  },

  post: async (
    accessToken: string,
    categoryId: number,
    body: Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode' | 'categoryType'>
  ) => {
    const response = await dallogApi.post(scheduleApi.endpoint.post(categoryId), body, {
      headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  patch: async (
    accessToken: string,
    scheduleId: string,
    body: Omit<ScheduleType, 'id' | 'categoryId' | 'colorCode' | 'categoryType'>
  ) => {
    const response = await dallogApi.patch(scheduleApi.endpoint.patch(scheduleId), body, {
      headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  delete: async (accessToken: string, scheduleId: string) => {
    const response = await dallogApi.delete(scheduleApi.endpoint.delete(scheduleId), {
      headers: { Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default scheduleApi;
