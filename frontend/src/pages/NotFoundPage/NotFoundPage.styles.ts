import { css, Theme } from '@emotion/react';

const layoutStyle = ({ flex }: Theme) => css`
  ${flex.column};

  gap: 15rem;

  height: calc(100% - 16rem);
`;

const buttonStyle = ({ colors }: Theme) => css`
  padding: 3rem 6rem;
  border: 1px solid ${colors.GRAY_800};
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.25);

  font-size: 5rem;
  color: ${colors.GRAY_800};

  &:hover {
    box-shadow: none;
  }
`;

const textStyle = ({ colors }: Theme, fontSize: string) => css`
  font-size: ${fontSize};
  font-weight: 400;
  text-align: center;
  line-height: 150%;
  color: ${colors.GRAY_800};
`;

export { buttonStyle, layoutStyle, textStyle };
