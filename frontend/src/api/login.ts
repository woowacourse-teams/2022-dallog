import axios from 'axios';

const loginApi = {
  baseUrl: 'http://3.38.116.83:8080',
  endPoint: {
    googleEntry: '/api/auth/google/oauth-uri',
    googleToken: '/api/auth/google/token',
  },

  getUrl: async () => {
    const { data } = await axios.get(`${loginApi.baseUrl}${loginApi.endPoint.googleEntry}`);

    return data.oAuthUri;
  },

  auth: async () => {
    const code = new URLSearchParams(location.search).get('code');
    const { data } = await axios.post(`${loginApi.baseUrl}${loginApi.endPoint.googleToken}`, {
      code,
    });

    return data.accessToken;
  },
};

export default loginApi;
