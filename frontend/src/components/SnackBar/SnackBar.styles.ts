import { css, Theme } from '@emotion/react';

const snackBarStyle = ({ colors }: Theme, isOpen: boolean) => css`
  position: fixed;
  bottom: ${isOpen ? '5rem' : '-20rem'};
  left: 50%;
  transform: translateX(-50%);

  padding: 4rem;
  border-radius: 3px;

  background: ${colors.YELLOW_500};

  color: ${colors.WHITE};
  font-size: 3.5rem;
  line-height: 3.5rem;
  text-align: center;

  transition: bottom 1s;
`;

export { snackBarStyle };
