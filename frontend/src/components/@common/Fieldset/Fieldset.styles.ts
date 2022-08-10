import { css, Theme } from '@emotion/react';

const fieldsetStyle = ({ flex }: Theme) => css`
  ${flex.column}

  align-items: flex-start;
  gap: 2.5rem;

  width: 100%;
  height: 15rem;

  font-size: 5rem;
`;

const labelStyle = css`
  padding: 0 1rem;
`;

const inputStyle = ({ colors }: Theme) => css`
  padding: 3rem;

  width: 100%;
  border-radius: 8px;
  border: 1px solid ${colors.GRAY_400};

  font-family: inherit;
  font-size: inherit;

  &:focus {
    outline: none;
    border-color: ${colors.YELLOW_500};
    box-shadow: 0 0 2px ${colors.YELLOW_500};
  }
`;

export { fieldsetStyle, labelStyle, inputStyle };
