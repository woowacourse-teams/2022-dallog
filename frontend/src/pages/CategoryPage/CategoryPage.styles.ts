import { css, Theme } from '@emotion/react';

const categoryPageStyle = ({ flex }: Theme) => css`
  ${flex.column}

  height: 100%;
  padding: 5rem;
`;

const searchFormStyle = css`
  position: relative;

  width: 100%;
`;

const searchButtonStyle = css`
  position: absolute;

  width: 12rem;
  height: calc(100% - 5rem);
`;

const searchFieldsetStyle = css`
  height: 10rem;
  margin-bottom: 5rem;
`;

const searchInputStyle = css`
  height: 100%;
  padding-left: 12rem;

  font-size: 4rem;
`;

export {
  categoryPageStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
};
