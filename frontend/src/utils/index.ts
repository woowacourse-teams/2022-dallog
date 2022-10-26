const debounce = <F extends (...params: any[]) => void>(callback: F, delay = 100) => {
  let timer: NodeJS.Timeout;

  return function (...args: any[]) {
    if (timer) clearTimeout(timer);

    timer = setTimeout(() => callback.call(args), delay);
  } as F;
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

export { debounce, getRandomNumber, getSearchParam, zeroFill };
