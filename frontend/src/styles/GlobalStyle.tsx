import emotionReset from 'emotion-reset';
import { Global, css } from '@emotion/react';

const global = css`
  @import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.4/dist/web/static/pretendard.css');

  ${emotionReset}

  *,
  *::after,
  *::before {
    margin: 0;

    box-sizing: border-box;
  }

  body {
    font-family: 'Pretendard', sans-serif;
    font-size: 3rem;
  }
`;

function GlobalStyle() {
  return <Global styles={global} />;
}

export default GlobalStyle;
