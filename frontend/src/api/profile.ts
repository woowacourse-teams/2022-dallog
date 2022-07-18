import axios from 'axios';

import { ProfileType } from '@/@types/profile';

const profileApi = {
  endpoint: '/api/members/me',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string) => {
    const response = await axios.get<ProfileType>(profileApi.endpoint, {
      headers: { ...profileApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => {
        const profile = JSON.parse(res).data;

        return profile;
      },
    });

    return response;
  },
};

export default profileApi;
