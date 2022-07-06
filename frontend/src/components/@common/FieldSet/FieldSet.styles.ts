import { css, Theme } from '@emotion/react';

const fieldSet = ({ flex }: Theme) => css`
  ${flex.column}

  align-items:flex-start;
  gap: 10px;

  font-size: 20px;
`;

const label = css`
  padding: 0 4px;
`;

const input = ({ colors }: Theme) => css`
  width: 500px;
  height: 60px;
  padding: 12px;

  border-radius: 8px;
  border: 1px solid ${colors.GRAY_400};

  font-family: inherit;
  font-size: inherit;
`;

export { fieldSet, label, input };
