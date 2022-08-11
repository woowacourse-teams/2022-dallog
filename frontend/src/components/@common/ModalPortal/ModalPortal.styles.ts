import { css, Theme } from '@emotion/react';

import { TRANSPARENT } from '@/constants/style';

const dimmer = (
  { colors, flex }: Theme,
  isOpen: boolean,
  dimmerBackground: typeof TRANSPARENT | undefined
) => css`
  ${flex.row};

  position: fixed;
  top: 0;
  left: 0;
  z-index: 30;

  width: 100%;
  height: 100%;

  background: ${dimmerBackground !== undefined
    ? dimmerBackground
    : isOpen
    ? `${colors.BLACK}bb`
    : 'transparent'};
`;

export { dimmer };
