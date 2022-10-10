import { STORAGE_KEY } from '@/constants';

const getAccessToken = () => {
  return localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
};

const getRefreshToken = () => {
  return localStorage.getItem(STORAGE_KEY.REFRESH_TOKEN);
};

const removeAccessToken = () => {
  localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
};

const removeRefreshToken = () => {
  localStorage.removeItem(STORAGE_KEY.REFRESH_TOKEN);
};

const setAccessToken = (accessToken: string) => {
  localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, accessToken);
};

const setRefreshToken = (refreshToken: string) => {
  localStorage.setItem(STORAGE_KEY.REFRESH_TOKEN, refreshToken);
};

export {
  getAccessToken,
  getRefreshToken,
  removeAccessToken,
  removeRefreshToken,
  setAccessToken,
  setRefreshToken,
};
