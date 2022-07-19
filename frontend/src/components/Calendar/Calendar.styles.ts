import { css, Theme } from '@emotion/react';

import { DAYS } from '@/constants';

const calendar = css`
  height: 95vh;
  margin: 3rem;
`;

const calendarHeader = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content:space-between;

  width: 100%;
  height: 5vh;
  padding: 3rem;

  font-size: 6rem;
  font-weight: 700;
`;

const monthPicker = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content:space-around;
  align-items: center;

  width: 40rem;
`;

const navButton = css`
  background: transparent;

  font-size: 5rem;
  line-height: 3rem;
  font-weight: 700;

  &:hover {
    transform: scale(1.1);
  }
`;

const navBarGrid = css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));

  height: 5vh;
`;

const calendarGrid = (rowNum: number) => css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));
  grid-auto-rows: calc(85vh / ${rowNum});
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

export { calendar, calendarHeader, navBarGrid, calendarGrid, dayBar, monthPicker, navButton };
