import { css, Theme } from '@emotion/react';

const layoutStyle = ({ flex }: Theme) => css`
  ${flex.column};

  gap: 30rem;
  height: 100vh;
`;

const buttonStyle = ({ colors }: Theme) => css`
  padding: 3rem;
  border: 1px solid ${colors.GRAY_800};
  border-radius: 8px;

  font-size: 5rem;
  color: ${colors.GRAY_800};
`;

const textStyle = ({ colors }: Theme, fontSize: string) => css`
  font-size: ${fontSize};
  font-weight: 400;
  color: ${colors.GRAY_800};
`;

export { buttonStyle, layoutStyle, textStyle };
