import { ProfileType } from '@/@types/profile';

import dallogApi from './';

const profileApi = {
  endpoint: {
    get: '/api/members/me',
    delete: '/api/members/me',
    patch: '/api/members/me',
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string) => {
    const response = await dallogApi.get<ProfileType>(profileApi.endpoint.get, {
      headers: { ...profileApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  delete: async (accessToken: string) => {
    const response = await dallogApi.delete(profileApi.endpoint.delete, {
      headers: { ...profileApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  patch: async (accessToken: string, body: Pick<ProfileType, 'displayName'>) => {
    const response = await dallogApi.patch(profileApi.endpoint.patch, body, {
      headers: { ...profileApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default profileApi;
