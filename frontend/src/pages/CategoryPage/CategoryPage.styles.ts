import { css, Theme } from '@emotion/react';

const categoryPageStyle = css`
  height: 80%;
  padding: 9rem;
`;

const searchFormStyle = css`
  position: relative;

  width: 100%;
  height: 12rem;
  margin-bottom: 8rem;
`;

const searchButtonStyle = css`
  position: absolute;

  top: 50%;
  transform: translateY(-50%);

  width: 12rem;
`;

const searchFieldsetStyle = css`
  height: 100%;
`;

const searchInputStyle = css`
  height: 100%;
  padding-left: 12rem;

  font-size: 4rem;
`;

const buttonStyle = ({ colors }: Theme) => css`
  width: 30rem;
  height: 12rem;
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

const outLineButtonStyle = ({ colors }: Theme) => css`
  width: 40rem;
  height: 12rem;
  border-radius: 3px;

  border: 1px solid ${colors.YELLOW_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.YELLOW_500};
`;

export {
  buttonStyle,
  categoryPageStyle,
  controlStyle,
  outLineButtonStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
};
