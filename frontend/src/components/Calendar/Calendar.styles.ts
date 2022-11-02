import { css, Theme } from '@emotion/react';

import { DAYS } from '@/constants/date';

const calendarHeaderStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  padding: 3rem 2rem;

  font-size: 5rem;
  font-weight: 500;
  color: ${colors.GRAY_700};
`;

const navStyle = ({ flex }: Theme) => css`
  ${flex.row}

  gap: 4rem;
`;

const spinnerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  gap: 2rem;

  width: 100%;
  height: 100%;

  font-size: 3rem;
`;

const monthPickerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-around;
`;

const todayButtonStyle = ({ colors }: Theme) => css`
  width: 15rem;
  height: 8rem;

  padding: auto 0;

  font-size: 4rem;
  font-weight: 500;
  color: ${colors.GRAY_700};
  line-height: 4rem;
`;

const navButtonStyle = ({ colors }: Theme) => css`
  position: relative;

  width: 8rem;
  height: 8rem;
  padding: 0;

  font-size: 4rem;
  line-height: 4rem;
  color: ${colors.GRAY_600};

  &:hover {
    border-radius: 50%;

    background: ${colors.GRAY_100};

    filter: none;
  }

  &:hover span {
    visibility: visible;
  }
`;

const navButtonTitleStyle = ({ colors }: Theme) => css`
  visibility: hidden;
  position: absolute;

  top: 120%;
  left: 50%;
  transform: translateX(-50%);

  padding: 2rem 3rem;

  background: ${colors.GRAY_700}ee;

  font-size: 3rem;
  font-weight: normal;
  color: ${colors.WHITE};
  white-space: nowrap;
`;

const dayGridStyle = css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));

  height: 7rem;
`;

const dayStyle = ({ colors }: Theme, day: string) => css`
  padding: 2rem;
  border-top: 1px solid ${colors.GRAY_300};
  border-right: 1px solid ${colors.GRAY_300};
  border-left: ${day === DAYS[0] && `1px solid ${colors.GRAY_300}`};

  font-size: 3rem;
  color: ${day === DAYS[0] && colors.RED_400};
  text-align: right;
`;

const calendarGridStyle = (rowNum: number) => css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));
  grid-auto-rows: calc(calc(100vh - 42rem) / ${rowNum});
`;

export {
  calendarGridStyle,
  calendarHeaderStyle,
  dayGridStyle,
  dayStyle,
  monthPickerStyle,
  navButtonStyle,
  navButtonTitleStyle,
  navStyle,
  spinnerStyle,
  todayButtonStyle,
};
