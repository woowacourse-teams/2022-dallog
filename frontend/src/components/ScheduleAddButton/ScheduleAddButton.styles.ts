import { css, Theme } from '@emotion/react';

const scheduleAddButton = ({ colors }: Theme) => css`
  position: fixed;
  right: 13rem;
  bottom: 13rem;

  width: 11rem;
  height: 11rem;
  border-radius: 50%;

  background: ${colors.YELLOW_500};

  font-size: 7rem;
  line-height: 11rem;
  font-weight: 700;
  color: ${colors.WHITE};
`;

export { scheduleAddButton };
