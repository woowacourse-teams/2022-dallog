import { css, Theme } from '@emotion/react';

const calendar = ({ flex }: Theme) => css`
  ${flex.column}

  width: 100%;
  height: 100%;

  gap: 5rem;
`;

const schedule = ({ colors }: Theme) => css`
  padding: 5rem;
  border: 1px solid ${colors.BLACK};

  font-size: 4rem;
  line-height: 8rem;
`;

export { calendar, schedule };
