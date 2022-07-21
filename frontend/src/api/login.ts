import axios from 'axios';

import { API_KEY } from '@/constants';

const loginApi = {
  endPoint: {
    googleEntry: '/api/auth/google/oauth-uri',
    googleToken: '/api/auth/google/token',
  },

  getUrl: async () => {
    const { data } = await axios.get(`${API_KEY}${loginApi.endPoint.googleEntry}`);

    return data.oAuthUri;
  },

  auth: async () => {
    const code = new URLSearchParams(location.search).get('code');
    const { data } = await axios.post(`${API_KEY}${loginApi.endPoint.googleToken}`, {
      code,
    });

    return data.accessToken;
  },
};

export default loginApi;
