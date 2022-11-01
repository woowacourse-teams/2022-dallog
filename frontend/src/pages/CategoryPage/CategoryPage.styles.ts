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

export { calendarStyle, categoryPageStyle };
