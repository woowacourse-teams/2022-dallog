import { css, Theme } from '@emotion/react';

const spinnerStyle = ({ colors }: Theme, size: number) => css`
  position: relative;
  display: inline-block;

  width: ${size}rem;
  height: ${size}rem;

  & div {
    position: absolute;
    display: block;

    width: ${size * 0.8}rem;
    height: ${size * 0.8}rem;
    margin: ${size * 0.1}rem;
    border: ${size * 0.1}rem solid ${colors.GRAY_700};
    box-sizing: border-box;
    border-radius: 50%;
    border-color: ${colors.YELLOW_500} transparent transparent transparent;

    animation: lds-ring 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
  }

  & div:nth-child(1) {
    animation-delay: -0.45s;
  }
  & div:nth-child(2) {
    animation-delay: -0.3s;
  }
  & div:nth-child(3) {
    animation-delay: -0.15s;
  }

  @keyframes lds-ring {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
`;

export { spinnerStyle };
