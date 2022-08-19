import { STORAGE_KEY } from '@/constants';

const getAccessToken = () => {
  return localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
};

const setAccessToken = (accessToken: string) => {
  localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, accessToken);
};

const removeAccessToken = () => {
  localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
};

export { getAccessToken, removeAccessToken, setAccessToken };
