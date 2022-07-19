import { css, Theme } from '@emotion/react';

const calendarDate = ({ colors }: Theme, day: number, isThisMonth: boolean) => css`
  background: ${day === 0 || day === 6 ? colors.GRAY_100 : colors.WHITE};

  opacity: ${isThisMonth || 0.5};
`;

const dateBorder = ({ colors }: Theme) => css`
  position: relative;

  height: 100%;
  padding: 1rem;
  border-top: 1px solid ${colors.GRAY_300};
  border-right: 1px solid ${colors.GRAY_300};
`;

const dateText = css`
  position: absolute;
  top: 2rem;
  right: 2rem;

  padding: 1rem;

  font-size: 4rem;
  font-weight: 700;
`;

export { calendarDate, dateText, dateBorder };
