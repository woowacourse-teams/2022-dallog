import { css, Theme } from '@emotion/react';

const categoryPageStyle = ({ flex }: Theme) => css`
  ${flex.row};

  justify-content: space-around;
  align-items: flex-start;

  height: 100%;
`;

const calendarStyle = ({ mq }: Theme) => css`
  position: relative;

  width: 65%;

  ${mq?.tablet || mq?.mobile} {
    display: none;
  }
`;

const hintStyle = ({ colors }: Theme) => css`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%);
  z-index: 10;

  padding: 4rem 6rem;
  border-radius: 7px;

  background: ${colors.ORANGE_500};

  font-size: 4rem;
  font-weight: 500;
  color: ${colors.WHITE};
`;

export { calendarStyle, categoryPageStyle, hintStyle };
