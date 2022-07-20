import { css, Theme } from '@emotion/react';

const categoryPage = ({ flex }: Theme) => css`
  ${flex.column}

  align-items: center;

  margin-top: 16rem;
  padding: 10rem 20rem;
`;

const categoryNav = css`
  width: 100%;
`;

const categorySearch = css`
  width: 62.5rem;
  height: 12.5rem;
  margin-bottom: 17.5rem;
`;

export { categoryPage, categoryNav, categorySearch };
