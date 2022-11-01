import { css, Theme } from '@emotion/react';

import { SCHEDULE } from '@/constants/style';

const dateCellStyle = ({ colors }: Theme, day: number, readonly: boolean) => css`
  position: relative;

  height: 100%;
  border-right: 1px solid ${colors.GRAY_300};
  border-bottom: 1px solid ${colors.GRAY_300};
  border-left: ${day === 0 && `1px solid ${colors.GRAY_300}`};

  ${!readonly &&
  css`
    &:hover {
      background: ${colors.GRAY_000};
    }
  `}
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

  width: ${SCHEDULE.HEIGHT}rem;
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
  text-align: ${isToday ? 'center' : 'right'};
  line-height: 3rem;
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

export { dateCellStyle, dateTextStyle, moreStyle };
