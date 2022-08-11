import { css, Theme } from '@emotion/react';

const skeletonStyle = ({ colors }: Theme, width: string, height: string) => css`
  @keyframes skeleton {
    0% {
      background-color: transparent;
    }
    25% {
      background-color: ${colors.GRAY_100};
    }
    50% {
      background-color: ${colors.GRAY_200};
    }

    75% {
      background-color: ${colors.GRAY_300};
    }
    100% {
      background-color: transparent;
    }
  }

  display: inline-block;

  width: ${width};
  height: ${height};
  border-radius: 4px;

  animation: skeleton 2s infinite ease-out;
`;

export { skeletonStyle };
