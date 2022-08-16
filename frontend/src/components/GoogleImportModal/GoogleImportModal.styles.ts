import { css, Theme } from '@emotion/react';

const layoutStyle = ({ colors, flex }: Theme) => css`
  ${flex.column};
  align-items: flex-start;
  justify-content: center;
  gap: 10rem;

  width: 120rem;
  height: 120rem;
  padding: 12.5rem;
  border-radius: 12px;

  background: ${colors.WHITE};

  color: ${colors.GRAY_700};
`;

const headerStyle = css`
  font-size: 8rem;
  font-weight: bold;
  text-align: center;
`;

const titleStyle = css`
  font-size: 5rem;
  font-weight: bold;
`;

const inputStyle = {
  label: css`
    ${titleStyle};
  `,
};

const googleSelectStyle = ({ colors }: Theme) => css`
  width: 100%;
  height: 13rem;
  padding: 3rem;
  border: 1px solid ${colors.GRAY_400};
  border-radius: 8px;

  font-size: 5rem;
`;

const googleSelectBoxStyle = ({ flex }: Theme) => css`
  ${flex.column};

  align-items: flex-start;

  gap: 2rem;
`;

export {
  googleSelectBoxStyle,
  googleSelectStyle,
  headerStyle,
  inputStyle,
  layoutStyle,
  titleStyle,
};
