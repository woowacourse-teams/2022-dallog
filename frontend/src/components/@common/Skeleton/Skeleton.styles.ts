import { css, Theme } from '@emotion/react';

const skeletonStyle = ({ colors }: Theme, width: string, height: string) => css`
  @keyframes skeleton {
    0% {
      background-color: ${colors.GRAY_300};
    }
    50% {
      background-color: ${colors.GRAY_400};
    }
    100% {
      background-color: ${colors.GRAY_300};
    }
  }

  display: inline-block;

  width: ${width};
  height: ${height};
  border-radius: 4px;

  animation: skeleton 1s infinite ease-out;
`;

export { skeletonStyle };
