import { css, Global, Theme } from '@emotion/react';
import emotionReset from 'emotion-reset';

const global = ({ colors, mq }: Theme) => css`
  ${emotionReset};

  *,
  *::after,
  *::before {
    margin: 0;

    box-sizing: border-box;
  }

  html {
    font-size: 4px;

    ${mq?.mobile} {
      font-size: 3.5px;
    }
  }

  body {
    overflow: overlay;

    font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue',
      'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', 'Apple Color Emoji',
      'Segoe UI Emoji', 'Segoe UI Symbol', sans-serif;
    font-size: 3rem;

    *::-webkit-scrollbar {
      width: 1rem;
    }

    *::-webkit-scrollbar-thumb {
      border-radius: 7px;
      background-clip: padding-box;
      border: 1px solid transparent;

      background: ${colors.GRAY_400};
    }

    *::-webkit-scrollbar-track {
      border-radius: 7px;
      box-shadow: inset 0 0 5px white;
    }
  }
`;

function GlobalStyle() {
  return <Global styles={global} />;
}

export default GlobalStyle;
