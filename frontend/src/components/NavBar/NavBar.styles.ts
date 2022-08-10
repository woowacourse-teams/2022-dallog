import { css, Theme } from '@emotion/react';

const navBar = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 20;

  width: 100%;
  height: 16rem;
  padding: 2rem 5rem 2rem 2rem;

  background: ${colors.WHITE};

  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
`;

const menus = ({ flex }: Theme) => css`
  ${flex.row}

  gap: 3rem;
`;

const logo = ({ colors, flex }: Theme) => css`
  ${flex.row}

  position: relative;

  background: transparent;

  font-size: 5rem;
  font-weight: bold;
  color: ${colors.GRAY_700};
`;

const logoImg = css`
  width: 6rem;
  height: 6rem;
`;

const logoText = css`
  margin-left: 2rem;
`;

const menu = ({ colors, flex }: Theme) => css`
  ${logo({ colors, flex })}

  width: 11rem;
  height: 11rem;

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

export { logo, logoImg, logoText, menu, menus, menuTitle, navBar };
