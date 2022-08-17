import { css, Theme } from '@emotion/react';

const privacyPolicyStyle = ({ flex }: Theme) => css`
  ${flex.column}

  align-items: flex-start;

  margin: 15rem 10%;
`;

const headerStyle = css`
  margin-bottom: 10rem;

  font-size: 5rem;
  font-weight: bold;
`;

const contentStyle = css`
  white-space: pre-wrap;
`;

export { contentStyle, headerStyle, privacyPolicyStyle };
