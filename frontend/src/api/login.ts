import dallogApi from './';

const loginApi = {
  endPoint: {
    googleEntry: '/api/auth/google/oauth-uri',
    googleToken: '/api/auth/google/token',
    validate: '/api/auth/validate/token',
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  getUrl: async () => {
    const { data } = await dallogApi.get(loginApi.endPoint.googleEntry);

    return data.oAuthUri;
  },

  auth: async (code: string | null) => {
    const { data } = await dallogApi.post(loginApi.endPoint.googleToken, {
      code,
    });

    return data.accessToken;
  },

  validate: async (accessToken: string) => {
    const response = await dallogApi.get(loginApi.endPoint.validate, {
      headers: { ...loginApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default loginApi;
