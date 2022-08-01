import { css, Theme } from '@emotion/react';

const categoryPage = ({ flex }: Theme) => css`
  ${flex.column}

  height: 100%;
  padding: 5rem;
`;

const searchForm = css`
  position: relative;

  width: 100%;
`;

const searchButton = css`
  position: absolute;

  width: 12rem;
  height: calc(100% - 5rem);
`;

const searchFieldset = css`
  height: 10rem;
  margin-bottom: 5rem;
`;

const searchInput = css`
  height: 100%;
  padding-left: 12rem;

  font-size: 4rem;
`;

export { categoryPage, searchButton, searchFieldset, searchForm, searchInput };
