import { css, Theme } from '@emotion/react';

const categoryTable = css`
  width: 100%;
`;

const categoryTableHeader = ({ flex, colors }: Theme) => css`
  ${flex.row}

  justify-content: space-around;

  height: 12rem;
  border-bottom: 2px solid ${colors.GRAY_400};

  background: ${colors.GRAY_100};

  font-size: 4rem;
  font-weight: 700;
`;

const intersectTarget = css`
  height: 10rem;
`;

export { categoryTable, categoryTableHeader, intersectTarget };
