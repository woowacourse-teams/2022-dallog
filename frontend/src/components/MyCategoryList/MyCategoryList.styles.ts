import { css, Theme } from '@emotion/react';

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

export { button, list, myCategory, title };
