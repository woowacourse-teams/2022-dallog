import axios from 'axios';

import { Schedule } from '@/@types';

import { API_KEY } from '@/constants';

const scheduleApi = {
  endpoint: `${API_KEY}/api/schedules`,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (year: number, month: number) => {
    const response = await axios.get<{ schedules: Schedule[] }>(
      `${scheduleApi.endpoint}?year=${year}&month=${month}`,
      {
        headers: scheduleApi.headers,
      }
    );

    return response;
  },

  post: async (body: Omit<Schedule, 'id'>) => {
    const response = await axios.post(scheduleApi.endpoint, body, {
      headers: scheduleApi.headers,
    });

    return response;
  },
};

export default scheduleApi;
