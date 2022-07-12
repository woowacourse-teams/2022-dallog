import { css, Theme } from '@emotion/react';

const fieldSet = ({ flex }: Theme) => css`
  ${flex.column}

  width: 100%;
  height: 15rem;

  align-items: flex-start;
  gap: 2.5rem;

  font-size: 5rem;
`;

const label = css`
  padding: 0 1rem;
`;

const input = ({ colors }: Theme) => css`
  padding: 3rem;

  width: 100%;

  border-radius: 8px;
  border: 1px solid ${colors.GRAY_400};

  font-family: inherit;
  font-size: inherit;
`;

export { fieldSet, label, input };
