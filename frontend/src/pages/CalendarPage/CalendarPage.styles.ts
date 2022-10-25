import { css, Theme } from '@emotion/react';

import { DAYS } from '@/constants/date';
import { SCHEDULE } from '@/constants/style';

const calendarPageStyle = css`
  padding: 0 5rem 5rem;
`;

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

const dateCellStyle = ({ colors }: Theme, day: number) => css`
  position: relative;

  height: 100%;
  border-right: 1px solid ${colors.GRAY_300};
  border-bottom: 1px solid ${colors.GRAY_300};
  border-left: ${day === 0 && `1px solid ${colors.GRAY_300}`};

  &:hover {
    background: ${colors.GRAY_000};
  }
`;

const dateTextStyle = (
  { colors }: Theme,
  day: number,
  isThisMonth: boolean,
  isToday: boolean
) => css`
  position: absolute;
  top: 1rem;
  right: 1rem;

  height: ${SCHEDULE.HEIGHT}rem;
  padding: 1rem;
  border-radius: 50%;

  background: ${isToday && colors.YELLOW_500};

  font-size: 2.5rem;
  font-weight: 500;
  color: ${isToday
    ? colors.WHITE
    : day === 0
    ? `${colors.RED_400}${isThisMonth ? '' : '80'}`
    : `${colors.GRAY_700}${isThisMonth ? '' : '80'}`};
  text-align: right;
  line-height: 3rem;
`;

const itemWithBackgroundStyle = (
  priority: number | null,
  color: string,
  isHovering: boolean,
  maxView: number,
  isEndDate: boolean
) => css`
  display: ${priority && priority >= maxView ? 'none' : 'block'};
  overflow: hidden;
  position: absolute;
  top: ${priority && priority * SCHEDULE.HEIGHT_WITH_MARGIN + 1}rem;

  width: ${isEndDate ? '97%' : '100%'};
  height: ${SCHEDULE.HEIGHT}rem;
  padding: 1rem;
  ${isEndDate &&
  css`
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
  `}

  background: ${color};

  font-size: 2.75rem;
  color: white;
  white-space: nowrap;
  text-overflow: ellipsis;
  line-height: 2.75rem;

  cursor: pointer;
  filter: ${isHovering && 'brightness(0.95)'};

  transition: background-color 0.3s;
`;

const itemWithoutBackgroundStyle = (
  { colors }: Theme,
  priority: number | null,
  color: string,
  isHovering: boolean,
  maxView: number,
  isEndDate: boolean
) => css`
  ${itemWithBackgroundStyle(priority, color, isHovering, maxView, isEndDate)};

  border-left: 3px solid ${color};

  background: ${isHovering ? colors.GRAY_000 : colors.WHITE};

  color: black;

  filter: none;

  transition: background-color 0.3s;
`;

const moreStyle = ({ colors }: Theme) => css`
  overflow: hidden;
  position: absolute;
  bottom: 0;

  width: 100%;
  height: ${SCHEDULE.HEIGHT}rem;
  padding: 1rem;

  font-size: 2.75rem;
  font-weight: 200;
  color: ${colors.GRAY_500};
  white-space: nowrap;
  text-overflow: ellipsis;
  line-height: 2.75rem;

  cursor: pointer;

  &:hover {
    color: ${colors.BLACK};
  }
`;

export {
  calendarGridStyle,
  calendarHeaderStyle,
  calendarPageStyle,
  dateCellStyle,
  dateTextStyle,
  dayGridStyle,
  dayStyle,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
  monthPickerStyle,
  moreStyle,
  navButtonStyle,
  navButtonTitleStyle,
  navStyle,
  spinnerStyle,
  todayButtonStyle,
};
