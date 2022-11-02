import { css, Theme } from '@emotion/react';

const calendarPageStyle = ({ mq }: Theme) => css`
  padding: 0 5rem 5rem;

  ${mq?.mobile} {
    padding: 0 2rem;
  }
`;

export { calendarPageStyle };
