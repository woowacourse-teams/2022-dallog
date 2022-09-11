import { css, Theme } from '@emotion/react';

const listStyle = ({ flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.column}

  display: ${isSideBarOpen ? 'flex' : 'none'};
  justify-content: flex-start;

  width: 54rem;

  font-size: 4rem;
`;

const headerLayoutStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
`;

const headerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  height: 8rem;

  font-weight: bold;

  cursor: pointer;
`;

const contentStyle = (isListOpen: boolean, listLength: number) => css`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  overflow: hidden;

  width: 100%;
  height: ${isListOpen ? `${8 * listLength}rem` : 0};

  transition: height 0.3s ease-in-out;
`;

export { contentStyle, headerLayoutStyle, headerStyle, listStyle };
