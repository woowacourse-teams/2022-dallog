import { css, Theme } from '@emotion/react';

const snackBarStyle = ({ colors }: Theme, isOpen: boolean) => css`
  ${isOpen
    ? css`
        z-index: 50;
        position: fixed;
        bottom: 5rem;
        left: 50%;
        transform: translateX(-50%);

        padding: 4rem;
        border-radius: 3px;

        background: ${colors.YELLOW_500};
        opacity: 0;

        color: ${colors.WHITE};
        font-size: 3.5rem;
        line-height: 3.5rem;
        text-align: center;

        @keyframes show {
          0% {
            opacity: 0;
          }

          50% {
            opacity: 1;
          }

          75% {
            opacity: 1;
          }

          100% {
            opacity: 0;
          }
        }

        animation: show 2.5s;
      `
    : css`
        display: none;
      `}
`;

export { snackBarStyle };
