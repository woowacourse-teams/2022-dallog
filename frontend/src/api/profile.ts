import { ProfileType } from '@/@types/profile';

import dallogApi from './';

const profileApi = {
  endpoint: '/api/members/me',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string | null) => {
    const response = await dallogApi.get<ProfileType>(profileApi.endpoint, {
      headers: { ...profileApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default profileApi;
