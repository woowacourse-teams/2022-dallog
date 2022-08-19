import { InputRefType } from '@/@types';

const createPostBody = (inputRef: InputRefType) => {
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

const getRandomNumber = (min: number, max: number) => {
  return Math.floor(Math.random() * (max - min)) + min;
};

const getSearchParam = (key: string) => {
  return new URLSearchParams(location.search).get(key);
};

const zeroFill = (str: string | number) => {
  return str.toString().padStart(2, '0');
};

export { createPostBody, getRandomNumber, getSearchParam, zeroFill };
