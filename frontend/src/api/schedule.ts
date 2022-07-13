import axios from 'axios';

import { Schedule } from '@/@types';

const scheduleApi = {
  endpoint: '/api/schedule',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async () => {
    const response = await axios.get<{ schedules: Schedule[] }>(scheduleApi.endpoint, {
      headers: scheduleApi.headers,
    });

    return response;
  },

  post: async (body: Omit<Schedule, 'id'>) => {
    const response = axios.post(scheduleApi.endpoint, body, {
      headers: scheduleApi.headers,
    });

    return response;
  },
};

export default scheduleApi;
