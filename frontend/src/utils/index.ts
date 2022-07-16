import { InputRef } from '@/@types';

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

const getDate = () => {
  return new Date(+new Date() + 3240 * 10000).toISOString().split('T')[0];
};

const getDateTime = () => {
  return new Date(+new Date() + 3240 * 10000).toISOString().replace(/\..*/, '').slice(0, -3);
};

export { createPostBody, getDate, getDateTime };
