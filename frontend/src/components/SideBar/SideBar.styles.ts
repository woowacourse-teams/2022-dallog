import { css, Theme } from '@emotion/react';

const sideBar = ({ colors, flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.row}

  position: absolute;
  z-index: 10;

  width: ${isSideBarOpen ? '64rem' : '0'};
  height: 100vh;
  padding: ${isSideBarOpen ? '5rem' : '0'};
  border: 1px solid ${colors.GRAY_400};

  background: ${colors.WHITE};

  transition: width 0.3s;
`;

const list = ({ flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.column}

  display: ${isSideBarOpen ? 'flex' : 'none'};
  gap: 3rem;

  width: 56rem;
`;

const title = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;

  font-size: 4rem;
  font-weight: bold;
`;

const myCategory = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 56rem;

  font-size: 4rem;
`;

const button = css`
  background: transparent;
`;

export { button, list, myCategory, sideBar, title };
