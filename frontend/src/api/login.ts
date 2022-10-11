import dallogApi from './';

const loginApi = {
  endPoint: {
    googleEntry: '/api/auth/google/oauth-uri',
    googleToken: '/api/auth/google/token',
    validate: '/api/auth/validate/token',
    again: '/api/auth/token/access',
  },
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  getUrl: async () => {
    const { data } = await dallogApi.get(
      `${loginApi.endPoint.googleEntry}?redirectUri=${location.href}oauth`
    );

    return data.oAuthUri;
  },

  auth: async (code: string | null) => {
    const { data } = await dallogApi.post(loginApi.endPoint.googleToken, {
      code,
      redirectUri: location.href.split('?')[0],
    });

    return data;
  },

  relogin: async (refreshToken: string | null) => {
    const { data } = await dallogApi.post(loginApi.endPoint.again, {
      refreshToken,
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
