import axios from 'axios';

const loginApi = {
  baseUrl: 'http://09d2-218-39-176-142.ngrok.io/api/auth/google',
  entryEndpoint: '/link',
  tokenEndpoint: '/token',

  getUrl: async () => {
    const { data } = await axios.get(`${loginApi.baseUrl}${loginApi.entryEndpoint}`);

    return data.data;
  },

  auth: async () => {
    const code = new URLSearchParams(location.search).get('code');
    const { data } = await axios.post(`${loginApi.baseUrl}${loginApi.tokenEndpoint}`, { code });

    return data.data.accessToken;
  },
};

export default loginApi;
