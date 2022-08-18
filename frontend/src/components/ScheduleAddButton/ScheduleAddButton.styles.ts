import { css, Theme } from '@emotion/react';

const scheduleAddButton = ({ colors }: Theme) => css`
  position: fixed;
  right: 7rem;
  bottom: 7rem;

  width: 13rem;
  height: 13rem;
  border-radius: 50%;
  box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.25);

  background: ${colors.WHITE};

  font-size: 7rem;
  line-height: 7rem;
  color: ${colors.YELLOW_500};
`;

export { scheduleAddButton };
