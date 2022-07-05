import emotionReset from 'emotion-reset';
import { Global, css } from '@emotion/react';

const global = css`
  ${emotionReset}

  *,
  *::after,
  *::before {
    margin: 0;

    box-sizing: border-box;
  }

  body {
    font-family: 'Nanum Gothic', sans-serif;
    font-size: 12px;
  }
`;

function GlobalStyle() {
  return <Global styles={global} />;
}

export default GlobalStyle;
