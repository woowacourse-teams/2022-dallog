import { css, Theme } from '@emotion/react';

const menu = ({ colors, flex }: Theme) => css`
  ${flex.row}

  position: relative;

  width: 11rem;
  height: 11rem;

  background: transparent;

  font-size: 7rem;
  font-weight: bold;
  color: ${colors.GRAY_700};

  &:hover {
    border-radius: 50%;

    background: ${colors.GRAY_100};

    filter: none;
  }

  &:hover span {
    visibility: visible;
  }
`;

const menuTitle = ({ colors }: Theme) => css`
  visibility: hidden;
  position: absolute;
  top: 120%;
  left: 50%;
  transform: translateX(-50%);

  padding: 2rem 3rem;

  background: ${colors.GRAY_700}ee;

  font-size: 3rem;
  font-weight: normal;
  color: ${colors.WHITE};
  white-space: nowrap;
`;

export { menu, menuTitle };
