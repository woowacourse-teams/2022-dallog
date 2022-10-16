const getRandomNumber = (min: number, max: number) => {
  return Math.floor(Math.random() * (max - min)) + min;
};

const getSearchParam = (key: string) => {
  return new URLSearchParams(location.search).get(key);
};

const zeroFill = (str: string | number) => {
  return str.toString().padStart(2, '0');
};

export { getRandomNumber, getSearchParam, zeroFill };
