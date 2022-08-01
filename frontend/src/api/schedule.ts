import { ScheduleResponseType, ScheduleType } from '@/@types/schedule';

import dallogApi from './';

const scheduleApi = {
  endpoint: '/api/members/me/schedules',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string | null, startDate: string, endDate: string) => {
    const response = await dallogApi.get<ScheduleResponseType[]>(
      `${scheduleApi.endpoint}?startDate=${startDate}&endDate=${endDate}`,
      {
        headers: { ...scheduleApi.headers, Authorization: `Bearer ${accessToken}` },
      }
    );

    return response;
  },

  post: async (body: Omit<ScheduleType, 'id'>) => {
    const response = await dallogApi.post(scheduleApi.endpoint, body, {
      headers: scheduleApi.headers,
    });

    return response;
  },
};

export default scheduleApi;
