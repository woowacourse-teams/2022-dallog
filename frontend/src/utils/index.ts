import { RESPONSIVE } from '@/constants/style';

const getRandomNumber = (min: number, max: number) => {
  return Math.floor(Math.random() * (max - min)) + min;
};

const getRootFontSize = () => {
  if (innerWidth >= RESPONSIVE.LAPTOP.MIN_WIDTH) return RESPONSIVE.LAPTOP.FONT_SIZE;

  if (innerWidth > RESPONSIVE.MOBILE.MAX_WIDTH) return RESPONSIVE.TABLET.FONT_SIZE;

  return RESPONSIVE.MOBILE.FONT_SIZE;
};

const getSearchParam = (key: string) => {
  return new URLSearchParams(location.search).get(key);
};

const zeroFill = (str: string | number) => {
  return str.toString().padStart(2, '0');
};

export { getRandomNumber, getRootFontSize, getSearchParam, zeroFill };
