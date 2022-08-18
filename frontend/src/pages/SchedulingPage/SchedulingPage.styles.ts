import { css, Theme } from '@emotion/react';

const pageStyle = ({ flex }: Theme) => css`
  ${flex.column}

  padding: 9rem 10%;
`;

const formStyle = ({ flex }: Theme) => css`
  ${flex.row}

  gap: 6rem;

  max-width: 100%;

  font-size: 4rem;
`;

const subscriptionStyle = ({ flex }: Theme) => css`
  ${flex.column}

  align-items: flex-start;
  gap: 2.5rem;
`;

const labelStyle = ({ colors }: Theme) => css`
  padding: 0 1rem;

  font-size: 4rem;
  color: ${colors.GRAY_800};
`;

const subscriptionSelectStyle = ({ colors }: Theme) => css`
  width: 80rem;
  height: 12rem;
  padding: 3rem;
  border: 1px solid ${colors.GRAY_400};
  border-radius: 8px;

  font-size: inherit;
`;

const dateTimeStyle = ({ flex }: Theme) => css`
  ${flex.row}

  gap: 3rem;

  width: 120rem;
`;

const dateTimeFieldsetStyle = css`
  height: auto;

  font-size: 4rem;
`;

const resultStyle = ({ flex }: Theme) => css`
  ${flex.column}

  overflow-y: overlay;
  justify-content: flex-start;
  gap: 5rem;

  width: 214rem;
  height: 60vh;
  padding: 0 4rem;
`;

const searchButtonStyle = ({ colors, flex }: Theme) => css`
  ${flex.row}

  gap: 5rem;

  width: 206rem;
  padding: 5rem;
  margin: 8rem 0 5rem;
  border-radius: 8px;
  box-shadow: 2px 2px 4px ${colors.GRAY_500};

  background: ${colors.ORANGE_500};

  font-size: 4rem;
  font-weight: 500;
  color: ${colors.WHITE};

  &:hover {
    box-shadow: none;
  }
`;

const resultTimeStyle = ({ flex }: Theme) => css`
  ${flex.row}

  gap: 3rem;

  width: 100%;
`;

const resultDateTimeStyle = ({ colors }: Theme) => css`
  width: 100%;
  padding: 6rem;
  border-radius: 8px;

  background: ${colors.BLUE_500};

  font-size: 4rem;
  font-weight: 500;
  color: white;
  text-align: center;
`;

export {
  dateTimeFieldsetStyle,
  dateTimeStyle,
  formStyle,
  labelStyle,
  pageStyle,
  resultDateTimeStyle,
  resultStyle,
  resultTimeStyle,
  searchButtonStyle,
  subscriptionSelectStyle,
  subscriptionStyle,
};
