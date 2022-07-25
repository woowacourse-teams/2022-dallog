import { Schedule } from '@/@types';

import dallogApi from './';

const scheduleApi = {
  endpoint: '/api/schedules',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (year: number, month: number) => {
    const response = await dallogApi.get<{ schedules: Schedule[] }>(
      `${scheduleApi.endpoint}?year=${year}&month=${month}`,
      {
        headers: scheduleApi.headers,
      }
    );

    return response;
  },

  post: async (body: Omit<Schedule, 'id'>) => {
    const response = await dallogApi.post(scheduleApi.endpoint, body, {
      headers: scheduleApi.headers,
    });

    return response;
  },
};

export default scheduleApi;
