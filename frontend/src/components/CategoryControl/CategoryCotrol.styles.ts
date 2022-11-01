import { css, Theme } from '@emotion/react';

const categoryStyle = ({ mq }: Theme) => css`
  width: 30%;

  ${mq?.tablet || mq?.mobile} {
    width: 100%;
  }
`;

const categoryHeaderStyle = ({ colors }: Theme) => css`
  padding: 1rem 3rem 4rem;

  font-size: 6rem;
  font-weight: 600;
  color: ${colors.GRAY_700};
`;

const controlStyle = ({ flex }: Theme) => css`
  ${flex.row};

  align-items: flex-start;
  justify-content: center;
  gap: 4rem;
`;

const searchFormStyle = css`
  position: relative;

  width: 100%;
  height: 12rem;
  margin-bottom: 5rem;
`;

const searchButtonStyle = css`
  position: absolute;
  z-index: 5;

  top: 50%;
  transform: translateY(-50%);

  width: 10rem;
`;

const searchFieldsetStyle = css`
  height: 100%;
`;

const searchInputStyle = css`
  height: 100%;
  padding-left: 10rem;

  font-size: 4rem;
`;

const buttonStyle = ({ colors }: Theme) => css`
  width: 20rem;
  height: 12rem;
  border-radius: 7px;
  border: 1px solid ${colors.GRAY_500};

  background: ${colors.YELLOW_500};

  font-size: 4rem;
  font-weight: 600;
  color: ${colors.WHITE};

  &:hover {
    box-shadow: none;
  }
`;

export {
  buttonStyle,
  categoryHeaderStyle,
  categoryStyle,
  controlStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
};
