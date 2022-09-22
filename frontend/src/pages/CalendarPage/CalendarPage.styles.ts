import { css, Theme } from '@emotion/react';

import { DAYS } from '@/constants/date';

const calendarPage = css`
  padding: 0 5rem 5rem;
`;

const calendarHeader = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  padding: 3rem 2rem;

  font-size: 5rem;
  font-weight: 500;
  color: ${colors.GRAY_700};
`;

const monthPicker = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-around;
`;

const todayButton = ({ colors }: Theme) => css`
  width: 12rem;
  height: 8rem;

  padding: auto 0;

  font-size: 4rem;
  line-height: 4rem;
  font-weight: 500;
  color: ${colors.GRAY_700};
`;

const navButton = ({ colors }: Theme) => css`
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

const navButtonTitle = ({ colors }: Theme) => css`
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

const navBarGrid = css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));

  height: 7rem;
`;

const calendarGrid = (rowNum: number) => css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));
  grid-auto-rows: calc(calc(100vh - 42rem) / ${rowNum});
`;

const dayBar = ({ colors }: Theme, day: string) => css`
  padding: 2rem 3rem;
  border-top: 1px solid ${colors.GRAY_300};
  border-right: 1px solid ${colors.GRAY_300};
  border-left: ${day === DAYS[0] && `1px solid ${colors.GRAY_300}`};

  font-size: 3rem;
  text-align: right;
  color: ${day === DAYS[0] && colors.RED_400};
`;

const dateBorder = ({ colors }: Theme, day: number) => css`
  position: relative;

  height: 100%;
  border-bottom: 1px solid ${colors.GRAY_300};
  border-right: 1px solid ${colors.GRAY_300};
  border-left: ${day === 0 && `1px solid ${colors.GRAY_300}`};

  &:hover {
    background: ${colors.GRAY_000};
  }
`;

const dateText = ({ colors }: Theme, day: number, isThisMonth: boolean, isToday: boolean) => css`
  position: absolute;
  top: 1rem;
  right: 1rem;

  width: 5rem;
  height: 5rem;
  padding: 1rem;
  border-radius: 50%;

  background: ${isToday && colors.YELLOW_500};

  font-size: 2.5rem;
  text-align: center;
  line-height: 3rem;
  font-weight: 500;
  color: ${isToday
    ? colors.WHITE
    : day === 0
    ? isThisMonth
      ? colors.RED_400
      : `${colors.RED_400}80`
    : isThisMonth
    ? colors.GRAY_700
    : `${colors.GRAY_700}80`};
`;

const itemWithBackgroundStyle = (
  priority: number,
  color: string,
  isHovering: boolean,
  maxView: number,
  isEndDate: boolean
) => css`
  overflow: hidden;
  position: absolute;
  top: ${priority * 5.5 + 1}rem;

  display: ${priority >= maxView ? 'none' : 'block'};

  width: ${isEndDate ? '96%' : '100%'};
  height: 5rem;
  padding: 1rem;
  ${isEndDate &&
  css`
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
  `}

  background: ${color};

  font-size: 2.75rem;
  line-height: 2.75rem;
  white-space: nowrap;
  text-overflow: ellipsis;
  color: white;

  cursor: pointer;
  filter: ${isHovering && 'brightness(0.95)'};

  transition: background-color 0.3s;
`;

const itemWithoutBackgroundStyle = (
  { colors }: Theme,
  priority: number,
  color: string,
  isHovering: boolean,
  maxView: number,
  isEndDate: boolean
) => css`
  ${itemWithBackgroundStyle(priority, color, isHovering, maxView, isEndDate)};

  overflow: hidden;

  border-left: 3px solid ${color};

  background: ${isHovering ? colors.GRAY_000 : colors.WHITE};

  color: black;

  cursor: pointer;
  filter: none;

  transition: background-color 0.3s;
`;

const moreStyle = ({ colors }: Theme) => css`
  position: absolute;
  bottom: 0;

  width: 100%;
  height: 5rem;
  padding: 1rem;

  font-size: 2.75rem;
  line-height: 2.75rem;
  white-space: nowrap;
  font-weight: 200;
  color: ${colors.GRAY_500};

  cursor: pointer;

  &:hover {
    color: ${colors.BLACK};
  }
`;

const spinnerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  gap: 2rem;

  width: 100%;
  height: 100%;

  font-size: 2rem;
`;

const waitingNavStyle = ({ flex }: Theme) => css`
  ${flex.row}

  gap:4rem;
`;

export {
  calendarGrid,
  calendarHeader,
  calendarPage,
  dayBar,
  dateBorder,
  dateText,
  itemWithoutBackgroundStyle,
  itemWithBackgroundStyle,
  monthPicker,
  moreStyle,
  navBarGrid,
  navButton,
  navButtonTitle,
  spinnerStyle,
  todayButton,
  waitingNavStyle,
};
