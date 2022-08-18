import { css, Global, Theme } from '@emotion/react';
import emotionReset from 'emotion-reset';

const global = ({ colors }: Theme) => css`
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

    *::-webkit-scrollbar {
      width: 2rem;
    }

    *::-webkit-scrollbar-thumb {
      border-radius: 10px;
      background-clip: padding-box;
      border: 2px solid transparent;

      background: ${colors.YELLOW_500};
    }

    *::-webkit-scrollbar-track {
      border-radius: 10px;
      box-shadow: inset 0px 0px 5px white;

      background: ${colors.GRAY_200};
    }
  }
`;

function GlobalStyle() {
  return <Global styles={global} />;
}

export default GlobalStyle;
