import { css, Theme } from '@emotion/react';

const scheduleAddButton = ({ colors }: Theme) => css`
  position: fixed;
  right: 52px;
  bottom: 52px;

  width: 44px;
  height: 44px;
  border-radius: 50%;

  background: ${colors.YELLOW_500};

  font-size: 28px;
  line-height: 44px;
  font-weight: 700;
  color: ${colors.WHITE};
`;

export { scheduleAddButton };
