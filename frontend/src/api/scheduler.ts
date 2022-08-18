import { SchedulingResponseType } from '@/@types/scheduler';

import { DATE_TIME } from '@/constants/date';

import dallogApi from '.';

const schedulerApi = {
  endpoint: (categoryId: number) => `/api/scheduler/categories/${categoryId}/available-periods`,

  headers: {
    'Content-Type': 'application/json',
  },

  get: async (
    accessToken: string,
    categoryId: number,
    startDateTime: string,
    endDateTime: string
  ) => {
    const response = await dallogApi.get<SchedulingResponseType[]>(
      `${schedulerApi.endpoint(categoryId)}?startDateTime=${startDateTime}T${
        DATE_TIME.START
      }&endDateTime=${endDateTime}T${DATE_TIME.END}`,
      {
        headers: { ...schedulerApi.headers, Authorization: `Bearer ${accessToken}` },
      }
    );

    return response;
  },
};

export default schedulerApi;
