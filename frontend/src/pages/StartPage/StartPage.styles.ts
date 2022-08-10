import { css, Theme } from '@emotion/react';

const calendarStyle = ({ flex, colors }: Theme) => css`
  ${flex.column};

  gap: 2rem;
  position: relative;
  left: -20%;
  top: -10rem;

  width: 200rem;
  border: 1px solid ${colors.GRAY_400};
  box-sizing: content-box;
  box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.25);

  transform: rotate(10deg);

  @keyframes fadeIn {
    from {
      opacity: 0;
    }

    to {
      opacity: 1;
    }
  }

  animation-name: fadeIn;
  animation-duration: 1s;
  animation-timing-function: ease-in-out;
  animation-fill-mode: forwards;
`;

const dateItemStyle = ({ colors }: Theme) => css`
  width: 200rem;
  height: 30rem;
  padding: 6rem 6rem 4rem;

  background: ${colors.WHITE};

  font-size: 20rem;
  text-align: right;
`;

const itemStyle = css`
  position: relative;
  left: -100%;

  width: 200rem;
  height: 50rem;
  padding: 4rem;

  font-size: 10rem;
  font-weight: 600;
  text-align: right;

  @keyframes slideIn {
    from {
      opacity: 0;
      left: -100%;
    }

    to {
      left: 0;
      opacity: 1;
    }
  }

  animation-name: slideIn;
  animation-duration: 2s;
  animation-timing-function: ease-in-out;
  animation-fill-mode: forwards;
`;

const firstItemStyle = ({ colors }: Theme) => css`
  ${itemStyle};

  background: ${colors.YELLOW_500};

  animation-delay: 0.5s;
`;

const secondItemStyle = ({ colors }: Theme) => css`
  ${itemStyle};

  background: ${colors.ORANGE_500};

  animation-delay: 1s;
`;

const thirdItemStyle = ({ colors }: Theme) => css`
  ${itemStyle};

  background: ${colors.BLUE_500};

  animation-delay: 1.5s;
`;

const blackTextStyle = ({ colors }: Theme) => css`
  width: 100%;

  font-size: 20rem;
  font-weight: 700;
  line-height: 25rem;
  color: ${colors.BLACK};
  text-shadow: 5px 5px 10px rgba(0, 0, 0, 0.25);
`;

const whiteTextStyle = ({ colors }: Theme) => css`
  width: 100%;

  font-size: 20rem;
  font-weight: 700;
  line-height: 25rem;
  color: ${colors.WHITE};
  text-shadow: 5px 5px 10px rgba(0, 0, 0, 0.25);
`;

const introductionStyle = css`
  width: 100%;
  margin: 20rem 20rem 0 0;

  text-align: right;
`;

const detailTextStyle = ({ colors }: Theme) => css`
  font-size: 3.5rem;
  color: ${colors.GRAY_500};
`;

const loginText = css`
  width: 100%;
`;

const googleLoginButton = ({ colors, flex }: Theme) => css`
  ${flex.row}

  position: relative;
  justify-content: flex-start;

  width: 75rem;
  height: 15rem;
  padding: 4rem;
  border-radius: 8px;
  border: 1px solid ${colors.GRAY_400};
  box-shadow: 2px 2px 2px ${colors.GRAY_400};

  background: ${colors.WHITE};

  font-size: 4rem;
  font-weight: 500;
  color: ${colors.BLACK}8a;

  &:hover {
    box-shadow: 3px 3px 3px ${colors.GRAY_500};
  }
`;

const mainContentStyle = ({ flex }: Theme) => css`
  ${flex.column};

  align-items: flex-end;
  gap: 6rem;
`;

const firstSectionStyle = ({ flex }: Theme) => css`
  ${flex.row};

  align-items: flex-start;
`;

const secondSectionStyle = ({ flex, colors }: Theme) => css`
  ${flex.column};

  align-items: center;
  justify-content: flex-start;

  width: 100%;
  height: 100%;
  margin-top: 20rem;
  padding: 20rem auto;
  gap: 20rem;

  background: ${colors.GRAY_200};
`;

const pageStyle = ({ flex }: Theme) => css`
  ${flex.column};

  overflow-x: hidden;
`;
const methodHeaderStyle = ({ colors }: Theme) => css`
  margin-top: 10rem;

  font-size: 5rem;
  color: ${colors.GRAY_500};
`;

const methodTextStyle = ({ colors }: Theme) => css`
  font-size: 6rem;
  line-height: 10rem;
  color: ${colors.GRAY_500};
`;

const methodItemStyle = ({ flex }: Theme, isShowing: boolean, direction: 'left' | 'right') => css`
  ${flex.row}

  position:relative;
  gap: 20rem;

  width: 70%;
  height: 100%;
  padding: 20rem;

  @keyframes slideInFromLeft {
    from {
      opacity: 0;
      left: -100%;
    }

    to {
      left: 0;
      opacity: 1;
    }
  }

  @keyframes slideInFromRight {
    from {
      opacity: 0;
      left: 100%;
    }

    to {
      left: 0;
      opacity: 1;
    }
  }

  ${isShowing &&
  css`
    animation-name: ${direction === 'left' ? 'slideInFromLeft' : 'slideInFromRight'};
    animation-duration: 0.7s;
    animation-timing-function: ease-in-out;
    animation-fill-mode: forwards;
  `}
`;

const imageStyle = css`
  width: 120rem;
  height: auto;
`;

const refStyle = css`
  height: 1rem;
`;

export {
  blackTextStyle,
  calendarStyle,
  detailTextStyle,
  dateItemStyle,
  firstItemStyle,
  firstSectionStyle,
  googleLoginButton,
  loginText,
  imageStyle,
  introductionStyle,
  mainContentStyle,
  methodHeaderStyle,
  methodItemStyle,
  methodTextStyle,
  pageStyle,
  refStyle,
  secondItemStyle,
  secondSectionStyle,
  thirdItemStyle,
  whiteTextStyle,
};
