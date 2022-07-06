import { css, Theme } from '@emotion/react';

const dimmer = ({ colors, flex }: Theme, isOpen: boolean) => css`
  ${flex.row};

  position: fixed;
  top: 0;
  left: 0;

  width: 100%;
  height: 100%;

  background: ${isOpen ? colors.GRAY_900 : 'transparent'};
  opacity: 0.9;
`;

export { dimmer };
