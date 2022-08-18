import { css, Theme } from '@emotion/react';

const fieldsetStyle = ({ flex }: Theme) => css`
  ${flex.column}

  align-items: flex-start;
  gap: 2.5rem;

  width: 100%;
  height: auto;

  font-size: 4rem;
`;

const labelStyle = ({ colors }: Theme) => css`
  padding: 0 1rem;

  color: ${colors.GRAY_800};
`;

const inputStyle = ({ colors }: Theme, isValid?: boolean) => css`
  padding: 3rem;

  width: 100%;
  border-radius: 8px;
  border: 1px solid ${isValid === false ? colors.RED_400 : colors.GRAY_400};

  font-family: inherit;
  font-size: inherit;

  &:focus {
    outline: none;
    border-color: ${isValid === false ? colors.RED_400 : colors.YELLOW_500};
    box-shadow: 0 0 2px ${isValid === false ? colors.RED_400 : colors.YELLOW_500};
  }
`;

const errorMessageStyle = ({ colors }: Theme, isValid?: boolean) => css`
  display: ${isValid ? 'none' : 'block'};

  font-size: 3rem;
  color: ${colors.RED_400};
`;

export { errorMessageStyle, fieldsetStyle, labelStyle, inputStyle };
