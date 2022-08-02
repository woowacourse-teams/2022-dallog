import { css, Theme } from '@emotion/react';

import { DAYS } from '@/constants';

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
  align-items: center;
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
`;

const calendarGrid = (rowNum: number) => css`
  display: grid;
  grid-template-columns: repeat(7, calc(100% / 7));
  grid-auto-rows: calc(75vh / ${rowNum});
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
`;

const dateText = ({ colors }: Theme, day: number, isThisMonth: boolean) => css`
  position: absolute;
  top: 2rem;
  right: 2rem;

  padding: 1rem;

  font-size: 3rem;
  font-weight: 500;

  color: ${day === 0
    ? isThisMonth
      ? colors.RED_400
      : `${colors.RED_400}80`
    : isThisMonth
    ? colors.GRAY_700
    : `${colors.GRAY_700}80`};
`;

const itemWithBackgroundStyle = (priority: number, color: string, isHovering: boolean) => css`
  position: absolute;
  top: ${priority * 7}rem;

  display: ${priority >= 4 ? 'none' : 'block'};
  width: 100%;
  height: 6rem;
  padding: 1rem;

  background: ${color};

  font-size: 3rem;
  font-weight: 700;
  line-height: 4rem;
  white-space: nowrap;
  color: white;

  cursor: pointer;
  filter: ${isHovering && 'brightness(80%)'};
`;

const itemWithoutBackgroundStyle = (
  { colors }: Theme,
  priority: number,
  color: string,
  isHovering: boolean
) => css`
  ${itemWithBackgroundStyle(priority, color, isHovering)}

  overflow-x: hidden;
  display: flex;
  align-items: center;

  background: ${isHovering ? colors.GRAY_100 : 'transparent'};

  color: black;

  cursor: pointer;
  filter: none;
`;

const circleStyle = (color: string) => css`
  width: 2.5rem;
  height: 2.5rem;
  margin-right: 1rem;
  border-radius: 50%;

  background: ${color};
`;

export {
  calendarGrid,
  calendarHeader,
  circleStyle,
  dayBar,
  dateBorder,
  dateText,
  itemWithoutBackgroundStyle,
  itemWithBackgroundStyle,
  monthPicker,
  navBarGrid,
  navButton,
  navButtonTitle,
  todayButton,
};
