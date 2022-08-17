import { GoogleCalendarGetResponseType, GoogleCalendarPostBodyType } from '@/@types/googleCalendar';

import dallogApi from './';

const googleCalendarApi = {
  endpoint: '/api/external-calendars/me',

  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string) => {
    const response = await dallogApi.get<GoogleCalendarGetResponseType>(
      googleCalendarApi.endpoint,
      {
        headers: { ...googleCalendarApi.headers, Authorization: `Bearer ${accessToken}` },
      }
    );

    return response;
  },

  post: async (accessToken: string, body: GoogleCalendarPostBodyType) => {
    const response = await dallogApi.post(googleCalendarApi.endpoint, body, {
      headers: { ...googleCalendarApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default googleCalendarApi;
