import { css, Global } from '@emotion/react';
import emotionReset from 'emotion-reset';

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
    overflow: overlay;

    font-family: 'Pretendard', sans-serif;
    font-size: 3rem;
  }
`;

function GlobalStyle() {
  return <Global styles={global} />;
}

export default GlobalStyle;
