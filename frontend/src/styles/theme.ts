import { css, Theme } from '@emotion/react';

const colors = {
  YELLOW_400: '#fee500',
  YELLOW_500: '#FCC419',
  GREEN_500: '#03c75a',
  WHITE: '#ffffff',
  GRAY_000: '#f8f9fa',
  GRAY_100: '#f1f3f5',
  GRAY_200: '#e9ecef',
  GRAY_300: '#dee2e6',
  GRAY_400: '#ced4da',
  GRAY_500: '#adb5bd',
  GRAY_600: '#868e96',
  GRAY_700: '#495057',
  GRAY_800: '#343a40',
  GRAY_900: '#212529',
  BLACK: '#000000',
};

const flex = {
  row: css`
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  `,
  column: css`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  `,
};

const theme: Theme = {
  colors,
  flex,
};

export default theme;
