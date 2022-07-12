import { css, Theme } from '@emotion/react';

const navBar = ({ colors }: Theme) => css`
  position: fixed;
  top: 0;
  left: 0;

  width: 100%;
  height: 16rem;
  padding: 2rem;

  background: ${colors.YELLOW_500};

  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
`;

export { navBar };
