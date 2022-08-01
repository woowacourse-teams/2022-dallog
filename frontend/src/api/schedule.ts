import { ScheduleType } from '@/@types/schedule';

import dallogApi from './';

const scheduleApi = {
  endpoint: '/api/schedules',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (year: number, month: number) => {
    const response = await dallogApi.get<{ schedules: ScheduleType[] }>(
      `${scheduleApi.endpoint}?year=${year}&month=${month}`,
      {
        headers: scheduleApi.headers,
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
