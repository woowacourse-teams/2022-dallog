import { css, Theme } from '@emotion/react';

import { DAYS } from '@/constants';

const calendarHeader = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content:space-between;

  width: 100%;
  padding: 4rem;

  font-size: 6rem;
  font-weight: 700;
`;

const monthPicker = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content:space-around;

  width: 40rem;
  padding-right: 4rem;

  font-size: 5rem;
  line-height: 5rem;
`;

const calendarGrid = css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));
`;

const dayBar = ({ colors }: Theme, day: string) => css`
  padding: 3rem;
  border-top: 1px solid ${colors.GRAY_300};
  border-right: 1px solid ${colors.GRAY_300};

  background: ${day === DAYS[0] || day === DAYS[6] ? colors.GRAY_100 : colors.WHITE};

  font-size: 3.5rem;
  font-weight: 700;
  text-align: right;
`;

export { calendarHeader, calendarGrid, dayBar, monthPicker };
