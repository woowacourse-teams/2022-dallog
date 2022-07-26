import { css, Theme } from '@emotion/react';

const dateBorder = ({ colors }: Theme, day: number) => css`
  position: relative;

  height: 100%;
  padding: 1rem;
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

export { dateText, dateBorder };
