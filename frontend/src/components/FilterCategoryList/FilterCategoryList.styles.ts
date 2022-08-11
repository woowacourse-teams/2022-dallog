import { css, Theme } from '@emotion/react';

const contentStyle = css`
  display: flex;
  flex-direction: column;
  gap: 2rem;

  width: 100%;
`;

const headerStyle = ({ flex }: Theme) => css`
  ${flex.row}

  justify-content: space-between;

  width: 100%;
  height: 8rem;
  margin-bottom: 2rem;

  font-weight: bold;
`;

const listStyle = ({ flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.column}

  display: ${isSideBarOpen ? 'flex' : 'none'};
  justify-content: flex-start;

  width: 54rem;

  font-size: 4rem;
`;

const skeletonStyle = ({ flex }: Theme) => css`
  ${flex.column};

  gap: 5rem;
`;

export { contentStyle, headerStyle, listStyle, skeletonStyle };
