import { css, Theme } from '@emotion/react';

const layoutStyle = ({ colors, flex }: Theme) => css`
  ${flex.column};

  align-items: flex-start;
  justify-content: center;
  gap: 10rem;

  width: 120rem;
  height: 120rem;
  padding: 12.5rem;
  border-radius: 7px;

  background: ${colors.WHITE};

  color: ${colors.GRAY_700};
`;

const headerStyle = css`
  font-size: 8rem;
  font-weight: bold;
  text-align: center;
`;

const titleStyle = css`
  padding: 0 1rem;
  font-size: 4rem;
`;

const googleSelectBoxStyle = ({ flex }: Theme) => css`
  ${flex.column};

  align-items: flex-start;
  gap: 2rem;

  width: 100%;

  font-size: 4rem;
`;

const formStyle = ({ flex }: Theme) => css`
  ${flex.column};

  align-items: flex-start;

  width: 100%;
  height: 100%;
`;

export { formStyle, googleSelectBoxStyle, headerStyle, layoutStyle, titleStyle };
