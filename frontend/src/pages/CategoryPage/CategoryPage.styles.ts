import { css, Theme } from '@emotion/react';

const categoryPage = ({ flex }: Theme) => css`
  ${flex.column}

  align-items: center;

  padding: 5rem;
`;

const categoryNav = css`
  width: 100%;
  margin-bottom: 5rem;
`;

const categorySearch = css`
  width: 62.5rem;
  height: 12.5rem;
`;

export { categoryPage, categoryNav, categorySearch };
