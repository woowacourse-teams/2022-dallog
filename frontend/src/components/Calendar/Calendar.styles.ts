import { css, Theme } from '@emotion/react';

const calendar = ({ flex }: Theme) => css`
  ${flex.column}

  width: 100%;
  height: 100%;

  gap: 20px;
`;

const schedule = ({ colors }: Theme) => css`
  padding: 20px;
  border: 1px solid ${colors.BLACK};

  font-size: 16px;
  line-height: 32px;
`;

export { calendar, schedule };
