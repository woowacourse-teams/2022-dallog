import axios from 'axios';

import { ProfileType } from '@/@types/profile';

import { API_KEY } from '@/constants';

const profileApi = {
  endpoint: `${API_KEY}/api/members/me`,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string | null) => {
    const response = await axios.get<ProfileType>(profileApi.endpoint, {
      headers: { ...profileApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default profileApi;
