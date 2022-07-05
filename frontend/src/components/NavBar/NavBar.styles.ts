// eslint-disable-next-line import/named
import { css, Theme } from '@emotion/react';

const navBar = ({ colors }: Theme) => css`
  width: 100%;
  height: 64px;
  padding: 8px;

  background: ${colors.yellow_500};

  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
`;

export { navBar };
