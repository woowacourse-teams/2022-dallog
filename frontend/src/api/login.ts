import dallogApi from './';

const loginApi = {
  endPoint: {
    googleEntry: '/api/auth/google/oauth-uri',
    googleToken: '/api/auth/google/token',
  },

  getUrl: async () => {
    const { data } = await dallogApi.get(loginApi.endPoint.googleEntry);

    return data.oAuthUri;
  },

  auth: async () => {
    const code = new URLSearchParams(location.search).get('code');
    const { data } = await dallogApi.post(loginApi.endPoint.googleToken, {
      code,
    });

    return data.accessToken;
  },
};

export default loginApi;
