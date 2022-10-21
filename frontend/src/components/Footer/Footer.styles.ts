import { css, Theme } from '@emotion/react';

const footerStyle = ({ colors, flex }: Theme) => css`
  ${flex.column};

  width: 100%;
  height: 40rem;

  color: ${colors.GRAY_600};
  line-height: 150%;
`;

const privacyPolicyButtonStyle = css`
  margin: 1rem;

  font-size: inherit;
  color: inherit;
`;

export { footerStyle, privacyPolicyButtonStyle };
