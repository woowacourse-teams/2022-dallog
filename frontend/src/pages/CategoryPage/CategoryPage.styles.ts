import { css, Theme } from '@emotion/react';

const categoryPageStyle = css`
  height: 80%;
  padding: 5rem;
`;

const searchFormStyle = css`
  position: relative;

  width: 100%;
`;

const searchButtonStyle = css`
  position: absolute;

  top: 0.5rem;

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

const addButtonStyle = ({ colors }: Theme) => css`
  width: 30rem;
  height: 10rem;
  border-radius: 3px;

  background: ${colors.YELLOW_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.WHITE};
`;

const controlStyle = ({ flex }: Theme) => css`
  ${flex.row};

  align-items: flex-start;
  justify-content: center;
  gap: 4rem;

  width: 100%;
`;

const filteringButtonStyle = ({ colors }: Theme) => css`
  width: 40rem;
  height: 10rem;
  border-radius: 3px;

  border: 1px solid ${colors.YELLOW_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.YELLOW_500};
`;

export {
  addButtonStyle,
  categoryPageStyle,
  controlStyle,
  filteringButtonStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
};
