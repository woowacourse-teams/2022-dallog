import { css, Theme } from '@emotion/react';

const categoryItem = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content:space-around;

  height: 20rem;
  border-bottom: 1px solid ${colors.GRAY_400};

  font-size: 4rem;
`;

const item = css`
  flex: 1 1 0;
  text-align: center;
`;

export { categoryItem, item };
