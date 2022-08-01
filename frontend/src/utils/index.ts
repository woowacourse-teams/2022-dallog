import { InputRef } from '@/@types';

import { STORAGE_KEY } from '@/constants';

const createPostBody = (inputRef: InputRef) => {
  const inputElements = Object.values(inputRef).map((el) => el.current);
  const isValidInputRefs = inputElements.every((el) => el instanceof HTMLInputElement);

  if (!isValidInputRefs) {
    return;
  }

  const inputRefKeys = Object.keys(inputRef);

  const body = inputRefKeys.reduce((acc: any, cur: number | string) => {
    acc[cur] = (inputRef[cur].current as HTMLInputElement).value;

    return acc;
  }, {});

  return body;
};

const getAccessToken = () => {
  return localStorage.getItem(STORAGE_KEY.ACCESS_TOKEN);
};

const setAccessToken = (accessToken: string) => {
  localStorage.setItem(STORAGE_KEY.ACCESS_TOKEN, accessToken);
};

const clearAccessToken = () => {
  localStorage.removeItem(STORAGE_KEY.ACCESS_TOKEN);
};

export { clearAccessToken, createPostBody, getAccessToken, setAccessToken };
