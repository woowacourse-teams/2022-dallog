import { css, Theme } from '@emotion/react';

import { DAYS } from '@/constants/date';
import { SCHEDULE } from '@/constants/style';

const categoryPageStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: flex-start;
  align-items: flex-start;
  gap: 8rem;

  padding: 5rem;
`;

const categoryStyle = css`
  width: 30%;
`;

const categoryHeaderStyle = ({ colors }: Theme) => css`
  padding: 1rem 3rem 4rem;

  font-size: 6rem;
  font-weight: 600;
  color: ${colors.GRAY_700};
`;

const controlStyle = ({ flex }: Theme) => css`
  ${flex.row};

  align-items: flex-start;
  justify-content: center;
  gap: 4rem;
`;

const searchFormStyle = css`
  position: relative;

  width: 100%;
  height: 12rem;
  margin-bottom: 5rem;
`;

const searchButtonStyle = css`
  position: absolute;

  top: 50%;
  transform: translateY(-50%);

  width: 10rem;
`;

const searchFieldsetStyle = css`
  height: 100%;
`;

const searchInputStyle = css`
  height: 100%;
  padding-left: 10rem;

  font-size: 4rem;
`;

const buttonStyle = ({ colors }: Theme) => css`
  width: 20rem;
  height: 12rem;
  border-radius: 8px;
  border: 1px solid ${colors.GRAY_500};

  background: ${colors.YELLOW_500};

  font-size: 4rem;
  font-weight: 600;
  color: ${colors.WHITE};

  &:hover {
    box-shadow: none;
  }
`;

const calendarStyle = css`
  position: relative;

  width: 100%;
`;

const hintStyle = ({ colors }: Theme) => css`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%);
  z-index: 10;

  padding: 4rem 6rem;
  border-radius: 4px;

  background: ${colors.ORANGE_500};

  font-size: 4rem;
  font-weight: 500;
  color: ${colors.WHITE};
`;

const calendarHeaderStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  padding: 0 2rem 3rem;

  font-size: 5rem;
  font-weight: 500;
  color: ${colors.GRAY_700};
`;

const waitingNavStyle = ({ flex }: Theme) => css`
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
  width: 12rem;
  height: 8rem;

  padding: auto 0;

  font-size: 4rem;
  line-height: 4rem;
  font-weight: 500;
  color: ${colors.GRAY_700};
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

const dayBarGridStyle = css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));

  height: 7rem;
`;

const dayBarStyle = ({ colors }: Theme, day: string) => css`
  padding: 2rem 3rem;
  border-top: 1px solid ${colors.GRAY_300};
  border-right: 1px solid ${colors.GRAY_300};
  border-left: ${day === DAYS[0] && `1px solid ${colors.GRAY_300}`};

  font-size: 3rem;
  text-align: right;
  color: ${day === DAYS[0] && colors.RED_400};
`;

const calendarGridStyle = (rowNum: number) => css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));
  grid-auto-rows: calc(calc(100vh - 44rem) / ${rowNum});
`;

const dateStyle = ({ colors }: Theme, day: number) => css`
  position: relative;

  height: 100%;
  border-bottom: 1px solid ${colors.GRAY_300};
  border-right: 1px solid ${colors.GRAY_300};
  border-left: ${day === 0 && `1px solid ${colors.GRAY_300}`};
`;

const dateTextStyle = (
  { colors }: Theme,
  day: number,
  isThisMonth: boolean,
  isToday: boolean,
  isCategorySelected?: boolean
) => css`
  position: absolute;
  top: 1rem;
  right: 1rem;

  width: 5rem;
  height: ${SCHEDULE.HEIGHT}rem;
  padding: 1rem;
  border-radius: 50%;

  background: ${isToday && (isCategorySelected ? colors.YELLOW_500 : colors.GRAY_300)};

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
  priority: number | null,
  color: string,
  maxView: number,
  isEndDate: boolean
) => css`
  overflow: hidden;
  position: absolute;
  top: ${priority && priority * SCHEDULE.HEIGHT_WITH_MARGIN + 1}rem;

  display: ${priority && priority >= maxView ? 'none' : 'block'};

  width: ${isEndDate ? '96%' : '100%'};
  height: ${SCHEDULE.HEIGHT}rem;
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
`;

const itemWithoutBackgroundStyle = (
  { colors }: Theme,
  priority: number | null,
  color: string,
  maxView: number,
  isEndDate: boolean
) => css`
  ${itemWithBackgroundStyle(priority, color, maxView, isEndDate)};

  overflow: hidden;

  border-left: 3px solid ${color};

  background: ${colors.WHITE};

  color: black;

  cursor: pointer;
  filter: none;
`;

const moreStyle = ({ colors }: Theme) => css`
  position: absolute;
  bottom: 0;

  width: 100%;
  height: ${SCHEDULE.HEIGHT}rem;
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

export {
  buttonStyle,
  calendarGridStyle,
  calendarHeaderStyle,
  calendarStyle,
  categoryHeaderStyle,
  categoryPageStyle,
  categoryStyle,
  controlStyle,
  dateStyle,
  dateTextStyle,
  dayBarGridStyle,
  dayBarStyle,
  hintStyle,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
  monthPickerStyle,
  moreStyle,
  navButtonStyle,
  navButtonTitleStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
  spinnerStyle,
  todayButtonStyle,
  waitingNavStyle,
};
