import { css, Theme } from '@emotion/react';

const scheduleAddButton = ({ colors, flex, mq }: Theme) => css`
  ${flex.row};

  position: fixed;
  right: 7rem;
  bottom: 7rem;

  width: 13rem;
  height: 13rem;
  border-radius: 50%;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);

  background: ${colors.WHITE};
  opacity: 0.8;

  font-size: 7rem;
  line-height: 7rem;
  color: ${colors.YELLOW_500};

  &:hover {
    opacity: 1;
  }

  ${mq?.mobile} {
    right: 4rem;

    width: 10rem;
    height: 10rem;

    font-size: 6rem;
    line-height: 6rem;
  }
`;

export { scheduleAddButton };
